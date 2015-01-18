
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.model.ApprenticeScorecard;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ApprenticeScorecardsDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_TAKEN_AT,
    };

    /**
     * ApprenticeScorecardsDataSource constructor.
     * 
     * @param context Current state.
     */
    public ApprenticeScorecardsDataSource(Context context) {
        dbHelper = new OtashuDatabaseHelper(context);
    }

    /**
     * Open database.
     * 
     * @throws SQLException
     */
    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
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
        contentValues.put(OtashuDatabaseHelper.COLUMN_TAKEN_AT, apprenticeScorecard.getTakenAt());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db
                .insert(OtashuDatabaseHelper.TABLE_APPRENTICE_SCORECARDS, null,
                        contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_APPRENTICE_SCORECARDS, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();

        ApprenticeScorecard newApprenticeScorecard = cursorToApprenticeScorecard(cursor);
        cursor.close();
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
        db.delete(OtashuDatabaseHelper.TABLE_APPRENTICE_SCORECARDS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Get all apprenticeScorecards from database table.
     * 
     * @return List of ApprenticeScorecards.
     */
    public List<ApprenticeScorecard> getAllApprenticeScorecards() {
        List<ApprenticeScorecard> apprenticeScorecards = new ArrayList<ApprenticeScorecard>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_APPRENTICE_SCORECARDS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        ApprenticeScorecard apprenticeScorecard = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                apprenticeScorecard = new ApprenticeScorecard();
                apprenticeScorecard.setId(cursor.getLong(0));
                apprenticeScorecard.setTakenAt(cursor.getString(1));

                // add note string to list of strings
                apprenticeScorecards.add(apprenticeScorecard);
            } while (cursor.moveToNext());
        }

        return apprenticeScorecards;
    }

    /**
     * Get all apprenticeScorecard ids from database table.
     * 
     * @return List of ApprenticeScorecards ids.
     */
    public List<Integer> getAllApprenticeScorecardIds() {
        List<Integer> apprenticeScorecard_ids = new ArrayList<Integer>();

        Cursor cursor = database.query(
                OtashuDatabaseHelper.TABLE_APPRENTICE_SCORECARDS, allColumns, null,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            ApprenticeScorecard apprenticeScorecard = cursorToApprenticeScorecard(cursor);
            apprenticeScorecard_ids.add((int) apprenticeScorecard.getId());
            cursor.moveToNext();
        }

        cursor.close();
        return apprenticeScorecard_ids;
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
        return apprenticeScorecard;
    }

    /**
     * getAllApprenticeScorecards gets a preview list of all apprenticeScorecards.
     * 
     * @return List of ApprenticeScorecard preview strings.
     */
    public List<String> getAllApprenticeScorecardListPreviews() {
        List<String> apprenticeScorecards = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_APPRENTICE_SCORECARDS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all apprenticeScorecards from database
        Cursor cursor = db.rawQuery(query, null);

        ApprenticeScorecard apprenticeScorecard = null;
        if (cursor.moveToFirst()) {
            do {
                // create apprenticeScorecard objects based on apprenticeScorecard data from
                // database
                apprenticeScorecard = new ApprenticeScorecard();
                apprenticeScorecard.setId(cursor.getLong(0));
                apprenticeScorecard.setTakenAt(cursor.getString(1));

                // add apprenticeScorecard string to list of strings
                apprenticeScorecards.add(apprenticeScorecard.toString());
            } while (cursor.moveToNext());
        }

        return apprenticeScorecards;
    }

    /**
     * Get a list of all apprenticeScorecards ids.
     * 
     * @return List of ApprenticeScorecard ids.
     */
    public List<Long> getAllApprenticeScorecardListDBTableIds() {
        List<Long> apprenticeScorecards = new LinkedList<Long>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_APPRENTICE_SCORECARDS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all apprenticeScorecards from database
        Cursor cursor = db.rawQuery(query, null);

        ApprenticeScorecard apprenticeScorecard = null;
        if (cursor.moveToFirst()) {
            do {
                // create apprenticeScorecard objects based on apprenticeScorecard data from
                // database
                apprenticeScorecard = new ApprenticeScorecard();
                apprenticeScorecard.setId(cursor.getLong(0));
                apprenticeScorecard.setTakenAt(cursor.getString(1));

                // add apprenticeScorecard to apprenticeScorecards list
                apprenticeScorecards.add(apprenticeScorecard.getId());
            } while (cursor.moveToNext());
        }

        return apprenticeScorecards;
    }

    public ApprenticeScorecard getApprenticeScorecard(long apprenticeScorecardId) {
        ApprenticeScorecard apprenticeScorecard = new ApprenticeScorecard();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_APPRENTICE_SCORECARDS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=" + apprenticeScorecardId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all apprenticeScorecards from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create apprenticeScorecard objects based on apprenticeScorecard data from
                // database
                apprenticeScorecard = new ApprenticeScorecard();
                apprenticeScorecard.setId(cursor.getLong(0));
                apprenticeScorecard.setTakenAt(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        return apprenticeScorecard;
    }

    public ApprenticeScorecard updateApprenticeScorecard(ApprenticeScorecard apprenticeScorecard) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, apprenticeScorecard.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_TAKEN_AT, apprenticeScorecard.getTakenAt());

        db.update(OtashuDatabaseHelper.TABLE_APPRENTICE_SCORECARDS, contentValues,
                OtashuDatabaseHelper.COLUMN_ID + "=" + apprenticeScorecard.getId(), null);

        return apprenticeScorecard;
    }

    public ApprenticeScorecard getRandomApprenticeScorecard() {
        ApprenticeScorecard apprenticeScorecard = new ApprenticeScorecard();

        // get all apprenticeScorecards first
        List<ApprenticeScorecard> allApprenticeScorecards = getAllApprenticeScorecards();

        // choose random apprenticeScorecard
        int chosenIndex = new Random().nextInt(allApprenticeScorecards.size());

        apprenticeScorecard = allApprenticeScorecards.get(chosenIndex);

        return apprenticeScorecard;
    }
}
