package ui;

import model.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final List<String> INSTRUMENT_LIST = Arrays.asList("Acoustic Bass Drum",
            "Bass Drum 1", "Side Stick", "Acoustic Snare", "Hand Clap", "Electric Snare",
            "Low Floor Tom", "Closed Hi-Hat", "High Floor Tom", "Pedal Hi-Hat", "Open Hi-Hat",
            "Low Mid-Tom", "Hi-Mid Tom", "Crash Cymbal 1", "High Tom", "Ride Cymbal 1",
            "Chinese Cymbal", "Ride Bell", "Tambourine", "Splash Cymbal", "Cowbell",
            "Crash Cymbal 2", "Vibraslap", "Ride Cymbal 2", "Hi Bongo", "Low Bongo",
            "Mute Hi Conga", "Open Hi Conga", "Low Conga", "High Timbale", "Low Timbale",
            "High Agogo", "Low Agogo", "Cabasa", "Maracas", "Short Whistle", "Long Whistle",
            "Short Guiro", "Long Guiro", "Claves", "Hi Wood Block", "Low Wood Block",
            "Mute Cuica", "Open Cuica", "Mute Triangle", "Open Triangle");

    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException {
        DrumTrackList tracks = addInstrumentLoop();
        DrumSequencer sequence = new DrumSequencer(80, tracks);
        sequence.playSequencer();
        printTrackInfo(sequence);
        while (true) {
            System.out.println("Enter 'stop' to stop the recording");
            String response = input.nextLine();
            if (response.equals("stop")) {
                sequence.stopSequencer();
                handleStop();
            }
        }
    }

    public static void handleStop() {
        System.out.println("Enter 'add tracks' to add a new track to the song. Enter 'change bpm' to change the BPM."
                + "enter 'quit' to quit.");
        String choice = input.nextLine();
    }

    public static DrumTrackList addInstrumentLoop() throws InvalidMidiDataException {
        DrumTrackList tracks = new DrumTrackList();
        while (true) {
            tracks.addTrack(addInstrument());
            System.out.println("Enter 'continue' to add more instruments, or 'done' to play the loop");
            String response = input.nextLine();
            if (response.equals("done")) {
                return tracks;
            }
        }
    }

    public static Instrument addInstrument() {

        System.out.println("Choose an instrument to play. Enter 'done' to stop:");

            int i = 1;

            for (String str : INSTRUMENT_LIST) {
                if (i % 5 != 1) {
                    System.out.print(str + "          ");
                } else {
                    System.out.println(str);
                }
                i += 1;
            }

            String instrumentChoice = input.nextLine();
            int instrumentNum = INSTRUMENT_LIST.indexOf(instrumentChoice) + 36;
            System.out.println("Enter what you would like the instrument to play. 'x' is a note, and '-' is a rest.");
            String noteChoice = input.nextLine();

            Instrument choice = new Instrument(instrumentNum, noteChoice);
            return choice;
    }

    public static void printTrackInfo(DrumSequencer sequence) {
        DrumTrackList tracks = sequence.getTrackList();
        System.out.println("BPM: " + sequence.getBeatsPerMinute());
        for (Instrument instrument: tracks.getInstruments()) {
            String instrumentName = INSTRUMENT_LIST.get(instrument.getInstrumentNumber()-37);
            String instrumentNotes = String.valueOf(instrument.getInstrumentNotes());
            System.out.println(instrumentName + " is playing " + instrumentNotes);
        }
    }
}
