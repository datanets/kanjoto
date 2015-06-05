
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.model.Achievement;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class AchievementsDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_NAME,
            OtashuDatabaseHelper.COLUMN_APPRENTICE_ID,
            OtashuDatabaseHelper.COLUMN_EARNED_ON,
            OtashuDatabaseHelper.COLUMN_KEY,
    };

    /**
     * AchievementsDataSource constructor.
     * 
     * @param context Current state.
     */
    public AchievementsDataSource(Context context) {
        dbHelper = new OtashuDatabaseHelper(context);
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
     * Create achievement row in database.
     * 
     * @param achievementvalues String of achievement values to insert.
     * @return Achievement of newly-created achievement data.
     */
    public Achievement createAchievement(Achievement achievement) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, achievement.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, achievement.getApprenticeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_EARNED_ON, achievement.getEarnedOn());
        contentValues.put(OtashuDatabaseHelper.COLUMN_KEY, achievement.getKey());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(OtashuDatabaseHelper.TABLE_ACHIEVEMENTS, null,
                contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_ACHIEVEMENTS, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Achievement newAchievement = cursorToAchievement(cursor);
        cursor.close();
        return newAchievement;
    }

    /**
     * Delete achievement row from database.
     * 
     * @param achievement Achievement to delete.
     */
    public void deleteAchievement(Achievement achievement) {
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete noteset
        db.delete(OtashuDatabaseHelper.TABLE_ACHIEVEMENTS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + achievement.getId(), null);
    }

    /**
     * Get all achievements from database table.
     * 
     * @return List of Achievements.
     */
    public List<Achievement> getAllAchievements(long apprenticeId) {
        List<Achievement> achievements = new ArrayList<Achievement>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_ACHIEVEMENTS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId)
        });

        Achievement achievement = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                achievement = new Achievement();
                achievement.setId(cursor.getLong(0));
                achievement.setName(cursor.getString(1));
                achievement.setApprenticeId(cursor.getLong(2));
                achievement.setEarnedOn(cursor.getString(3));
                achievement.setKey(cursor.getString(4));

                // add note string to list of strings
                achievements.add(achievement);
            } while (cursor.moveToNext());
        }

        return achievements;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Achievement
     */
    private Achievement cursorToAchievement(Cursor cursor) {
        Achievement achievement = new Achievement();
        achievement.setId(cursor.getLong(0));
        achievement.setName(cursor.getString(1));
        achievement.setApprenticeId(cursor.getLong(2));
        achievement.setEarnedOn(cursor.getString(3));
        achievement.setKey(cursor.getString(4));
        return achievement;
    }

    public Achievement getAchievement(long achievementId) {
        Achievement achievement = new Achievement();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_ACHIEVEMENTS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all achievements from database
        Cursor cursor = db.rawQuery(query, new String[] {
            String.valueOf(achievementId)
        });

        if (cursor.moveToFirst()) {
            do {
                // create achievement objects based on achievement data from database
                achievement = new Achievement();
                achievement.setId(cursor.getLong(0));
                achievement.setName(cursor.getString(1));
                achievement.setApprenticeId(cursor.getLong(2));
                achievement.setEarnedOn(cursor.getString(3));
                achievement.setKey(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        db.close();

        return achievement;
    }

    public Achievement getAchievementByKey(String key) {
        Achievement achievement = new Achievement();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_ACHIEVEMENTS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_KEY + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all achievements from database
        Cursor cursor = db.rawQuery(query, new String[] {
            key
        });

        if (cursor.moveToFirst()) {
            do {
                // create achievement objects based on achievement data from database
                achievement = new Achievement();
                achievement.setId(cursor.getLong(0));
                achievement.setName(cursor.getString(1));
                achievement.setApprenticeId(cursor.getLong(2));
                achievement.setEarnedOn(cursor.getString(3));
                achievement.setKey(cursor.getString(4));
            } while (cursor.moveToNext());
        }

        db.close();

        return achievement;
    }

    public int getAchievementCount(long apprenticeId, String achievementName) {
        int result = 0;

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_ACHIEVEMENTS
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=?"
                + " AND " + OtashuDatabaseHelper.COLUMN_NAME + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all achievements from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(apprenticeId), achievementName
        });

        if (cursor.moveToFirst()) {
            result = cursor.getCount();
        }

        db.close();

        return result;
    }

    public Achievement updateAchievement(Achievement achievement) {
        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, achievement.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, achievement.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, achievement.getApprenticeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_EARNED_ON, achievement.getEarnedOn());
        contentValues.put(OtashuDatabaseHelper.COLUMN_KEY, achievement.getKey());

        db.update(OtashuDatabaseHelper.TABLE_ACHIEVEMENTS, contentValues,
                OtashuDatabaseHelper.COLUMN_ID + "=" + achievement.getId(), null);

        return achievement;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
