
package com.andrewsummers.otashu.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.andrewsummers.otashu.data.EdgesDataSource;
import com.andrewsummers.otashu.data.LabelsDataSource;
import com.andrewsummers.otashu.data.NotesetsDataSource;
import com.andrewsummers.otashu.data.NotevaluesDataSource;
import com.andrewsummers.otashu.model.Edge;
import com.andrewsummers.otashu.model.Label;
import com.andrewsummers.otashu.model.Note;
import com.andrewsummers.otashu.model.Notevalue;
import com.andrewsummers.otashu.view.PlaybackGLSurfaceView;
import com.leff.midi.MidiFile;
import com.leff.midi.MidiTrack;
import com.leff.midi.event.NoteOff;
import com.leff.midi.event.NoteOn;
import com.leff.midi.event.ProgramChange;
import com.leff.midi.event.meta.Tempo;
import com.leff.midi.event.meta.TimeSignature;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.net.Uri;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.util.SparseArray;

/**
 * GenerateMusicActivity is an Activity which generates music.
 * <p>
 * This activity provides a way to generate music for later playback. There are a small variety of
 * generation algorithms included, namely: Logic A (music generation based on notesets matching on
 * first or last notes in each noteset sequence) and Logic B (music generation based on transition
 * data the Apprentice has previously learned).
 * </p>
 */
public class GenerateMusicActivity extends Activity {
    private GLSurfaceView mGLView;
    int selectedEmotionId = 1;
    int selectedInstrumentId = -1;
    int playbackSpeed = 120;
    File path = Environment.getExternalStorageDirectory();
    String externalDirectory = path.toString() + "/otashu/";
    File musicSource = new File(externalDirectory + "otashu.mid");
    private static MediaPlayer mediaPlayer;
    private SparseArray<List<Integer>> musicalKeys = new SparseArray<List<Integer>>();
    private SparseArray<float[]> noteColorTable = new SparseArray<float[]>();
    private static SparseArray<String> noteMap;
    static
    {
        noteMap = new SparseArray<String>();
        noteMap.put(60, "C4");
        noteMap.put(61, "C#4");
        noteMap.put(62, "D4");
        noteMap.put(63, "D#4");
        noteMap.put(64, "E4");
        noteMap.put(65, "F4");
        noteMap.put(66, "F#4");
        noteMap.put(67, "G4");
        noteMap.put(68, "G#4");
        noteMap.put(69, "A4");
        noteMap.put(70, "A#4");
        noteMap.put(71, "B4");
    }

    /**
     * onCreate override that provides entry point for activity.
     * 
     * @param savedInstanceState Current application state data.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // get selected instrument_id from spinner
        Bundle bundle = getIntent().getExtras();
        selectedEmotionId = bundle.getInt("emotion_id");
        selectedInstrumentId = bundle.getInt("instrument_id");

        // TODO: double-check this section later

        // C4, E4, G4
        musicalKeys.put(60, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            {
                add(60);
                add(64);
                add(67);
            }
        });

        // C#4, F4, G#4
        musicalKeys.put(61, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            {
                add(61);
                add(65);
                add(67);
            }
        });

        // D4, F#4, A4
        musicalKeys.put(62, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            {
                add(62);
                add(66);
                add(69);
            }
        });

        // D#4, G4, A#4
        musicalKeys.put(63, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            {
                add(63);
                add(67);
                add(70);
            }
        });

        // E4, G#4, B4
        musicalKeys.put(64, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            {
                add(64);
                add(68);
                add(71);
            }
        });

        // F4, A4, C4
        musicalKeys.put(65, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            {
                add(65);
                add(69);
                add(60);
            }
        });

        // F#4, A#4, C#4
        musicalKeys.put(66, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            {
                add(66);
                add(70);
                add(61);
            }
        });

        // G4, B4, D4
        musicalKeys.put(67, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            {
                add(67);
                add(71);
                add(62);
            }
        });

        // G#4, C4, D#4
        musicalKeys.put(68, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            {
                add(68);
                add(60);
                add(63);
            }
        });

        // A4, C#4, E4
        musicalKeys.put(69, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            {
                add(69);
                add(61);
                add(64);
            }
        });

        // A#4, D4, F4
        musicalKeys.put(70, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            {
                add(70);
                add(62);
                add(65);
            }
        });

        // B4, D#4, F#4
        musicalKeys.put(71, new ArrayList<Integer>() {
            private static final long serialVersionUID = 1L;
            {
                add(71);
                add(63);
                add(66);
            }
        });

        //SparseArray<List<Note>> allNotesets = gatherRelatedEmotions();
        List<Note> notes = new ArrayList<Note>();

        notes = logicC();
        // notes = logicB(allNotesets);

        final List<Note> finalNotes = notes;

        /*
         * // original logic for (int i = 0; i < 4; i++) {
         * notes.addAll(getRandomNoteset(allNotesets)); }
         */

