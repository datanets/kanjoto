
package com.andrewsummers.otashu.activity;

import java.util.List;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

import com.andrewsummers.otashu.R;
import com.andrewsummers.otashu.data.KeySignaturesDataSource;
import com.andrewsummers.otashu.data.OtashuDatabaseHelper;
import com.andrewsummers.otashu.model.KeySignature;

public class DatabaseDumperKeySignaturesActivity extends Activity {
    private SharedPreferences sharedPref;
    private long apprenticeId = 0;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_database_dumper);
        
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        apprenticeId = Long.parseLong(sharedPref.getString(
                "pref_selected_apprentice", "1"));

        KeySignaturesDataSource edds = new KeySignaturesDataSource(this);
        List<KeySignature> allKeySignatures = edds.getAllKeySignatures(apprenticeId);
        edds.close();

        TextView debugText = (TextView) findViewById(R.id.debug_text);

        debugText
                .setText(debugText.getText().toString() + "Table: Key Signatures\n"
                        + OtashuDatabaseHelper.COLUMN_ID + "|"
                        + OtashuDatabaseHelper.COLUMN_EMOTION_ID + "|"
                        + OtashuDatabaseHelper.COLUMN_APPRENTICE_ID + "\n");

        for (KeySignature keySignature : allKeySignatures) {

            String newText = debugText.getText().toString();
            newText += keySignature.getId() + "|"
                    + keySignature.getEmotionId() + "|"
                    + keySignature.getApprenticeId() + "\n";

            debugText.setText(newText);
        }
    }
}
