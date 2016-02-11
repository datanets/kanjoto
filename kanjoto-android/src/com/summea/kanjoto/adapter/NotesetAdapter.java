
package com.summea.kanjoto.adapter;

import java.util.Arrays;
import java.util.List;

import com.summea.kanjoto.R;
import com.summea.kanjoto.data.LabelsDataSource;
import com.summea.kanjoto.data.NotevaluesDataSource;
import com.summea.kanjoto.model.Label;
import com.summea.kanjoto.model.NotesetAndRelated;
import com.summea.kanjoto.model.Notevalue;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NotesetAdapter extends BaseAdapter {

    private Context mContext;
    private List<NotesetAndRelated> notesetsAndRelated;
    SparseArray<Notevalue> notevalues = new SparseArray<Notevalue>();
    SparseArray<Label> labels = new SparseArray<Label>();

    public NotesetAdapter(Context context, List<NotesetAndRelated> allNotesetsAndNotes) {
        mContext = context;
        notesetsAndRelated = allNotesetsAndNotes;

        NotevaluesDataSource nvds = new NotevaluesDataSource(mContext);
        List<Notevalue> allNotevalues = nvds.getAllNotevalues();
        nvds.close();

        for (Notevalue notevalue : allNotevalues) {
            notevalues.put(notevalue.getNotevalue(), notevalue);
        }

        LabelsDataSource lds = new LabelsDataSource(mContext);
        List<Label> allLabels = lds.getAllLabels();
        lds.close();

        for (Label label : allLabels) {
            labels.put((int) label.getId(), label);
        }
    }

    @Override
    public int getCount() {
        return notesetsAndRelated.size();
    }

    @Override
    public Object getItem(int position) {
        return notesetsAndRelated.get(position);
    }

    @Override
    public long getItemId(int position) {
        return notesetsAndRelated.get(position).getNoteset().getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                    R.layout.row_noteset, null);
        }

        TextView emotion = (TextView) convertView.findViewById(R.id.emotion);
        emotion.setText(notesetsAndRelated.get(position).getEmotion().getName());

        String backgroundColor = "#dddddd";
        if (notesetsAndRelated.get(position).getLabel().getColor() != null) {
            backgroundColor = notesetsAndRelated.get(position).getLabel().getColor();
        }

        if (notesetsAndRelated.get(position).getNoteset().getEnabled() == 0) {
            backgroundColor = "#e8e8e8";
        }

        // add correct color to background (but maintain default state "pressed" and "selected"
        // effects)
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[] {
                android.R.attr.state_pressed
        }, mContext.getResources().getDrawable(R.drawable.row_selector));
        drawable.addState(new int[] {
                android.R.attr.state_selected
        }, mContext.getResources().getDrawable(R.drawable.row_selector));
        drawable.addState(new int[] {}, new ColorDrawable(Color.parseColor(backgroundColor)));
        emotion.setBackground(drawable);

        int[] noteItems = {
                R.id.note_1, R.id.note_2, R.id.note_3, R.id.note_4
        };
        TextView note = null;

        // fill in note names for each note in each row of this custom list
        for (int i = 0; i < noteItems.length; i++) {
            note = (TextView) convertView.findViewById(noteItems[i]);
            // note.setText(getNoteName(notesetsAndRelated.get(position).getNotes().get(i).getNotevalue()));
            note.setText(notevalues.get(
                    notesetsAndRelated.get(position).getNotes().get(i).getNotevalue())
                    .getNotelabel());

            backgroundColor = "#dddddd";
            if (labels.get(
                    (int) notevalues.get(
                            notesetsAndRelated.get(position).getNotes().get(i).getNotevalue())
                            .getLabelId()).getColor() != null)
                backgroundColor = labels.get(
                        (int) notevalues.get(
                                notesetsAndRelated.get(position).getNotes().get(i).getNotevalue())
                                .getLabelId()).getColor();

            if (notesetsAndRelated.get(position).getNoteset().getEnabled() == 0) {
                backgroundColor = "#e8e8e8";
            }

            drawable = new StateListDrawable();
            drawable.addState(new int[] {
                    android.R.attr.state_pressed
            }, mContext.getResources().getDrawable(R.drawable.row_selector));
            drawable.addState(new int[] {
                    android.R.attr.state_selected
            }, mContext.getResources().getDrawable(R.drawable.row_selector));
            drawable.addState(new int[] {}, new ColorDrawable(Color.parseColor(backgroundColor)));
            note.setBackground(drawable);
        }

        return convertView;
    }

    public String getNoteName(int noteValue) {
        String[] noteNamesArray = mContext.getResources().getStringArray(R.array.note_labels_array);
        String[] noteValuesArray = mContext.getResources()
                .getStringArray(R.array.note_values_array);

        // get array index position of note value (so we can get correct note name later)
        int noteIndex = Arrays.asList(noteValuesArray).indexOf(String.valueOf(noteValue));

        // return correct note name from note names array
        return noteNamesArray[noteIndex];
    }

    public void addItem(NotesetAndRelated notesetAndRelatedToBeAdded) {
        notesetsAndRelated.add(notesetAndRelatedToBeAdded);
    }

    public Object removeItem(int position) {
        return notesetsAndRelated.remove(position);
    }

    public void clear() {
        notesetsAndRelated.clear();
    }
}
