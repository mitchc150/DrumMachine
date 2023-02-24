package model;

import javax.sound.midi.*;

public class DrumSequencer {
    private Sequencer sequencer;
    private DrumTrackList drumSequence;
    private int beatsPerMinute;

    public DrumSequencer(int beatsPerMinute, DrumTrackList trackList) throws MidiUnavailableException, InvalidMidiDataException {
        this.beatsPerMinute = beatsPerMinute;
        drumSequence = trackList;

        try {
            sequencer = MidiSystem.getSequencer();
            sequencer.open();
        } catch (MidiUnavailableException e) {
            throw new RuntimeException(e);
        }

        sequencer.setTempoInBPM(beatsPerMinute);
        sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        sequencer.setSequence(drumSequence.getSequence());
    }

    public void playSequencer() {
        sequencer.start();
    }

    public void stopSequencer() { sequencer.stop(); }

    public int getBeatsPerMinute() {
        return this.beatsPerMinute;
    }

    public void setBeatsPerMinute(int newBeatsPerMinute) {
        this.beatsPerMinute = newBeatsPerMinute;
    }

    public DrumTrackList getTrackList() { return this.drumSequence; }
}