        StringBuilder notesText = new StringBuilder();
        int lineBreak = 1;
        for (Note note : notes) {
            notesText.append(noteMap.get(note.getNotevalue()) + ", ");
            if (lineBreak % 4 == 0)
                notesText.append("\n");
            lineBreak++;
        }

        // get default instrument for playback
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String defaultInstrument = sharedPref.getString("pref_default_instrument", "");
        playbackSpeed = Integer.valueOf(sharedPref.getString("pref_default_playback_speed", "120"));

        generateMusic(notes, musicSource, defaultInstrument, playbackSpeed);

        playMusic(musicSource);

        List<Notevalue> allNotevalues = new ArrayList<Notevalue>();
        List<Label> allLabels = new ArrayList<Label>();

        NotevaluesDataSource nvds = new NotevaluesDataSource(this);
        allNotevalues = nvds.getAllNotevalues();
        nvds.close();

        LabelsDataSource lds = new LabelsDataSource(this);
        allLabels = lds.getAllLabels();
        lds.close();

        int color = 0;

        for (Notevalue notevalue : allNotevalues) {
            boolean found = false;
            for (Label label : allLabels) {
                if (notevalue.getLabelId() == label.getId()) {
                    color = Color.parseColor(label.getColor());
                    float[] noteColor = {
                            Color.red(color) / 255.0f, Color.green(color) / 255.0f,
                            Color.blue(color) / 255.0f, 1.0f
                    };
                    noteColorTable.put(notevalue.getNotevalue(), noteColor);
                    found = true;
                    break;
                }
            }

            if (!found) {
                color = Color.parseColor("#dddddd");
                float[] noteColor = {
                        Color.red(color) / 255.0f, Color.green(color) / 255.0f,
                        Color.blue(color) / 255.0f
                };
                noteColorTable.put(notevalue.getNotevalue(), noteColor);
            }
        }

        // Use GLSurfaceView as ContentView for this Activity
        mGLView = new PlaybackGLSurfaceView(this, notes, noteColorTable, playbackSpeed);
        setContentView(mGLView);

