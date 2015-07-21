
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.model.Path;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class PathsDataSource {
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_FROM_NODE_ID,
            OtashuDatabaseHelper.COLUMN_TO_NODE_ID,
            OtashuDatabaseHelper.COLUMN_APPRENTICE_ID,
            OtashuDatabaseHelper.COLUMN_EMOTION_ID,
            OtashuDatabaseHelper.COLUMN_POSITION,
            OtashuDatabaseHelper.COLUMN_RANK,
    };

    /**
     * PathsDataSource constructor.
     * 
     * @param context Current state.
     */
    public PathsDataSource(Context context) {
        dbHelper = new OtashuDatabaseHelper(context);
    }

    /**
     * PathsDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public PathsDataSource(Context context, String databaseName) {
        dbHelper = new OtashuDatabaseHelper(context, databaseName);
    }

    /**
     * Open database.
     * 
     * @throws SQLException
     */
    public void open() throws SQLException {
        dbHelper.getWritableDatabase();
    }

    /**
     * Close database.
     */
    public void close() {
        dbHelper.close();
    }

    /**
     * Create path row in database.
     * 
     * @param pathvalues String of path values to insert.
     * @return Path of newly-created path data.
     */
    public Path createPath(Path path) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_FROM_NODE_ID, path.getFromNodeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_TO_NODE_ID, path.getToNodeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, path.getApprenticeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, path.getEmotionId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_POSITION, path.getPosition());
        contentValues.put(OtashuDatabaseHelper.COLUMN_RANK, path.getRank());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(OtashuDatabaseHelper.TABLE_PATHS, null,
                contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_PATHS, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Path newPath = cursorToPath(cursor);
        cursor.close();
        db.close();

        return newPath;
    }

    /**
     * Delete path row from database.
     * 
     * @param path Path to delete.
     */
    public void deletePath(Path path) {
        long id = path.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete path
        db.delete(OtashuDatabaseHelper.TABLE_PATHS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);

        db.close();
    }

    /**
     * Get all paths from database table.
     * 
     * @return List of Paths.
     */
    public List<Path> getAllPaths() {
        List<Path> paths = new ArrayList<Path>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_PATHS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Path path = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                path = new Path();
                path.setId(cursor.getLong(0));
                path.setFromNodeId(cursor.getInt(1));
                path.setToNodeId(cursor.getInt(2));
                path.setApprenticeId(cursor.getLong(3));
                path.setEmotionId(cursor.getLong(4));
                path.setPosition(cursor.getInt(5));
                path.setRank(cursor.getInt(6));

                // add note string to list of strings
                paths.add(path);
            } while (cursor.moveToNext());
        }

        db.close();

        return paths;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Path
     */
    private Path cursorToPath(Cursor cursor) {
        Path path = new Path();
        path.setId(cursor.getLong(0));
        path.setFromNodeId(cursor.getInt(1));
        path.setToNodeId(cursor.getInt(2));
        path.setApprenticeId(cursor.getLong(3));
        path.setEmotionId(cursor.getLong(4));
        path.setPosition(cursor.getInt(5));
        path.setRank(cursor.getInt(6));
        return path;
    }

    /**
     * getAllPaths gets a preview list of all paths.
     * 
     * @return List of Path preview strings.
     */
    public List<String> getAllPathListPreviews() {
        List<String> paths = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_PATHS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all paths from database
        Cursor cursor = db.rawQuery(query, null);

        Path path = null;
        if (cursor.moveToFirst()) {
            do {
                // create path objects based on path data from database
                path = new Path();
                path.setId(cursor.getLong(0));
                path.setFromNodeId(cursor.getInt(1));
                path.setToNodeId(cursor.getInt(2));
                path.setApprenticeId(cursor.getLong(3));
                path.setEmotionId(cursor.getLong(4));
                path.setPosition(cursor.getInt(5));
                path.setRank(cursor.getInt(6));

                // add path string to list of strings
                paths.add(path.toString());
            } while (cursor.moveToNext());
        }

        db.close();

        return paths;
    }

    /**
     * Get a list of all paths ids.
     * 
     * @return List of Path ids.
     */
    public List<Long> getAllPathListDBTableIds() {
        List<Long> paths = new LinkedList<Long>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_PATHS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all paths from database
        Cursor cursor = db.rawQuery(query, null);

        Path path = null;
        if (cursor.moveToFirst()) {
            do {
                // create path objects based on path data from database
                path = new Path();
                path.setId(cursor.getLong(0));

                // add path to paths list
                paths.add(path.getId());
            } while (cursor.moveToNext());
        }

        db.close();

        return paths;
    }

    public Path getPath(long pathId) {
        Path path = new Path();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_PATHS + " WHERE "
                + OtashuDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all paths from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(pathId)
        });

        if (cursor.moveToFirst()) {
            do {
                // create path objects based on path data from database
                path = new Path();
                path.setId(cursor.getLong(0));
                path.setFromNodeId(cursor.getInt(1));
                path.setToNodeId(cursor.getInt(2));
                path.setApprenticeId(cursor.getLong(3));
                path.setEmotionId(cursor.getLong(4));
                path.setPosition(cursor.getInt(5));
                path.setRank(cursor.getInt(6));
            } while (cursor.moveToNext());
        }

        db.close();

        return path;
    }

    public Path updatePath(Path path) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, path.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_FROM_NODE_ID, path.getFromNodeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_TO_NODE_ID, path.getToNodeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, path.getApprenticeId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_EMOTION_ID, path.getEmotionId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_POSITION, path.getPosition());
        contentValues.put(OtashuDatabaseHelper.COLUMN_RANK, path.getRank());

        db.update(OtashuDatabaseHelper.TABLE_PATHS, contentValues, OtashuDatabaseHelper.COLUMN_ID
                + "=" + path.getId(), null);

        db.close();

        return path;
    }
}
