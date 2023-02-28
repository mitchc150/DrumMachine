package model;

import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

// Represents a particular instrument track with an instrument MIDI number, instrument name and notes to be played
public class Instrument {
    private static final List<String> INSTRUMENT_LIST = Arrays.asList("Acoustic Bass Drum",
            "Bass Drum 1", "Side Stick", "Acoustic Snare", "Hand Clap", "Electric Snare",
            "Low Floor Tom", "Closed Hi-Hat", "High Floor Tom", "Pedal Hi-Hat", "Low Tom", "Open Hi-Hat",
            "Low Mid-Tom", "Hi-Mid Tom", "Crash Cymbal 1", "High Tom", "Ride Cymbal 1",
            "Chinese Cymbal", "Ride Bell", "Tambourine", "Splash Cymbal", "Cowbell",
            "Crash Cymbal 2", "Vibraslap", "Ride Cymbal 2", "Hi Bongo", "Low Bongo",
            "Mute Hi Conga", "Open Hi Conga", "Low Conga", "High Timbale", "Low Timbale",
            "High Agogo", "Low Agogo", "Cabasa", "Maracas", "Short Whistle", "Long Whistle",
            "Short Guiro", "Long Guiro", "Claves", "Hi Wood Block", "Low Wood Block",
            "Mute Cuica", "Open Cuica", "Mute Triangle", "Open Triangle");

    private int instrumentNumber;
    private String instrumentName;
    private char[] instrumentNotesList;
    private String instrumentNotes;



    // REQUIRES: 35 <= instrumentNumber <= 81
    //           instrumentNotes consists only of '-' and 'x'
    // MODIFIES: this
    // EFFECTS: creates a new Instrument with MIDI number, name and notes to be played
    public Instrument(int instrumentNumber, String instrumentNotes) {
        this.instrumentNotesList = instrumentNotes.toCharArray();
        this.instrumentNotes = instrumentNotes;
        this.instrumentNumber = instrumentNumber;
        this.instrumentName = INSTRUMENT_LIST.get(instrumentNumber - 35);
    }

    // EFFECTS: returns instrument's MIDI code number
    public int getInstrumentNumber() {
        return this.instrumentNumber;
    }

    // EFFECTS: returns instrument's name
    public String getInstrumentName() {
        return this.instrumentName;
    }

    // EFFECTS: returns instrument's notes to be played, in list form
    public char[] getInstrumentNotesList() {
        return this.instrumentNotesList;
    }

    // EFFECTS: returns instrument's notes to be played, in string form
    public String getInstrumentNotesString() {
        return this.instrumentNotes;
    }

    // REQUIRES: 35 <= newNumber <= 81
    // MODIFIES: this
    // EFFECTS: changes the instrument MIDI code, and updates the name accordingly
    public void setInstrument(int newNumber) {
        this.instrumentNumber = newNumber;
        this.instrumentName = INSTRUMENT_LIST.get(newNumber - 35);
    }

    // REQUIRES: newNotes consists only of 'x' and '-'
    // MODIFIES: this
    // EFFECTS: changes the instrument's notes to be played
    public void setInstrumentNotes(String newNotes) {
        this.instrumentNotesList = newNotes.toCharArray();
    }

    // EFFECTS: returns the instrument as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("number", instrumentNumber);
        json.put("notes", instrumentNotes);
        return json;
    }

}