package model;

import javax.sound.midi.*;
import java.util.ArrayList;

public class DrumTrackList {
    private Sequence sequence;
    private ArrayList<Instrument> instruments;
    private ArrayList<Track> tracks;

    public DrumTrackList() throws InvalidMidiDataException {
        sequence = new Sequence(Sequence.PPQ, 4);
        instruments = new ArrayList<>();
        tracks = new ArrayList<>();
    }

    public Sequence getSequence() {
        return sequence;
    }

    public ArrayList<Instrument> getInstruments() {
        return instruments;
    }

    public ArrayList<Track> getTracks() {
        return tracks;
    }

    // REQUIRES: instrument have grammatical notes
    //           35 <= instrument number <= ???
    // MODIFIES: this
    // EFFECTS: creates a new track for the instrument and adds it to the list of tracks
    public void addTrack(Instrument instrument) throws Exception {
        Track track = sequence.createTrack();
        tracks.add(track);
        instruments.add(instrument);

        int n = 1;
        for (char i: instrument.getInstrumentNotes()) {

            if (i == 'x') {
                track.add(makeEvent(instrument.getInstrumentNumber(), true, n));
            } else if (i == '-') {
                track.add(makeEvent(instrument.getInstrumentNumber(), false, n));
            }
            n += 1;
        }
    }

    // REQUIRES: 0 <= int <= instruments.length()
    // MODIFIES: this
    // EFFECTS: removes an instrument from the track
    public void removeTrack(int instrumentNumber) {
        tracks.remove(instrumentNumber);
        instruments.remove(instrumentNumber);
    }

    // REQUIRES: 0 <= instrumentNumber <= ???
    //           0 <= velocity <= 100
    //           0 <= tick
    // EFFECTS: returns a midi event with (a) the instrument, (b) the velocity of note hit, (c) time of event in ticks
    private MidiEvent makeEvent(int instrumentNumber, boolean on, int tick) throws Exception {
        MidiEvent event = null;
        ShortMessage a = new ShortMessage();
        if (on) {
            a.setMessage(144, 9, instrumentNumber, 100);
        } else {
            a.setMessage(128, 9, instrumentNumber, 100);
        }
        event = new MidiEvent(a, tick);

        return event;
    }
}
