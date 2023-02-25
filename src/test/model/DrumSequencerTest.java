package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;

import static org.junit.jupiter.api.Assertions.*;

public class DrumSequencerTest {

    DrumTrackList tracks;
    DrumSequencer sequence;
    Instrument instrument;

    @BeforeEach
    public void init() throws InvalidMidiDataException, MidiUnavailableException {
        instrument = new Instrument(35, "x-x-");
        tracks = new DrumTrackList();
        sequence = new DrumSequencer(120, tracks);
    }

    @Test
    public void constructorTest() throws MidiUnavailableException, InvalidMidiDataException {
        sequence = new DrumSequencer(120, tracks);
        assertEquals(sequence.getTrackList(), tracks);
        assertTrue(sequence.getSequencer().isOpen());
        assertEquals(sequence.getSequencer().getTempoInBPM(), 120);
        assertEquals(sequence.getSequencer().getLoopCount(), Sequencer.LOOP_CONTINUOUSLY);
        assertEquals(sequence.getSequencer().getSequence(), tracks.getSequence());
    }

    @Test
    public void setBPMTest() {
        sequence.setBeatsPerMinute(12);
        assertEquals(sequence.getBeatsPerMinute(), 12);
        assertEquals(sequence.getSequencer().getTempoInBPM(), 12);
    }

    @Test
    public void playTest() {
        sequence.playSequencer();
        Sequencer sequencer = sequence.getSequencer();
        assertTrue(sequencer.isRunning());
        sequence.stopSequencer();
    }

    @Test
    public void stopTest() {
        sequence.playSequencer();
        sequence.stopSequencer();
        Sequencer sequencer = sequence.getSequencer();
        assertFalse(sequencer.isRunning());
    }

    @Test
    public void addMoreTracksTest() throws Exception {
        assertEquals(sequence.getTrackList(), tracks);
        tracks.addTrack(instrument);
        assertEquals(sequence.getTrackList(), tracks);
    }
}
