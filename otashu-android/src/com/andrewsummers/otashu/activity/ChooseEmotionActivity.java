package com.andrewsummers.otashu.activity;

import java.util.ArrayList;
import java.util.List;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.BookmarksDataSource;
import com.andrewsummers.otashu.data.EmotionsDataSource;
import com.andrewsummers.otashu.model.Bookmark;
import com.andrewsummers.otashu.model.Emotion;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

public class ChooseEmotionActivity extends Activity implements OnClickListener {

    private Button buttonGo = null;
    private Button buttonBookmark = null;
    private String lastSerializedNotes = "";
    
    /**
     * onCreate override that provides emotion-choose view to user.
     * 
     * @param savedInstanceState
     *            Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_choose_emotion);

        // add listeners to buttons
        buttonBookmark = (Button) findViewById(R.id.button_bookmark);
        buttonBookmark.setOnClickListener(this);
        
        buttonGo = (Button) findViewById(R.id.button_go);
        buttonGo.setOnClickListener(this);
        
        EmotionsDataSource emotionsDataSource = new EmotionsDataSource(this);
        emotionsDataSource.open();

        List<Emotion> allEmotions = new ArrayList<Emotion>();
        allEmotions = emotionsDataSource.getAllEmotions();
        
        emotionsDataSource.close();
        
        Spinner spinner = null;
        
        // locate next spinner in layout
        spinner = (Spinner) findViewById(R.id.spinner_emotion);

        // create array adapter for list of emotions
        ArrayAdapter<Emotion> emotionsAdapter = new ArrayAdapter<Emotion>(this, android.R.layout.simple_spinner_item);
        emotionsAdapter.addAll(allEmotions);
        
        // specify the default layout when list of choices appears
        emotionsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        
        // apply this adapter to the spinner
        spinner.setAdapter(emotionsAdapter);
        
        // instrument spinner
        ArrayAdapter<CharSequence> adapter = null;
        spinner = (Spinner) findViewById(R.id.spinner_instrument);
        adapter = ArrayAdapter
                .createFromResource(this, R.array.instrument_labels_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(String.valueOf("0")));

    }

    @Override
    public void onClick(View v) {
        Intent intent = null;

        switch (v.getId()) {
        case R.id.button_go:
            EmotionsDataSource emotionsDataSource = new EmotionsDataSource(this);
            emotionsDataSource.open();

            List<Integer> allEmotionIds = new ArrayList<Integer>();
            allEmotionIds = emotionsDataSource.getAllEmotionIds();
            
            Spinner emotionSpinner = (Spinner) findViewById(R.id.spinner_emotion);
            int selectedEmotionValue = allEmotionIds.get(emotionSpinner.getSelectedItemPosition());
            emotionsDataSource.close();

            Spinner instrumentSpinner = (Spinner) findViewById(R.id.spinner_instrument);
            int[] allInstrumentIds = getResources().getIntArray(R.array.instrument_values_array);
            int selectedInstrumentId = allInstrumentIds[instrumentSpinner.getSelectedItemPosition()];
            
            Bundle bundle = new Bundle();
            bundle.putInt("emotion_id", selectedEmotionValue);
            bundle.putInt("instrument_id", selectedInstrumentId);

            intent = new Intent(this, GenerateMusicActivity.class);
            intent.putExtras(bundle);            
            startActivityForResult(intent, 1);
            break;
        case R.id.button_bookmark:
            // save last generated noteset as a bookmark
            save_bookmark();
            break;
        }
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Log.d("MYLOG", "serialized notes: " + data.getStringExtra("serialized_notes"));
                lastSerializedNotes = data.getStringExtra("serialized_notes");
            }
        }
    }
    
    public int save_bookmark() {
        Bookmark bookmark = new Bookmark();
        bookmark.setName("Untitled");
        bookmark.setSerializedValue(lastSerializedNotes);
        saveBookmark(bookmark);
        return 0;
    }
    
    private void saveBookmark(Bookmark bookmark) {

        // save bookmark in database
        BookmarksDataSource bds = new BookmarksDataSource(this);
        bds.createBookmark(bookmark);
        
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.bookmark_saved),
                duration);
        toast.show();
    }
}