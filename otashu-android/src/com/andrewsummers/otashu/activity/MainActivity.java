package com.andrewsummers.otashu.activity;

import com.andrewsummers.otashu.ImageAdapter;
import com.andrewsummers.otashu.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

/**
 * MainActivity currently acts as a general menu in order to demo various
 * functionality available in this application.
 */
public class MainActivity extends Activity implements OnClickListener {

    /**
     * onCreate override that provides menu buttons on menu view.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set default preferences
        PreferenceManager.setDefaultValues(this, R.xml.preferences, false);
        
        // get specific layout for content view
        setContentView(R.layout.activity_main);
        
        GridView gridview = (GridView) findViewById(R.id.gridview);
        gridview.setAdapter(new ImageAdapter(this));
        
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = null;
                
                switch (position) {
                case 0:
                    intent = new Intent(MainActivity.this, ViewAllNotesetsActivity.class);
                    startActivity(intent);
                    break;
                case 1:
                    intent = new Intent(MainActivity.this, ChooseEmotionActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(MainActivity.this, ViewAllEmotionsActivity.class);
                    startActivity(intent);
                    break;
                case 3:
                    intent = new Intent(MainActivity.this, ApprenticeActivity.class);
                    startActivity(intent);
                    break;
                }                
            }
        });
    }

    /**
     * onClick override that acts as a router to start desired activities.
     * 
     * @param view
     *            Incoming view.
     */
    @Override
    public void onClick(View v) {        
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        
        // handle menu item selection
        switch (item.getItemId()) {
        case R.id.view_settings:
            intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        case R.id.export_database:
            intent = new Intent(this, ExportDatabaseActivity.class);
            startActivity(intent);
            return true;
        case R.id.import_database:
            intent = new Intent(this, ImportDatabaseActivity.class);
            startActivity(intent);
            return true;
        case R.id.database_dumper:
            intent = new Intent(this, DatabaseDumperActivity.class);
            startActivity(intent);
            return true;
        default:
            return super.onOptionsItemSelected(item);
        }
    }
}