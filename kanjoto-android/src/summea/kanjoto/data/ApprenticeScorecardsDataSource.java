
package summea.kanjoto.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import summea.kanjoto.model.ApprenticeScorecard;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ApprenticeScorecardsDataSource {
    private SQLiteDatabase database;
    private KanjotoDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            KanjotoDatabaseHelper.COLUMN_ID,
            KanjotoDatabaseHelper.COLUMN_TAKEN_AT,
            KanjotoDatabaseHelper.COLUMN_TOTAL,
            KanjotoDatabaseHelper.COLUMN_CORRECT,
            KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID,
            KanjotoDatabaseHelper.COLUMN_GRAPH_ID,
            KanjotoDatabaseHelper.COLUMN_SCALE_ID,
    };

    /**
     * ApprenticeScorecardsDataSource constructor.
     * 
     * @param context Current state.
     */
    public ApprenticeScorecardsDataSource(Context context) {
        dbHelper = new KanjotoDatabaseHelper(context);
    }

    /**
     * ApprenticeScorecardsDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public ApprenticeScorecardsDataSource(Context context, String databaseName) {
        dbHelper = new KanjotoDatabaseHelper(context, databaseName);
    }

    /**
     * Open database.
     * 
     * @throws SQLException
     */
    public void open() throws SQLException {
        setDatabase(dbHelper.getWritableDatabase());
    }

    /**
     * Close database.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Create apprenticeScorecard row in database.
     * 
     * @param apprenticeScorecardvalues String of apprenticeScorecard values to insert.
     * @return ApprenticeScorecard of newly-created apprenticeScorecard data.
     */
    public ApprenticeScorecard createApprenticeScorecard(ApprenticeScorecard apprenticeScorecard) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_TAKEN_AT, apprenticeScorecard.getTakenAt());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_TOTAL, apprenticeScorecard.getTotal());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_CORRECT, apprenticeScorecard.getCorrect());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID,
                apprenticeScorecard.getApprenticeId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_GRAPH_ID, apprenticeScorecard.getGraphId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_SCALE_ID, apprenticeScorecard.getScaleId());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db
                .insert(KanjotoDatabaseHelper.TABLE_APPRENTICE_SCORECARDS, null,
                        contentValues);

        Cursor cursor = db.query(
                KanjotoDatabaseHelper.TABLE_APPRENTICE_SCORECARDS, allColumns,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();

        ApprenticeScorecard newApprenticeScorecard = cursorToApprenticeScorecard(cursor);
        cursor.close();
        db.close();

        return newApprenticeScorecard;
    }

    /**
     * Delete apprenticeScorecard row from database.
     * 
     * @param apprenticeScorecard ApprenticeScorecard to delete.
     */
    public void deleteApprenticeScorecard(ApprenticeScorecard apprenticeScorecard) {
        long id = apprenticeScorecard.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete apprenticeScorecard
        db.delete(KanjotoDatabaseHelper.TABLE_APPRENTICE_SCORECARDS,
                KanjotoDatabaseHelper.COLUMN_ID + " = " + id, null);

        db.close();
    }

    /**
     * Get all apprenticeScorecards from database table.
     * 
     * @return List of ApprenticeScorecards.
     */
    public List<ApprenticeScorecard> getAllApprenticeScorecards(long apprenticeId, String orderBy) {
        List<ApprenticeScorecard> apprenticeScorecards = new ArrayList<ApprenticeScorecard>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_APPRENTICE_SCORECARDS
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";
        if (orderBy != null && !orderBy.isEmpty()) {
            query += " ORDER BY " + orderBy + " ASC";
        }

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId),
        });

        ApprenticeScorecard apprenticeScorecard = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                apprenticeScorecard = new ApprenticeScorecard();
                apprenticeScorecard.setId(cursor.getLong(0));
                apprenticeScorecard.setTakenAt(cursor.getString(1));
                apprenticeScorecard.setTotal(cursor.getInt(2));
                apprenticeScorecard.setCorrect(cursor.getInt(3));
                apprenticeScorecard.setApprenticeId(cursor.getInt(4));
                apprenticeScorecard.setGraphId(cursor.getLong(5));
                apprenticeScorecard.setScaleId(cursor.getLong(6));

                // add note string to list of strings
                apprenticeScorecards.add(apprenticeScorecard);
            } while (cursor.moveToNext());
        }

        db.close();

        return apprenticeScorecards;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return ApprenticeScorecard
     */
    private ApprenticeScorecard cursorToApprenticeScorecard(Cursor cursor) {
        ApprenticeScorecard apprenticeScorecard = new ApprenticeScorecard();
        apprenticeScorecard.setId(cursor.getLong(0));
        apprenticeScorecard.setTakenAt(cursor.getString(1));
        apprenticeScorecard.setTotal(cursor.getInt(2));
        apprenticeScorecard.setCorrect(cursor.getInt(3));
        apprenticeScorecard.setApprenticeId(cursor.getInt(4));
        apprenticeScorecard.setGraphId(cursor.getLong(5));
        apprenticeScorecard.setScaleId(cursor.getLong(6));
        return apprenticeScorecard;
    }

    /**
     * getAllApprenticeScorecards gets a preview list of all apprenticeScorecards.
     * 
     * @return List of ApprenticeScorecard preview strings.
     */
    public List<String> getAllApprenticeScorecardListPreviews(long apprenticeId) {
        List<String> apprenticeScorecards = new LinkedList<String>();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_APPRENTICE_SCORECARDS
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all apprenticeScorecards from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId)
        });

        ApprenticeScorecard apprenticeScorecard = null;
        if (cursor.moveToFirst()) {
            do {
                // create apprenticeScorecard objects based on apprenticeScorecard data from
                // database
                apprenticeScorecard = new ApprenticeScorecard();
                apprenticeScorecard.setId(cursor.getLong(0));
                apprenticeScorecard.setTakenAt(cursor.getString(1));
                apprenticeScorecard.setTotal(cursor.getInt(2));
                apprenticeScorecard.setCorrect(cursor.getInt(3));
                apprenticeScorecard.setApprenticeId(cursor.getInt(4));
                apprenticeScorecard.setGraphId(cursor.getLong(5));
                apprenticeScorecard.setScaleId(cursor.getLong(6));

                // add apprenticeScorecard string to list of strings
                apprenticeScorecards.add(apprenticeScorecard.toString());
            } while (cursor.moveToNext());
        }

        db.close();

        return apprenticeScorecards;
    }

    public ApprenticeScorecard getApprenticeScorecard(long apprenticeScorecardId) {
        ApprenticeScorecard apprenticeScorecard = new ApprenticeScorecard();

        String query = "SELECT * FROM " + KanjotoDatabaseHelper.TABLE_APPRENTICE_SCORECARDS
                + " WHERE " + KanjotoDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all apprenticeScorecards from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeScorecardId)
        });

        if (cursor.moveToFirst()) {
            do {
                // create apprenticeScorecard objects based on apprenticeScorecard data from
                // database
                apprenticeScorecard = new ApprenticeScorecard();
                apprenticeScorecard.setId(cursor.getLong(0));
                apprenticeScorecard.setTakenAt(cursor.getString(1));
                apprenticeScorecard.setTotal(cursor.getInt(2));
                apprenticeScorecard.setCorrect(cursor.getInt(3));
                apprenticeScorecard.setApprenticeId(cursor.getInt(4));
                apprenticeScorecard.setGraphId(cursor.getLong(5));
                apprenticeScorecard.setScaleId(cursor.getLong(6));
            } while (cursor.moveToNext());
        }

        db.close();

        return apprenticeScorecard;
    }

    public ApprenticeScorecard updateApprenticeScorecard(ApprenticeScorecard apprenticeScorecard) {
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KanjotoDatabaseHelper.COLUMN_ID, apprenticeScorecard.getId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_TAKEN_AT, apprenticeScorecard.getTakenAt());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_TOTAL, apprenticeScorecard.getTotal());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_CORRECT, apprenticeScorecard.getCorrect());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_APPRENTICE_ID,
                apprenticeScorecard.getApprenticeId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_GRAPH_ID, apprenticeScorecard.getGraphId());
        contentValues.put(KanjotoDatabaseHelper.COLUMN_SCALE_ID, apprenticeScorecard.getScaleId());

        db.update(KanjotoDatabaseHelper.TABLE_APPRENTICE_SCORECARDS, contentValues,
                KanjotoDatabaseHelper.COLUMN_ID + "=" + apprenticeScorecard.getId(), null);

        db.close();

        return apprenticeScorecard;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
