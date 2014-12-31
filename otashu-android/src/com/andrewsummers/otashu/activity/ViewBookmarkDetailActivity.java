
package com.andrewsummers.otashu.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.BookmarksDataSource;
import com.andrewsummers.otashu.model.Bookmark;
import com.andrewsummers.otashu.model.Note;

import android.app.Activity;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * View details of a particular bookmark.
 */
public class ViewBookmarkDetailActivity extends Activity implements OnClickListener {

    private int bookmarkId = 0;
    private String currentBookmarkSerializedValue = "";
    private Button buttonPlayBookmark = null;
    private File path = Environment.getExternalStorageDirectory();
    private String externalDirectory = path.toString() + "/otashu/";
    private File musicSource = new File(externalDirectory + "otashu_bookmark.mid");
    private static MediaPlayer mediaPlayer;

    /**
     * onCreate override used to get details view.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_view_bookmark_detail);

        Log.d("MYLOG", "got list item id: " + getIntent().getExtras().getLong("list_id"));
        bookmarkId = (int) getIntent().getExtras().getLong("list_id");

        /*
         * // prevent crashes due to lack of database data if (allBookmarksData.isEmpty())
         * allBookmarksData.add((long) 0);
         */

        Bookmark bookmark = new Bookmark();
        BookmarksDataSource bds = new BookmarksDataSource(this);
        bookmark = bds.getBookmark(bookmarkId);
        bds.close();

        TextView bookmarkName = (TextView) findViewById(R.id.bookmark_detail_name_value);
        bookmarkName.setText(bookmark.getName());

        TextView bookmarkSerializedValue = (TextView) findViewById(R.id.bookmark_detail_serialized_value_value);
        bookmarkSerializedValue.setText(bookmark.getSerializedValue());

        currentBookmarkSerializedValue = bookmark.getSerializedValue();

        try {
            // add listeners to buttons
            // have to cast to Button in this case
            buttonPlayBookmark = (Button) findViewById(R.id.button_play_bookmark);
            buttonPlayBookmark.setOnClickListener(this);
        } catch (Exception e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }
    }

    /**
     * onClick override that acts as a router to start desired activities.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_play_bookmark:
                List<Note> notes = new ArrayList<Note>();

                JSONArray jsonArr = new JSONArray();
                try {
                    JSONObject mainJsonObj = new JSONObject(currentBookmarkSerializedValue);
                    jsonArr = mainJsonObj.getJSONArray("notes");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                for (int i = 0; i < jsonArr.length(); i++) {
                    JSONObject jsonNote;
                    int notevalue = 0;
                    int velocity = 0;
                    float length = 1.0f;
                    int position = 1;
                    try {
                        jsonNote = jsonArr.getJSONObject(i);
                        notevalue = jsonNote.getInt("notevalue");
                        velocity = jsonNote.getInt("velocity");
                        length = Float.parseFloat(jsonNote.getString("length"));
                        position = jsonNote.getInt("position");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    Note note = new Note();
                    note.setNotevalue(notevalue);
                    note.setVelocity(velocity);
                    note.setLength(length);
                    note.setPosition(position);
                    notes.add(note);
                }

                // get default instrument for playback
                SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
                String defaultInstrument = sharedPref.getString("pref_default_instrument", "");

                GenerateMusicActivity generateMusic = new GenerateMusicActivity();
                generateMusic.generateMusic(notes, musicSource, defaultInstrument);

                // play generated notes for user
                playMusic(musicSource);

                break;
        }
    }

    public void playMusic(File musicSource) {
        // get media player ready
        mediaPlayer = MediaPlayer.create(this, Uri.fromFile(musicSource));

        // play music
        mediaPlayer.start();
    }

    /**
     * onBackPressed override used to stop playing music when done with activity
     */
    @Override
    public void onBackPressed() {
        Log.d("MYLOG", "stop playing music!");

        // stop playing music
        mediaPlayer.stop();

        super.onBackPressed();
    }

}
