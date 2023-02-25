package ui;

import model.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class DrumMachineApp {
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

    private DrumTrackList tracks;
    private DrumSequencer sequence;
    private Scanner input;
    private int bpm;

    // EFFECTS: runs the drum machine application
    public DrumMachineApp() throws Exception {
        runDrumMachine();
    }


    private void runDrumMachine() throws Exception {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.next();
            command = command.toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes the sequencer, asking for user input for the BPM
    private void init() throws MidiUnavailableException, InvalidMidiDataException {
        input = new Scanner(System.in);
        tracks = new DrumTrackList();

        System.out.println("Enter the bpm you would like for the drum machine");
        bpm = Integer.parseInt(input.nextLine());
        sequence = new DrumSequencer(bpm, tracks);
    }

    // EFFECTS: displays the user's options
    private void displayMenu() {
        printBPM();
        printTrackList();
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add tracks");
        System.out.println("\tr -> remove tracks");
        System.out.println("\tp -> play the loop");
        System.out.println("\ts -> stop the loop");
        System.out.println("\tb -> change the bpm");
        System.out.println("\tq -> quit");
    }

    // MODIFIES: this
    // EFFECTS: processes user's command
    private void processCommand(String command) throws Exception {
        switch (command) {
            case "a":
                stopLoop();
                addTrack();
                break;
            case "r":
                stopLoop();
                removeTrack();
                break;
            case "p":
                playLoop();
                break;
            case "s":
                stopLoop();
                break;
            case "b":
                stopLoop();
                changeBPM();
                break;
            default:
                System.out.println("Selection not valid");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds track to the loop
    public void addTrack() throws Exception {

        int i = 1;

        for (String str : INSTRUMENT_LIST) {
            if (i % 5 != 1) {
                System.out.print(str + "          ");
            } else {
                System.out.println(str);
            }
            i += 1;
        }
        input.nextLine();

        System.out.println("Choose an instrument to play");
        String instrumentChoice = input.nextLine();

        int instrumentNum = INSTRUMENT_LIST.indexOf(instrumentChoice) + 35;

        System.out.println("Enter what you would like the instrument to play. 'x' is a note, and '-' is a rest.");
        String noteChoice = input.nextLine();

        Instrument choice = new Instrument(instrumentNum, noteChoice);

        tracks.addTrack(choice);
        sequence = new DrumSequencer(bpm, tracks);
    }

    // MODIFIES: this
    // EFFECTS: removes a track from the loop
    private void removeTrack() throws MidiUnavailableException, InvalidMidiDataException {
        System.out.println("Which track would you like to remove? Please type the instrument number.");
        printTrackList();
        input.nextLine();
        int removeChoice = Integer.parseInt(input.nextLine()) - 1;
        System.out.println("Removing instrument " + removeChoice + " from the tracklist.");
        tracks.removeTrack(removeChoice);
        sequence = new DrumSequencer(bpm, tracks);
    }

    // MODIFIES: this
    // EFFECTS: plays the drum loop
    private void playLoop()  {
        sequence.playSequencer();
    }

    // MODIFIES: this
    // EFFECTS: stops the drum loop
    private void stopLoop() {
        sequence.stopSequencer();
    }

    // MODIFIES: this
    // EFFECTS: change the BPM of the drum loop
    private void changeBPM() {
        System.out.println("Current BPM is " + bpm + "\n");
        System.out.println("Enter the new value you would like for the BPM: ");
        this.bpm = Integer.parseInt(input.nextLine());
        sequence.setBeatsPerMinute(bpm);
    }

    // EFFECTS: print information about the tracks, including (a) number, (b) midi code, (c) name, and (d) notes
    private void printTrackList() {
        for (int i = 0; i < tracks.getInstruments().size(); i++) {
            System.out.println("Instrument number: " + (i + 1));
            System.out.println("MIDI code of instrument: " + tracks.getInstruments().get(i).getInstrumentNumber());
            System.out.println("Instrument name: " + INSTRUMENT_LIST.get(i + 35));
            System.out.println("Instrument notes: "
                    + Arrays.toString(tracks.getInstruments().get(i).getInstrumentNotes()) + "\n");
        }
    }

    // EFFECTS: print information about BPM
    private void printBPM() {
        System.out.println("BPM: " + bpm);
    }
}
