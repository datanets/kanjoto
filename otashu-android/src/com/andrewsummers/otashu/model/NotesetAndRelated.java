package com.andrewsummers.otashu.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Noteset is a model for Noteset objects.
 */
public class NotesetAndRelated {
    
    private Emotion emotion = new Emotion();
    private Label label = new Label();
    private Noteset noteset = new Noteset();
    private List<Note> notes = new ArrayList<Note>();
    
    /**
     * getEmotion gets emotion
     * 
     * @return <code>Emotion</code> emotion object
     */
    public Emotion getEmotion() {
        return emotion;
    }

    /**
     * setEmotion sets emotion
     * 
     * @param emotion
     *            New Emotion object.
     */
    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }
    
    /**
     * getLabel gets related Label
     * 
     * @return <code>Label</code> label object
     */
    public Label getLabel() {
        return label;
    }

    /**
     * setLabel sets label
     * 
     * @param label
     *            New label object.
     */
    public void setLabel(Label label) {
        this.label = label;
    }
    
    /**
     * getNoteset gets noteset
     * 
     * @return <code>Noteset</code> noteset object
     */
    public Noteset getNoteset() {
        return noteset;
    }

    /**
     * setNoteset sets noteset
     * 
     * @param noteset
     *            New noteset object.
     */
    public void setNoteset(Noteset noteset) {
        this.noteset = noteset;
    }
    
    /**
     * getNotes gets related notes
     * 
     * @return <code>List<Note></code> notes list
     */
    public List<Note> getNotes() {
        return notes;
    }

    /**
     * setNotes sets related notes
     * 
     * @param 
     *            New notes list value.
     */
    public void setNotes(List<Note> notes) {
        this.notes = notes;
    }
    
    /**
     * toString override to return noteset name.
     * 
     * @return <code>String</code> of noteset name.
     */
    @Override
    public String toString() {
        return "noteset and notes";
    }
}