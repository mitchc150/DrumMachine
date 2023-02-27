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
    Instrument instrument3;
    DrumTrackList drumTrackList;

    @BeforeEach
    public void init() throws InvalidMidiDataException {
        instrument1 = new Instrument(45, "x-x-x-x-x-x");
        instrument2 = new Instrument(35, "-x-x-x-x-x-x");
        instrument3 = new Instrument(60, "------------");
        drumTrackList = new DrumTrackList(120);
    }

    @Test
    public void constructorTest() {
        assertEquals(drumTrackList.getSequence().getDivisionType(), Sequence.PPQ);
        assertEquals(drumTrackList.getSequence().getResolution(), 4);
        assertTrue(drumTrackList.getTracks().isEmpty());
        assertTrue(drumTrackList.getInstruments().isEmpty());
        assertEquals(drumTrackList.getBPM(), 120);
    }

    @Test
    public void setBPMTest() {
        drumTrackList.setBPM(90);
        assertEquals(drumTrackList.getBPM(), 90);
    }

    @Test
    public void addTrackTest() throws Exception {
        drumTrackList.addTrack(instrument1);
        assertEquals(drumTrackList.getInstruments().get(0), instrument1);

        drumTrackList.addTrack(instrument2);
        assertEquals(drumTrackList.getInstruments().get(1), instrument2);

        drumTrackList.addTrack(instrument3);
        assertEquals(drumTrackList.getInstruments().get(2), instrument3);
    }

    @Test
    public void removeTrackTest() throws Exception {
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
