package com.andrewsummers.otashu.activity;

import java.util.Calendar;
import java.util.Date;

import com.andrewsummers.otashu.ImageAdapter;
import com.andrewsummers.otashu.OtashuReceiver;
import com.andrewsummers.otashu.R;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.GridView;

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

        // TODO: move this into a setting or something
        
        // load preferences
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean alarmEnabled = sharedPref.getBoolean("pref_alarm_enabled", false);
        
        // set alarm, if enabled
        if (alarmEnabled) {
            AlarmManager am = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Calendar alarmDate = Calendar.getInstance();
            //alarmDate.add(Calendar.SECOND, 10);
            alarmDate.add(Calendar.HOUR, 8);

            Intent intent = new Intent(this, OtashuReceiver.class);
            PendingIntent sender = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            
            am.set(AlarmManager.RTC_WAKEUP, alarmDate.getTimeInMillis(), sender);
            Log.d("MYLOG", "alarm set: " + alarmDate.getTimeInMillis());
        }
        
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
                    break;
                case 1:
                    intent = new Intent(MainActivity.this, ChooseEmotionActivity.class);
                    break;
                case 2:
                    intent = new Intent(MainActivity.this, ViewAllEmotionsActivity.class);
                    break;
                case 3:
                    intent = new Intent(MainActivity.this, ApprenticeActivity.class);
                    break;
                }
                
                startActivity(intent);
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
            break;
        case R.id.view_labels:
            intent = new Intent(this, ViewAllLabelsActivity.class);
            break;
        case R.id.export_database:
            intent = new Intent(this, ExportDatabaseActivity.class);
            break;
        case R.id.import_database:
            intent = new Intent(this, ImportDatabaseActivity.class);
            break;
        case R.id.database_dumper:
            intent = new Intent(this, DatabaseDumperActivity.class);
            break;
        case R.id.view_bookmarks:
            intent = new Intent(this, ViewAllBookmarksActivity.class);
            break;
        case R.id.view_notevalues:
            intent = new Intent(this, ViewAllNotevaluesActivity.class);
            break;
        }
        
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }
}