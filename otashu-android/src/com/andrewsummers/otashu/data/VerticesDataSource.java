
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.model.Vertex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class VerticesDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_GRAPH_ID,
            OtashuDatabaseHelper.COLUMN_NODE,
    };

    /**
     * VerticesDataSource constructor.
     * 
     * @param context Current state.
     */
    public VerticesDataSource(Context context) {
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
     * Create vertex row in database.
     * 
     * @param vertexvalues String of vertex values to insert.
     * @return Vertex of newly-created vertex data.
     */
    public Vertex createVertex(Vertex vertex) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_GRAPH_ID, vertex.getGraphId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NODE, vertex.getNode());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(OtashuDatabaseHelper.TABLE_VERTICES, null,
                contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_VERTICES, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Vertex newVertex = cursorToVertex(cursor);
        cursor.close();
        return newVertex;
    }

    /**
     * Delete vertex row from database.
     * 
     * @param vertex Vertex to delete.
     */
    public void deleteVertex(Vertex vertex) {
        long id = vertex.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete vertex
        db.delete(OtashuDatabaseHelper.TABLE_VERTICES,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Get all vertices from database table.
     * 
     * @return List of Vertices.
     */
    public List<Vertex> getAllVertices() {
        List<Vertex> vertices = new ArrayList<Vertex>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_VERTICES;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Vertex vertex = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                vertex = new Vertex();
                vertex.setId(cursor.getLong(0));
                vertex.setGraphId(cursor.getLong(1));
                vertex.setNode(cursor.getInt(2));

                // add note string to list of strings
                vertices.add(vertex);
            } while (cursor.moveToNext());
        }

        return vertices;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Vertex
     */
    private Vertex cursorToVertex(Cursor cursor) {
        Vertex vertex = new Vertex();
        vertex.setId(cursor.getLong(0));
        vertex.setGraphId(cursor.getLong(1));
        vertex.setNode(cursor.getInt(2));
        return vertex;
    }

    public Vertex getVertex(long graphId, int vertexNodeId) {
        Vertex vertex = new Vertex();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_VERTICES + " WHERE "
                + OtashuDatabaseHelper.COLUMN_GRAPH_ID + "=" + graphId + " AND "
                + OtashuDatabaseHelper.COLUMN_NODE + "=" + vertexNodeId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all vertices from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create vertex objects based on vertex data from database
                vertex = new Vertex();
                vertex.setId(cursor.getLong(0));
                vertex.setGraphId(cursor.getLong(1));
                vertex.setNode(cursor.getInt(2));
            } while (cursor.moveToNext());
        }

        return vertex;
    }

    public Vertex updateVertex(Vertex vertex) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, vertex.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_GRAPH_ID, vertex.getGraphId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NODE, vertex.getNode());

        db.update(OtashuDatabaseHelper.TABLE_VERTICES, contentValues,
                OtashuDatabaseHelper.COLUMN_ID
                        + "=" + vertex.getId(), null);

        return vertex;
    }

    public SQLiteDatabase getDatabase() {
        return database;
    }

    public void setDatabase(SQLiteDatabase database) {
        this.database = database;
    }
}
