package model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.Sequence;
import javax.sound.midi.Track;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;


public class DrumTrackListTest {
    Instrument instrument1;
    Instrument instrument2;
    DrumTrackList drumTrackList;

    @BeforeEach
    public void init() throws InvalidMidiDataException {
        instrument1 = new Instrument(15, "x-x-x-x-x-x");
        instrument2 = new Instrument(12, "-x-x-x-x-x-x");
        drumTrackList = new DrumTrackList();
    }

    @Test
    public void constructorTest() {
        assertEquals(drumTrackList.getSequence().getDivisionType(), Sequence.PPQ);
        assertEquals(drumTrackList.getSequence().getResolution(), 4);
        drumTrackList.getTracks().isEmpty();
        drumTrackList.getInstruments().isEmpty();
    }

    @Test
    public void addTrackTest() {
        drumTrackList.addTrack(instrument1);
        assertEquals(drumTrackList.getInstruments().get(0), instrument1);
    }

    @Test
    public void removeTrackTest() {
        drumTrackList.addTrack(instrument1);
        drumTrackList.addTrack(instrument2);
        assertEquals(drumTrackList.getInstruments().size(), 2);
        assertEquals(drumTrackList.getTracks().size(), 2);
        drumTrackList.removeTrack(0);
        assertEquals(drumTrackList.getInstruments().size(), 1);
        assertEquals(drumTrackList.getTracks().size(), 1);
        drumTrackList.removeTrack(0);
        assertEquals(drumTrackList.getInstruments().size(), 0);
        assertEquals(drumTrackList.getTracks().size(), 0);
    }
}
