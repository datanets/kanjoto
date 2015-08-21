
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.model.ApprenticeScore;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ApprenticeScoresDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_SCORECARD_ID,
            OtashuDatabaseHelper.COLUMN_QUESTION_NUMBER,
            OtashuDatabaseHelper.COLUMN_CORRECT,
            OtashuDatabaseHelper.COLUMN_NOTEVALUE,
    };

    /**
     * ApprenticeScoresDataSource constructor.
     * 
     * @param context Current state.
     */
    public ApprenticeScoresDataSource(Context context) {
        dbHelper = new OtashuDatabaseHelper(context);
    }

    /**
     * ApprenticeScoresDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public ApprenticeScoresDataSource(Context context, String databaseName) {
        dbHelper = new OtashuDatabaseHelper(context, databaseName);
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
     * Create apprenticeScore row in database.
     * 
     * @param apprenticeScorevalues String of apprenticeScore values to insert.
     * @return ApprenticeScore of newly-created apprenticeScore data.
     */
    public ApprenticeScore createApprenticeScore(ApprenticeScore apprenticeScore) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_SCORECARD_ID,
                apprenticeScore.getScorecardId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_QUESTION_NUMBER,
                apprenticeScore.getQuestionNumber());
        contentValues.put(OtashuDatabaseHelper.COLUMN_CORRECT, apprenticeScore.getCorrect());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NOTEVALUE, apprenticeScore.getNotevalue());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db
                .insert(OtashuDatabaseHelper.TABLE_APPRENTICE_SCORES, null,
                        contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_APPRENTICE_SCORES, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();

        ApprenticeScore newApprenticeScore = cursorToApprenticeScore(cursor);
        cursor.close();
        db.close();

        return newApprenticeScore;
    }

    /**
     * Delete apprenticeScore row from database.
     * 
     * @param apprenticeScore ApprenticeScore to delete.
     */
    public void deleteApprenticeScore(ApprenticeScore apprenticeScore) {
        long id = apprenticeScore.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete apprenticeScore
        db.delete(OtashuDatabaseHelper.TABLE_APPRENTICE_SCORES,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);

        db.close();
    }

    /**
     * Get all apprenticeScores from database table for a specific Scorecard
     * 
     * @return List of ApprenticeScores.
     */
    public List<ApprenticeScore> getAllApprenticeScores() {
        List<ApprenticeScore> apprenticeScores = new ArrayList<ApprenticeScore>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_APPRENTICE_SCORES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        ApprenticeScore apprenticeScore = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                apprenticeScore = new ApprenticeScore();
                apprenticeScore.setId(cursor.getLong(0));
                apprenticeScore.setScorecardId(cursor.getLong(1));
                apprenticeScore.setQuestionNumber(cursor.getInt(2));
                apprenticeScore.setCorrect(cursor.getInt(3));
                apprenticeScore.setNotevalue(cursor.getLong(4));

                // add note string to list of strings
                apprenticeScores.add(apprenticeScore);
            } while (cursor.moveToNext());
        }

        db.close();

        return apprenticeScores;
    }

    /**
     * Get all apprenticeScores from database table for a specific Scorecard
     * 
     * @return List of ApprenticeScores.
     */
    public List<ApprenticeScore> getAllApprenticeScoresByScorecard(long scorecardId) {
        List<ApprenticeScore> apprenticeScores = new ArrayList<ApprenticeScore>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_APPRENTICE_SCORES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_SCORECARD_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(scorecardId)
        });

        ApprenticeScore apprenticeScore = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                apprenticeScore = new ApprenticeScore();
                apprenticeScore.setId(cursor.getLong(0));
                apprenticeScore.setScorecardId(cursor.getLong(1));
                apprenticeScore.setQuestionNumber(cursor.getInt(2));
                apprenticeScore.setCorrect(cursor.getInt(3));
                apprenticeScore.setNotevalue(cursor.getLong(4));

                // add note string to list of strings
                apprenticeScores.add(apprenticeScore);
            } while (cursor.moveToNext());
        }

        db.close();

        return apprenticeScores;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return ApprenticeScore
     */
    private ApprenticeScore cursorToApprenticeScore(Cursor cursor) {
        ApprenticeScore apprenticeScore = new ApprenticeScore();
        apprenticeScore.setId(cursor.getLong(0));
        apprenticeScore.setScorecardId(cursor.getLong(1));
        apprenticeScore.setQuestionNumber(cursor.getInt(2));
        apprenticeScore.setCorrect(cursor.getInt(3));
        apprenticeScore.setNotevalue(cursor.getLong(4));
        return apprenticeScore;
    }

    public ApprenticeScore getApprenticeScore(long apprenticeScoreId) {
        ApprenticeScore apprenticeScore = new ApprenticeScore();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_APPRENTICE_SCORES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all apprenticeScores from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeScoreId)
        });

        if (cursor.moveToFirst()) {
            do {
                // create apprenticeScore objects based on apprenticeScore data from database
                apprenticeScore = new ApprenticeScore();
                apprenticeScore.setId(cursor.getLong(0));
                apprenticeScore.setScorecardId(cursor.getLong(1));
                apprenticeScore.setQuestionNumber(cursor.getInt(2));
                apprenticeScore.setCorrect(cursor.getInt(3));
                apprenticeScore.setNotevalue(cursor.getLong(4));
            } while (cursor.moveToNext());
        }

        db.close();

        return apprenticeScore;
    }

    public ApprenticeScore updateApprenticeScore(ApprenticeScore apprenticeScore) {
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, apprenticeScore.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_SCORECARD_ID,
                apprenticeScore.getScorecardId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_QUESTION_NUMBER,
                apprenticeScore.getQuestionNumber());
        contentValues.put(OtashuDatabaseHelper.COLUMN_CORRECT, apprenticeScore.getCorrect());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NOTEVALUE, apprenticeScore.getNotevalue());

        db.update(OtashuDatabaseHelper.TABLE_APPRENTICE_SCORES, contentValues,
                OtashuDatabaseHelper.COLUMN_ID + "=" + apprenticeScore.getId(), null);

        db.close();

        return apprenticeScore;
    }

    /**
     * Get total number of correct scores from database table for a specific Scorecard
     * 
     * @return int of total number correct
     */
    public int getCorrectApprenticeScoresCount(long scorecardId) {
        int totalCorrect = 0;

        String query = "SELECT COUNT(*) FROM " + OtashuDatabaseHelper.TABLE_APPRENTICE_SCORES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_SCORECARD_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_CORRECT + "=?"
                + " GROUP BY " + OtashuDatabaseHelper.COLUMN_QUESTION_NUMBER;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(scorecardId),
                String.valueOf(1)
        });
        totalCorrect = cursor.getCount();

        db.close();

        return totalCorrect;
    }

    /**
     * Get total number of correct scores from database table for a specific Scorecard
     * 
     * @return int of total number correct
     */
    public int getApprenticeScoresCount(long scorecardId) {
        int total = 0;

        String query = "SELECT COUNT(*) FROM " + OtashuDatabaseHelper.TABLE_APPRENTICE_SCORES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_SCORECARD_ID + "=?"
                + " GROUP BY " + OtashuDatabaseHelper.COLUMN_QUESTION_NUMBER;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(scorecardId)
        });
        total = cursor.getCount();

        db.close();

        return total;
    }

    /**
     * Get all ApprenticeScores for given ApprenticeScorecard
     * 
     * @return List of ApprenticeScores.
     */
    public List<ApprenticeScore> getApprenticeScoresByScorecard(long scorecardId) {
        List<ApprenticeScore> apprenticeScores = new ArrayList<ApprenticeScore>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_APPRENTICE_SCORES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_SCORECARD_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(scorecardId)
        });

        ApprenticeScore apprenticeScore = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                apprenticeScore = new ApprenticeScore();
                apprenticeScore.setId(cursor.getLong(0));
                apprenticeScore.setScorecardId(cursor.getLong(1));
                apprenticeScore.setQuestionNumber(cursor.getInt(2));
                apprenticeScore.setCorrect(cursor.getInt(3));
                apprenticeScore.setNotevalue(cursor.getLong(4));

                // add note string to list of strings
                apprenticeScores.add(apprenticeScore);
            } while (cursor.moveToNext());
        }

        db.close();

        return apprenticeScores;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