        // return to previous activity when done playing
        mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer aMediaPlayer) {
                // fill return intent with values we are passing to parent activity
                Intent output = new Intent();
                output.putExtra("serialized_notes", serializeNotes(finalNotes));

                // sending back data to parent activity (the activity that originally launched this
                // activity)
                setResult(RESULT_OK, output);

                if (mediaPlayer != null) {
                    if (mediaPlayer.isPlaying()) {
                        // stop playing music
                        mediaPlayer.stop();
                    }
                    mediaPlayer.release();
                }

                // close activity
                finish();
            }
        });
    }

    /**
     * Gather related notesets for a given emotion from datastore. Emotion selection is made via a
     * bundle variable.
     * 
     * @return HashMap of notes related to given emotion
     */
    public SparseArray<List<Note>> gatherRelatedEmotions() {
        SparseArray<List<Note>> allNotesetBundles = new SparseArray<List<Note>>();
        Note emptyNote = new Note();
        List<Note> emptyNoteList = new LinkedList<Note>();
        emptyNoteList.add(emptyNote);

        NotesetsDataSource nds = new NotesetsDataSource(this);
        allNotesetBundles = nds.getAllNotesetBundles(selectedEmotionId);
        nds.close();

        // prevent crashes due to lack of database data
        if (allNotesetBundles.size() <= 0) {
            allNotesetBundles.put(1, emptyNoteList);
        }

        return allNotesetBundles;
    }

    /**
     * Get a random noteset from a HashMap of notesets.
     * 
     * @param notesets HashMap of notesets from which to choose.
     * @return List of notes related to chosen noteset.
     */
    public List<Note> getRandomNoteset(HashMap<Integer, List<Note>> notesets) {
        List<Note> notes = new LinkedList<Note>();

        // get random noteset
        Random random = new Random();
        List<Integer> keys = new ArrayList<Integer>(notesets.keySet());
        Integer randomKey = keys.get(random.nextInt(keys.size()));
        notes = notesets.get(randomKey);

        return notes;
    }

    /**
     * Generate music and write results to a MIDI file.
     * 
     * @param notes List<Note> of notes to write to file.
     * @param musicSource File location of musicSource file for writing.
     * @param defaultInstrument
     */
    public void generateMusic(List<Note> notes, File musicSource, String defaultInstrument,
            int playbackSpeed) {
        MidiTrack tempoTrack = new MidiTrack();
        MidiTrack noteTrack = new MidiTrack();

        TimeSignature ts = new TimeSignature();
        ts.setTimeSignature(4, 4, TimeSignature.DEFAULT_METER, TimeSignature.DEFAULT_DIVISION);

        Tempo t = new Tempo();
        t.setBpm(playbackSpeed);

        if ((selectedInstrumentId < 0) && (defaultInstrument != null)) {
            try {
                selectedInstrumentId = Integer.valueOf(defaultInstrument);
            } catch (Exception e) {
                Log.d("MYLOG", e.getStackTrace().toString());
                // set default to 1 (piano) if no default preference found
                selectedInstrumentId = 1;
            }
        }

        // set instrument type
        ProgramChange pc = new ProgramChange(0, 0, selectedInstrumentId);

        tempoTrack.insertEvent(ts);
        tempoTrack.insertEvent(t);
        tempoTrack.insertEvent(pc);

        int currentTotalNoteLength = 480;

        for (int i = 0; i < notes.size(); i++) {
            int channel = 0;
            int pitch = notes.get(i).getNotevalue();
            int velocity = 100;
            int length = 480;
            float fLength = 480.0f;

            fLength = notes.get(i).getLength();
            if (notes.get(i).getVelocity() > 0)
                velocity = notes.get(i).getVelocity();

            if (fLength > 0.0)
                fLength = (480 * notes.get(i).getLength());
            else
                fLength = 480;

            length = (int) fLength;

            NoteOn on = new NoteOn(i * currentTotalNoteLength, channel, pitch, velocity);
            NoteOff off = new NoteOff(i * currentTotalNoteLength + length, channel, pitch, 0);

            noteTrack.insertEvent(on);
            noteTrack.insertEvent(off);

            noteTrack.insertNote(channel, pitch, velocity, i * currentTotalNoteLength, length);

            if (length > 0) {
                currentTotalNoteLength = 480; // TODO: make this match note length (better) somehow
            } else {
                currentTotalNoteLength = 480;
            }
        }

        ArrayList<MidiTrack> tracks = new ArrayList<MidiTrack>();
        tracks.add(tempoTrack);
        tracks.add(noteTrack);

        MidiFile midi = new MidiFile(MidiFile.DEFAULT_RESOLUTION, tracks);

        try {
            midi.writeToFile(musicSource);
        } catch (IOException e) {
            Log.d("MYLOG", e.getStackTrace().toString());
        }

        serializeNotes(notes);
    }

    public void playMusic(File musicSource) {
        // get media player ready
        if (mediaPlayer == null) {
            mediaPlayer = MediaPlayer.create(this, Uri.fromFile(musicSource));
        } else {
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(this, Uri.fromFile(musicSource));
        }

        // play music
        mediaPlayer.start();
    }

    /**
     * onBackPressed override used to stop playing music when done with activity
     */
    @Override
    public void onBackPressed() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                // stop playing music
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
        super.onBackPressed();
    }

    /*
     * Original hard-coded logic
     */
    private List<Note> logicA(SparseArray<List<Note>> allNotesets) {
        List<Note> notes = new ArrayList<Note>();
        List<Integer> lookingForNotesInKey = new ArrayList<Integer>();
        Note lastNote = new Note();
        lastNote.setNotevalue(0);

        Random random = new Random();

        List<Integer> keys = new ArrayList<Integer>();
        for (int i = 0; i < allNotesets.size(); i++) {
            keys.add(allNotesets.keyAt(i));
        }

        // List<Integer> keys = new ArrayList<Integer>(allNotesets.keySet());
        Integer randomKey = keys.get(random.nextInt(keys.size()));

        // loop through all found emotion-related notesets
        for (int i = 0; i < 16; i++) {
            List<Note> nsets = new ArrayList<Note>();

            // get random noteset (and try to find one that matches the new musical key search
            // focus)
            random = new Random();
            randomKey = keys.get(random.nextInt(keys.size()));

            // get individual noteset
            nsets = allNotesets.get(randomKey);

            try {
                // Check if last note in current noteset sequence matches first note in a musical
                // key list. A match gives us criteria for finding another, similar noteset to
                // append for playback

                if (musicalKeys.get(nsets.get(3).getNotevalue()) != null) {
                    lookingForNotesInKey = musicalKeys.get(nsets.get(3).getNotevalue());
                } else {
                    lookingForNotesInKey = musicalKeys.get(60);
                }
            } catch (Exception e) {
                Log.d("MYLOG", e.getStackTrace().toString());
            }

            for (int j = 0; j < 50; j++) {
                random = new Random();
                randomKey = keys.get(random.nextInt(keys.size()));

                List<Note> noteset = allNotesets.get(randomKey);

                if (lookingForNotesInKey.contains(noteset.get(0).getNotevalue())) {
                    for (int k = 0; k < 4; k++) {
                        try {
                            notes.add(noteset.get(k));
                        } catch (Exception e) {
                            Log.d("MYLOG", e.getStackTrace().toString());
                        }
                    }
                    lastNote = noteset.get(3);
                    break;
                }

            }
        }

        return notes;
    }

    /*
     * Random-based logic.
     */
    private List<Note> logicB(SparseArray<List<Note>> gatheredNotesetList) {
        List<Note> notes = new ArrayList<Note>();
        Note lastNote = new Note();
        lastNote.setNotevalue(0);

        Random random = new Random();

        List<Integer> keys = new ArrayList<Integer>();
        for (int i = 0; i < gatheredNotesetList.size(); i++) {
            keys.add(gatheredNotesetList.keyAt(i));
        }

        Integer randomKey = keys.get(random.nextInt(keys.size()));

        // loop through all found emotion-related notesets
        for (int i = 0; i < 16; i++) {
            List<Note> nset = new ArrayList<Note>();

            // get random noteset
            random = new Random();
            randomKey = keys.get(random.nextInt(keys.size()));

            // get individual noteset
            nset = gatheredNotesetList.get(randomKey);

            try {
                for (Note note : nset) {
                    notes.add(note);
                }
            } catch (Exception e) {
                Log.d("MYLOG", e.getStackTrace().toString());
            }
        }

        return notes;
    }

    private List<Note> logicC() {
        
        /*
         * ## LogicC
         * 1. Get selected emotion
         * 2. Gather a strong (low-weight) noteset from Emotions graph using selected emotion
         * 3. Get a strong Transition graph edge for current noteset's tail
         * 4. Check if there is any strong noteset with a head beginning with previously found strong Transition graph edge
         * 5. If noteset found, add to output list and repeat from Step 3 until done
         * 5b. If noteset not found, randomly find an emotion-related noteset and repeat from Step 3 until done
         */
        
        List<Note> notes = new ArrayList<Note>();
        int nextNodeTo = 0;
        
        for (int i = 0; i < 8; i++) {
            
            List<Edge> edges = new ArrayList<Edge>();
            
            // 1. Get selected emotion (selectedEmotionId)
            // get Emotion graph id
            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            long graphId = Long.parseLong(sharedPref.getString(
                    "pref_emotion_graph_for_apprentice", "1"));
            
            // 2. Gather a strong (low-weight) noteset from Emotions graph using selected emotion
            EdgesDataSource eds = new EdgesDataSource(this);
            if (nextNodeTo != 0) {
                Log.d("MYLOG", "using nextNodeTo: " + nextNodeTo);
                edges = eds.getStrongPath(graphId, selectedEmotionId, 0, nextNodeTo);
            } else {
                Log.d("MYLOG", "not using nextNodeTo...");
                edges = eds.getStrongPath(graphId, selectedEmotionId, 0);
            }
            
            // add edge results to notes list
            for (int j = 0; j < edges.size(); j++) {
                Note note = new Note();
                note.setNotevalue(edges.get(j).getFromNodeId());
                notes.add(note);
            }
            // add last edge
            Note note = new Note();
            note.setNotevalue(edges.get(edges.size()-1).getToNodeId());
            notes.add(note);
            
            // 3. Get a strong Transition graph edge for current noteset's tail
            graphId = Long.parseLong(sharedPref.getString(
                    "pref_transition_graph_for_apprentice", "2"));
            
            Edge edge = eds.getStrongTransitionPath(graphId, selectedEmotionId, edges.get(edges.size()-1).getToNodeId());
            
            // 4. Check if there is any strong noteset with a head beginning with previously found strong Transition graph edge
            // 5. If noteset found, add to output list and repeat from Step 3 until done
            if (edge != null) {
                Log.d("MYLOG", "logic c found transition: " + edges.toString());
                nextNodeTo = edge.getToNodeId();
            }
            // 5b. If noteset not found, randomly find an emotion-related noteset and repeat from Step 3 until done
            else {
                Log.d("MYLOG", "logic c didn't find a transition... using random approach");
                nextNodeTo = 0;
            }
            
            eds.close();
        }
        
        Log.d("MYLOG", "logic c notes: " + notes.toString());
        
        return notes;
    }

    public String serializeNotes(List<Note> notes) {
        JSONObject mainJsonObj = new JSONObject();
        JSONArray jsonArr = new JSONArray();

        try {
            // add each note into JSON object
            for (Note note : notes) {
                JSONObject jsonObj = new JSONObject();
                jsonObj.put("id", note.getId());
                jsonObj.put("notevalue", note.getNotevalue());
                jsonObj.put("velocity", note.getVelocity());
                jsonObj.put("length", note.getLength());
                jsonObj.put("position", note.getPosition());
                jsonArr.put(jsonObj);
            }

            mainJsonObj.put("notes", jsonArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Log.d("MYLOG", mainJsonObj.toString());
        return mainJsonObj.toString();
    }
}
