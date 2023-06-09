package model;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.sound.midi.*;
import java.util.ArrayList;

// Represents a sequencer with a list of instruments and their notes. Translates instruments
// into MIDI tracks and adds them into a sequence, with beats per minute.

public class DrumTrackList {
    private final Sequence sequence;
    private final ArrayList<Instrument> instruments;
    private final ArrayList<Track> tracks;
    private int bpm;

    // REQUIRES: bpm >= 0
    // MODIFIES: this
    // EFFECTS: initializes the sequence, the list of images and tracks, and beats per minute
    public DrumTrackList(int bpm) throws InvalidMidiDataException {
        sequence = new Sequence(Sequence.PPQ, 4);
        instruments = new ArrayList<>();
        tracks = new ArrayList<>();
        this.bpm = bpm;
    }

    // EFFECTS: returns the sequence from the tracklist
    public Sequence getSequence() {
        return sequence;
    }

    // EFFECTS: returns the array of instruments in the tracklist
    public ArrayList<Instrument> getInstruments() {
        return instruments;
    }

    // EFFECTS: returns the array of MIDI tracks in the tracklist
    public ArrayList<Track> getTracks() {
        return tracks;
    }

    // REQUIRES: instrument have grammatical notes
    //           35 <= instrument number <= 81
    // MODIFIES: this
    // EFFECTS: creates a new track for the instrument and adds it to the list of tracks
    public void addTrack(Instrument instrument) throws Exception {
        Track track = sequence.createTrack();
        tracks.add(track);
        instruments.add(instrument);

        int n = 1;
        for (char i : instrument.getInstrumentNotesList()) {

            if (i == 'x') {
                track.add(makeEvent(instrument.getInstrumentNumber(), true, n));
            } else {
                track.add(makeEvent(instrument.getInstrumentNumber(), false, n));
            }
            n += 1;
        }

        EventLog.getInstance().logEvent(new Event("Added instrument number "
                + instrument.getInstrumentNumber() + " playing " + instrument.getInstrumentNotesString() + " to the"
                + " track list"));
    }

    // REQUIRES: 0 <= instrumentNumber <= instruments.length()
    // MODIFIES: this
    // EFFECTS: removes an instrument from the track
    public void removeTrack(int instrumentNumber) {
        Track track = tracks.get(instrumentNumber);
        tracks.remove(instrumentNumber);
        instruments.remove(instrumentNumber);
        sequence.deleteTrack(track);

        EventLog.getInstance().logEvent(new Event("Removed instrument number " + instrumentNumber));
    }

    // REQUIRES: 35 <= instrumentNumber <= 81
    //           0 <= velocity <= 100
    //           0 <= tick
    // EFFECTS: returns a midi event with (a) the instrument, (b) the velocity of note hit, (c) time of event in ticks
    // MODELLED ON / INSPIRED BY FUNCTIONALITY FOUND ON EXTERNAL SOURCE: https://www.geeksforgeeks.org/java-midi/
    private MidiEvent makeEvent(int instrumentNumber, boolean on, int tick) throws Exception {
        MidiEvent event;
        ShortMessage a = new ShortMessage();
        if (on) {
            a.setMessage(144, 9, instrumentNumber, 100);
        } else {
            a.setMessage(128, 9, instrumentNumber, 100);
        }
        event = new MidiEvent(a, tick);

        return event;
    }

    // EFFECTS: returns BPM of the drum machine
    public int getBPM() {
        return this.bpm;
    }

    // REQUIRES newBPM >= 0
    // MODIFIES: this
    // EFFECTS: changes BPM of the drum machine
    public void setBPM(int newBPM) {
        this.bpm = newBPM;

        EventLog.getInstance().logEvent(new Event("Set bpm to " + this.getBPM()));
    }

    // EFFECTS: returns the DrumTrackList as a JSON object
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("bpm", this.bpm);
        json.put("instruments", instrumentsToJson());
        EventLog.getInstance().logEvent(new Event("Saved file to JSON"));
        return json;
    }

    // EFFECTS: returns instruments in the tracklist as a JSON array
    private JSONArray instrumentsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Instrument i : instruments) {
            jsonArray.put(i.toJson());
        }

        return jsonArray;
    }

    // EFFECTS: records that the DrumTrackList is being played
    public static void play() {
        EventLog.getInstance().logEvent(new Event("Playing..."));
    }

    // EFFECTS: records that the DrumTrackList has been stopped
    public static void stop() {
        EventLog.getInstance().logEvent(new Event("Stopped playing"));
    }
}
