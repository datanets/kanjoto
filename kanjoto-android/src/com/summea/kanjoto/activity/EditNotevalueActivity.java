
package com.summea.kanjoto.activity;

import java.util.ArrayList;
import java.util.List;

import com.summea.kanjoto.R;
import com.summea.kanjoto.data.LabelsDataSource;
import com.summea.kanjoto.data.NotevaluesDataSource;
import com.summea.kanjoto.model.Label;
import com.summea.kanjoto.model.Notevalue;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

/**
 * EditNotevalueActivity is an Activity which provides users the ability to edit notevalues.
 * <p>
 * This activity provides a form for editing existing Notevalues. Notevalues to edit is selected
 * either via the "view all notevalues" activity or by the related "edit" context menu. The edit
 * form fills in data found (from the database) for specified Notevalue to edit and (if successful)
 * any saved updates will then be saved in the database.
 * </p>
 */
public class EditNotevalueActivity extends Activity implements OnClickListener {
    private Button buttonSave = null;
    private Notevalue editNotevalue; // keep track of which Notevalue is currently being edited

    /**
     * onCreate override that provides notevalue creation view to user .
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get specific layout for content view
        setContentView(R.layout.activity_edit_notevalue);

        // add listeners to buttons
        buttonSave = (Button) findViewById(R.id.button_save);
        buttonSave.setOnClickListener(this);

        long notevalueId = getIntent().getExtras().getLong("list_id");
        NotevaluesDataSource nvds = new NotevaluesDataSource(this);
        editNotevalue = nvds.getNotevalue(notevalueId);
        nvds.close();

        LabelsDataSource lds = new LabelsDataSource(this);
        List<String> allLabels = new ArrayList<String>();
        allLabels = lds.getAllLabelListPreviews();

        Label selectedLabel = lds.getLabel(editNotevalue.getLabelId());
        lds.close();

        ArrayAdapter<CharSequence> adapter = null;

        Spinner spinner = (Spinner) findViewById(R.id.spinner_notelabel);
        adapter = ArrayAdapter
                .createFromResource(this, R.array.note_labels_array,
                        android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(String.valueOf(editNotevalue.getNotelabel())));

        // locate next spinner in layout
        spinner = (Spinner) findViewById(R.id.spinner_label);

        // create array adapter for list of notevalues
        ArrayAdapter<String> labelsAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item);
        labelsAdapter.addAll(allLabels);

        // specify the default layout when list of choices appears
        labelsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // apply this adapter to the spinner
        spinner.setAdapter(labelsAdapter);
        spinner.setSelection(labelsAdapter.getPosition(selectedLabel.getName()));
    }

    /**
     * onClick override used to save notevalue data once user clicks save button.
     * 
     * @param view Incoming view.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_save:
                // gather notevalue data from form
                String[] noteValuesArray = getResources().getStringArray(R.array.note_values_array);
                String[] noteLabelsArray = getResources().getStringArray(R.array.note_labels_array);

                LabelsDataSource lds = new LabelsDataSource(this);
                List<Long> allLabelIds = lds.getAllLabelListDBTableIds();
                lds.close();

                Notevalue notevalueToUpdate = new Notevalue();

                Spinner notevalueNotevalue = (Spinner) findViewById(R.id.spinner_notelabel);
                // notevalueNotelabel = (Spinner) findViewById(R.id.spinner_notelabel);
                Spinner notevalueLabel = (Spinner) findViewById(R.id.spinner_label);

                notevalueToUpdate.setId(editNotevalue.getId());
                notevalueToUpdate.setNotevalue(Integer.parseInt(noteValuesArray[notevalueNotevalue
                        .getSelectedItemPosition()]));
                notevalueToUpdate.setNotelabel(noteLabelsArray[notevalueNotevalue
                        .getSelectedItemPosition()]);
                notevalueToUpdate.setLabelId(allLabelIds.get(notevalueLabel
                        .getSelectedItemPosition()));

                // update notevalue
                saveNotevalueUpdates(v, notevalueToUpdate);

                // close activity
                finish();
                break;
        }
    }

    /**
     * onResume override used to open up data source when resuming activity.
     */
    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * onPause override used to close data source when activity paused.
     */
    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * Save notevalue data.
     * 
     * @param v Incoming view.
     * @param data Incoming string of data to be saved.
     */
    private void saveNotevalueUpdates(View v, Notevalue notevalue) {
        // save notevalue in database
        NotevaluesDataSource eds = new NotevaluesDataSource(this);
        eds.updateNotevalue(notevalue);
        eds.close();

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context,
                context.getResources().getString(R.string.notevalue_saved),
                duration);
        toast.show();
    }
}
