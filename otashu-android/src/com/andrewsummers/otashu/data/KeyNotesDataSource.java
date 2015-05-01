
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.andrewsummers.otashu.model.KeyNote;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class KeyNotesDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_KEY_SIGNATURE_ID,
            OtashuDatabaseHelper.COLUMN_NOTEVALUE,
            OtashuDatabaseHelper.COLUMN_WEIGHT,
            OtashuDatabaseHelper.COLUMN_APPRENTICE_ID,
    };

    /**
     * KeyNotesDataSource constructor.
     * 
     * @param context Current state.
     */
    public KeyNotesDataSource(Context context) {
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
     * Create keyNote row in database.
     * 
     * @param keyNotevalues String of keyNote values to insert.
     * @return KeyNote of newly-created keyNote data.
     */
    public KeyNote createKeyNote(KeyNote keyNote) {
        ContentValues contentValues = new ContentValues();
        contentValues
                .put(OtashuDatabaseHelper.COLUMN_KEY_SIGNATURE_ID, keyNote.getKeySignatureId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NOTEVALUE, keyNote.getNotevalue());
        contentValues.put(OtashuDatabaseHelper.COLUMN_WEIGHT, keyNote.getWeight());
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, keyNote.getApprenticeId());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db
                .insert(OtashuDatabaseHelper.TABLE_KEY_NOTES, null,
                        contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_KEY_NOTES, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();

        KeyNote newKeyNote = cursorToKeyNote(cursor);
        cursor.close();
        return newKeyNote;
    }

    /**
     * Delete keyNote row from database.
     * 
     * @param keyNote KeyNote to delete.
     */
    public void deleteKeyNote(KeyNote keyNote) {
        long id = keyNote.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete keyNote
        db.delete(OtashuDatabaseHelper.TABLE_KEY_NOTES,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Get all keyNotes from database table.
     * 
     * @return List of KeyNotes.
     */
    public List<KeyNote> getAllKeyNotes(long apprenticeId) {
        List<KeyNote> keyNotes = new ArrayList<KeyNote>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_NOTES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        KeyNote keyNote = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                keyNote = new KeyNote();
                keyNote.setId(cursor.getLong(0));
                keyNote.setKeySignatureId(cursor.getLong(1));
                keyNote.setNotevalue(cursor.getInt(2));
                keyNote.setWeight(cursor.getFloat(3));
                keyNote.setApprenticeId(cursor.getLong(4));

                // add note string to list of strings
                keyNotes.add(keyNote);
            } while (cursor.moveToNext());
        }

        return keyNotes;
    }

    /**
     * Get all keyNote ids from database table.
     * 
     * @return List of KeyNotes ids.
     */
    public List<Integer> getAllKeyNoteIds(long apprenticeId) {
        List<Integer> keyNote_ids = new ArrayList<Integer>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_NOTES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            KeyNote keyNote = cursorToKeyNote(cursor);
            keyNote_ids.add((int) keyNote.getId());
            cursor.moveToNext();
        }

        cursor.close();
        return keyNote_ids;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return KeyNote
     */
    private KeyNote cursorToKeyNote(Cursor cursor) {
        KeyNote keyNote = new KeyNote();
        keyNote.setId(cursor.getLong(0));
        keyNote.setKeySignatureId(cursor.getLong(1));
        keyNote.setNotevalue(cursor.getInt(2));
        keyNote.setWeight(cursor.getFloat(3));
        keyNote.setApprenticeId(cursor.getLong(4));
        return keyNote;
    }

    /**
     * getAllKeyNotes gets a preview list of all keyNotes.
     * 
     * @return List of KeyNote preview strings.
     */
    public List<String> getAllKeyNoteListPreviews(long apprenticeId) {
        List<String> keyNotes = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_NOTES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keyNotes from database
        Cursor cursor = db.rawQuery(query, null);

        KeyNote keyNote = null;
        if (cursor.moveToFirst()) {
            do {
                // create keyNote objects based on keyNote data from database
                keyNote = new KeyNote();
                keyNote.setId(cursor.getLong(0));
                keyNote.setKeySignatureId(cursor.getLong(1));
                keyNote.setNotevalue(cursor.getInt(2));
                keyNote.setWeight(cursor.getFloat(3));
                keyNote.setApprenticeId(cursor.getLong(4));

                // add keyNote string to list of strings
                keyNotes.add(keyNote.toString());
            } while (cursor.moveToNext());
        }

        return keyNotes;
    }

    /**
     * Get a list of all keyNotes ids.
     * 
     * @return List of KeyNote ids.
     */
    public List<Long> getAllKeyNoteListDBTableIds(long apprenticeId) {
        List<Long> keyNotes = new LinkedList<Long>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_KEY_NOTES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keyNotes from database
        Cursor cursor = db.rawQuery(query, null);

        KeyNote keyNote = null;
        if (cursor.moveToFirst()) {
            do {
                // create keyNote objects based on keyNote data from database
                keyNote = new KeyNote();
                keyNote.setId(cursor.getLong(0));

                // add keyNote to keyNotes list
                keyNotes.add(keyNote.getId());
            } while (cursor.moveToNext());
        }

        return keyNotes;
    }

    public KeyNote getKeyNote(long apprenticeId, long keyNoteId) {
        KeyNote keyNote = new KeyNote();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_NOTES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId
                + " AND " + OtashuDatabaseHelper.COLUMN_ID + "=" + keyNoteId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keyNotes from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create keyNote objects based on keyNote data from database
                keyNote = new KeyNote();
                keyNote.setId(cursor.getLong(0));
                keyNote.setKeySignatureId(cursor.getLong(1));
                keyNote.setNotevalue(cursor.getInt(2));
                keyNote.setWeight(cursor.getFloat(3));
                keyNote.setApprenticeId(cursor.getLong(4));
            } while (cursor.moveToNext());
        }

        return keyNote;
    }

    public List<KeyNote> getKeyNotesByKeySignature(long apprenticeId, long keySignatureId) {
        List<KeyNote> keyNotes = new ArrayList<KeyNote>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_NOTES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId
                + " AND " + OtashuDatabaseHelper.COLUMN_KEY_SIGNATURE_ID + "=" + keySignatureId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keyNotes from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create keyNote objects based on keyNote data from database
                KeyNote keyNote = new KeyNote();
                keyNote.setId(cursor.getLong(0));
                keyNote.setKeySignatureId(cursor.getLong(1));
                keyNote.setNotevalue(cursor.getInt(2));
                keyNote.setWeight(cursor.getFloat(3));
                keyNote.setApprenticeId(cursor.getLong(4));
                keyNotes.add(keyNote);
            } while (cursor.moveToNext());
        }

        return keyNotes;
    }

    public List<Integer> getKeyNoteNotevaluesByKeySignature(long apprenticeId, long keySignatureId) {
        List<Integer> keyNotes = new ArrayList<Integer>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_KEY_NOTES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId
                + " AND " + OtashuDatabaseHelper.COLUMN_KEY_SIGNATURE_ID + "=" + keySignatureId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keyNotes from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                keyNotes.add(cursor.getInt(2));
            } while (cursor.moveToNext());
        }

        return keyNotes;
    }

    public KeyNote updateKeyNote(KeyNote keyNote) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, keyNote.getId());
        contentValues
                .put(OtashuDatabaseHelper.COLUMN_KEY_SIGNATURE_ID, keyNote.getKeySignatureId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NOTEVALUE, keyNote.getNotevalue());
        contentValues.put(OtashuDatabaseHelper.COLUMN_WEIGHT, keyNote.getWeight());
        contentValues.put(OtashuDatabaseHelper.COLUMN_APPRENTICE_ID, keyNote.getApprenticeId());

        db.update(OtashuDatabaseHelper.TABLE_KEY_NOTES, contentValues,
                OtashuDatabaseHelper.COLUMN_ID + "=" + keyNote.getId(), null);

        return keyNote;
    }

    public KeyNote getRandomKeyNote(long apprenticeId) {
        KeyNote keyNote = new KeyNote();

        // get all keyNotes first
        List<KeyNote> allKeyNotes = getAllKeyNotes(apprenticeId);

        if (allKeyNotes.size() > 0) {
            // choose random keyNote
            int chosenIndex = new Random().nextInt(allKeyNotes.size());
            keyNote = allKeyNotes.get(chosenIndex);
        }

        return keyNote;
    }

    public List<Long> keySignatureIdsThatContain(long apprenticeId, int notevalue) {
        List<Long> keySignatureIds = new ArrayList<Long>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_KEY_NOTES
                + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId
                + " AND " + OtashuDatabaseHelper.COLUMN_NOTEVALUE + "=" + notevalue;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all keyNotes from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // add keyNote to keyNotes list
                keySignatureIds.add(cursor.getLong(0));
            } while (cursor.moveToNext());
        }

        return keySignatureIds;
    }

    public long getKeySignatureByNotes(long apprenticeId, List<Integer> notevaluesInKeySignature) {
        long keySignatureId = 1;
        // List<Long> foundKeySignatureIds = new ArrayList<Long>();
        HashMap<Long, Integer> foundKeySignatureIds = new HashMap<Long, Integer>();

        // loop through each notevalue from input
        // and check to see if we've found a key signature from database
        for (int i = 0; i < notevaluesInKeySignature.size(); i++) {
            String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + ", "
                    + OtashuDatabaseHelper.COLUMN_KEY_SIGNATURE_ID + " FROM "
                    + OtashuDatabaseHelper.TABLE_KEY_NOTES
                    + " WHERE " + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "=" + apprenticeId
                    + " AND " + OtashuDatabaseHelper.COLUMN_NOTEVALUE + "="
                    + notevaluesInKeySignature.get(i);

            // create database handle
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            // select all keyNotes from database
            Cursor cursor = db.rawQuery(query, null);

            if (cursor.moveToFirst()) {
                do {
                    // add keyNote to keyNotes list
                    long knid = cursor.getLong(1);
                    // check if key already exists
                    if (foundKeySignatureIds.containsKey(knid)) {
                        int value = foundKeySignatureIds.get(knid);
                        value++;
                        foundKeySignatureIds.put(knid, value);
                    } else {
                        // add new key
                        foundKeySignatureIds.put(knid, 1);
                    }
                } while (cursor.moveToNext());
            }
        }

        boolean foundBestMatch = false;

        if (foundKeySignatureIds.size() > 0) {
            for (int i = 4; i > 0; i--) {
                Iterator<Map.Entry<Long, Integer>> itr = foundKeySignatureIds.entrySet().iterator();
                while (itr.hasNext() && !foundBestMatch) {
                    Map.Entry<Long, Integer> kvpair = itr.next();
                    if (kvpair.getValue() == i) {
                        foundBestMatch = true;
                        keySignatureId = kvpair.getKey();
                    }
                }
            }
        }

        return keySignatureId;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
