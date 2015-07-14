
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.andrewsummers.otashu.model.Label;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class LabelsDataSource {
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_NAME,
            OtashuDatabaseHelper.COLUMN_COLOR
    };

    /**
     * LabelsDataSource constructor.
     * 
     * @param context Current state.
     */
    public LabelsDataSource(Context context) {
        dbHelper = new OtashuDatabaseHelper(context);
    }

    /**
     * LabelsDataSource constructor.
     * 
     * @param context Current state.
     * @param databaseName Database to use.
     */
    public LabelsDataSource(Context context, String databaseName) {
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
     * Create label row in database.
     * 
     * @param labelvalues String of label values to insert.
     * @return Label of newly-created label data.
     */
    public Label createLabel(Label label) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, label.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_COLOR, label.getColor());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(OtashuDatabaseHelper.TABLE_LABELS, null,
                contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_LABELS, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Label newLabel = cursorToLabel(cursor);
        cursor.close();
        db.close();

        return newLabel;
    }

    /**
     * Delete label row from database.
     * 
     * @param label Label to delete.
     */
    public void deleteLabel(Label label) {
        long id = label.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete label
        db.delete(OtashuDatabaseHelper.TABLE_LABELS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);

        db.close();
    }

    /**
     * Get all labels from database table.
     * 
     * @return List of Labels.
     */
    public List<Label> getAllLabels() {
        List<Label> labels = new ArrayList<Label>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_LABELS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Label label = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                label = new Label();
                label.setId(Integer.parseInt(cursor.getString(0)));
                label.setName(cursor.getString(1));
                label.setColor(cursor.getString(2));

                // add note string to list of strings
                labels.add(label);
            } while (cursor.moveToNext());
        }

        db.close();

        return labels;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Label
     */
    private Label cursorToLabel(Cursor cursor) {
        Label label = new Label();
        label.setId(cursor.getLong(0));
        label.setName(cursor.getString(1));
        label.setColor(cursor.getString(2));
        return label;
    }

    /**
     * getAllLabels gets a preview list of all labels.
     * 
     * @return List of Label preview strings.
     */
    public List<String> getAllLabelListPreviews() {
        List<String> labels = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_LABELS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all labels from database
        Cursor cursor = db.rawQuery(query, null);

        Label label = null;
        if (cursor.moveToFirst()) {
            do {
                // create label objects based on label data from database
                label = new Label();
                label.setId(Integer.parseInt(cursor.getString(0)));
                label.setName(cursor.getString(1));
                label.setColor(cursor.getString(2));

                // add label string to list of strings
                labels.add(label.toString());
            } while (cursor.moveToNext());
        }

        db.close();

        return labels;
    }

    /**
     * Get a list of all labels ids.
     * 
     * @return List of Label ids.
     */
    public List<Long> getAllLabelListDBTableIds() {
        List<Long> labels = new LinkedList<Long>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_LABELS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all labels from database
        Cursor cursor = db.rawQuery(query, null);

        Label label = null;
        if (cursor.moveToFirst()) {
            do {
                // create label objects based on label data from database
                label = new Label();
                label.setId(Long.parseLong(cursor.getString(0)));

                // add label to labels list
                labels.add(label.getId());
            } while (cursor.moveToNext());
        }

        db.close();

        return labels;
    }

    public Label getLabel(long labelId) {
        Label label = new Label();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_LABELS + " WHERE "
                + OtashuDatabaseHelper.COLUMN_ID + "=?";

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all labels from database
        Cursor cursor = db.rawQuery(query, new String[] {
                String.valueOf(labelId)
        });

        if (cursor.moveToFirst()) {
            do {
                // create label objects based on label data from database
                label = new Label();
                label.setId(Integer.parseInt(cursor.getString(0)));
                label.setName(cursor.getString(1));
                label.setColor(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        db.close();

        return label;
    }

    public Label updateLabel(Label label) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, label.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, label.getName());
        contentValues.put(OtashuDatabaseHelper.COLUMN_COLOR, label.getColor());

        db.update(OtashuDatabaseHelper.TABLE_LABELS, contentValues, OtashuDatabaseHelper.COLUMN_ID
                + "=" + label.getId(), null);

        db.close();

        return label;
    }
}
