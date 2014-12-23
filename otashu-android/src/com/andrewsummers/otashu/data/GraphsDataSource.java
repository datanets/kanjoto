
package com.andrewsummers.otashu.data;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import com.andrewsummers.otashu.model.Graph;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class GraphsDataSource {
    private SQLiteDatabase database;
    private OtashuDatabaseHelper dbHelper;

    // database table columns
    private String[] allColumns = {
            OtashuDatabaseHelper.COLUMN_ID,
            OtashuDatabaseHelper.COLUMN_NAME,
    };

    /**
     * GraphsDataSource constructor.
     * 
     * @param context Current state.
     */
    public GraphsDataSource(Context context) {
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
     * Create graph row in database.
     * 
     * @param graphvalues String of graph values to insert.
     * @return Graph of newly-created graph data.
     */
    public Graph createGraph(Graph graph) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, graph.getName());

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        long insertId = db.insert(OtashuDatabaseHelper.TABLE_GRAPHS, null,
                contentValues);

        Cursor cursor = db.query(
                OtashuDatabaseHelper.TABLE_GRAPHS, allColumns,
                OtashuDatabaseHelper.COLUMN_ID + " = " + insertId, null,
                null, null, null);

        cursor.moveToFirst();
        Graph newGraph = cursorToGraph(cursor);
        cursor.close();
        return newGraph;
    }

    /**
     * Delete graph row from database.
     * 
     * @param graph Graph to delete.
     */
    public void deleteGraph(Graph graph) {
        long id = graph.getId();

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // delete graph
        db.delete(OtashuDatabaseHelper.TABLE_GRAPHS,
                OtashuDatabaseHelper.COLUMN_ID + " = " + id, null);
    }

    /**
     * Get all graphs from database table.
     * 
     * @return List of Graphs.
     */
    public List<Graph> getAllGraphs() {
        List<Graph> graphs = new ArrayList<Graph>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_GRAPHS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all notes from database
        Cursor cursor = db.rawQuery(query, null);

        Graph graph = null;
        if (cursor.moveToFirst()) {
            do {
                // create note objects based on note data from database
                graph = new Graph();
                graph.setId(cursor.getLong(0));
                graph.setName(cursor.getString(1));

                // add note string to list of strings
                graphs.add(graph);
            } while (cursor.moveToNext());
        }

        return graphs;
    }

    /**
     * Get all graph ids from database table.
     * 
     * @return List of Graphs ids.
     */
    public List<Integer> getAllGraphIds() {
        List<Integer> graph_ids = new ArrayList<Integer>();

        Cursor cursor = database.query(
                OtashuDatabaseHelper.TABLE_GRAPHS, allColumns, null,
                null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Graph graph = cursorToGraph(cursor);
            graph_ids.add((int) graph.getId());
            cursor.moveToNext();
        }

        cursor.close();
        return graph_ids;
    }

    /**
     * Access column data at current position of result.
     * 
     * @param cursor Current cursor location.
     * @return Graph
     */
    private Graph cursorToGraph(Cursor cursor) {
        Graph graph = new Graph();
        graph.setId(cursor.getLong(0));
        graph.setName(cursor.getString(1));
        return graph;
    }

    /**
     * getAllGraphs gets a preview list of all graphs.
     * 
     * @return List of Graph preview strings.
     */
    public List<String> getAllGraphListPreviews() {
        List<String> graphs = new LinkedList<String>();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_GRAPHS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all graphs from database
        Cursor cursor = db.rawQuery(query, null);

        Graph graph = null;
        if (cursor.moveToFirst()) {
            do {
                // create graph objects based on graph data from database
                graph = new Graph();
                graph.setId(cursor.getLong(0));
                graph.setName(cursor.getString(1));

                // add graph string to list of strings
                graphs.add(graph.toString());
            } while (cursor.moveToNext());
        }

        return graphs;
    }

    /**
     * Get a list of all graphs ids.
     * 
     * @return List of Graph ids.
     */
    public List<Long> getAllGraphListDBTableIds() {
        List<Long> graphs = new LinkedList<Long>();

        String query = "SELECT " + OtashuDatabaseHelper.COLUMN_ID + " FROM "
                + OtashuDatabaseHelper.TABLE_GRAPHS;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all graphs from database
        Cursor cursor = db.rawQuery(query, null);

        Graph graph = null;
        if (cursor.moveToFirst()) {
            do {
                // create graph objects based on graph data from database
                graph = new Graph();
                graph.setId(Long.parseLong(cursor.getString(0)));

                // add graph to graphs list
                graphs.add(graph.getId());
            } while (cursor.moveToNext());
        }

        return graphs;
    }

    public Graph getGraph(long graphId) {
        Graph graph = new Graph();

        String query = "SELECT * FROM " + OtashuDatabaseHelper.TABLE_GRAPHS + " WHERE "
                + OtashuDatabaseHelper.COLUMN_ID + "=" + graphId;

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // select all graphs from database
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                // create graph objects based on graph data from database
                graph = new Graph();
                graph.setId(cursor.getLong(0));
                graph.setName(cursor.getString(1));
            } while (cursor.moveToNext());
        }

        return graph;
    }

    public Graph updateGraph(Graph graph) {

        // create database handle
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(OtashuDatabaseHelper.COLUMN_ID, graph.getId());
        contentValues.put(OtashuDatabaseHelper.COLUMN_NAME, graph.getName());

        db.update(OtashuDatabaseHelper.TABLE_GRAPHS, contentValues, OtashuDatabaseHelper.COLUMN_ID
                + "=" + graph.getId(), null);

        return graph;
    }

    public Graph getRandomGraph() {
        Graph graph = new Graph();

        // get all graphs first
        List<Graph> allGraphs = getAllGraphs();

        // choose random graph
        int chosenIndex = new Random().nextInt(allGraphs.size());

        graph = allGraphs.get(chosenIndex);

        return graph;
    }
}
