package ui;

import model.*;
import model.Instrument;

import javax.sound.midi.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

// Drum machine application
// MODELLED ON TELLER APPLICATION FROM edX MODULE
public class DrumMachineApp {
    private static final List<String> INSTRUMENT_LIST = Arrays.asList("Acoustic Bass Drum",
            "Bass Drum 1", "Side Stick", "Acoustic Snare", "Hand Clap", "Electric Snare",
            "Low Floor Tom", "Closed Hi-Hat", "High Floor Tom", "Pedal Hi-Hat", "Low Tom",
            "Open Hi-Hat", "Low Mid-Tom", "Hi-Mid Tom", "Crash Cymbal 1", "High Tom", "Ride Cymbal 1",
            "Chinese Cymbal", "Ride Bell", "Tambourine", "Splash Cymbal", "Cowbell",
            "Crash Cymbal 2", "Vibraslap", "Ride Cymbal 2", "Hi Bongo", "Low Bongo",
            "Mute Hi Conga", "Open Hi Conga", "Low Conga", "High Timbale", "Low Timbale",
            "High Agogo", "Low Agogo", "Cabasa", "Maracas", "Short Whistle", "Long Whistle",
            "Short Guiro", "Long Guiro", "Claves", "Hi Wood Block", "Low Wood Block",
            "Mute Cuica", "Open Cuica", "Mute Triangle", "Open Triangle");

    private DrumTrackList tracks;
    private Sequencer sequencer;
    private Scanner input;

    // EFFECTS: builds a new Drum Machine by running the application
    public DrumMachineApp() throws Exception {
        runDrumMachine();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runDrumMachine() throws Exception {
        boolean keepGoing = true;
        String command;

        init();

        while (keepGoing) {
            displayMenu();
            command = input.nextLine();
            command = command.toLowerCase();

            if (command.equals("q")) {
                System.out.println("Goodbye!");
                keepGoing = false;
                System.exit(0);
            } else {
                processCommand(command);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: initializes the sequencer, asking for user input for the BPM
    private void init() throws MidiUnavailableException, InvalidMidiDataException {
        input = new Scanner(System.in);
        System.out.println("Enter the bpm you would like for the drum machine");
        tracks = new DrumTrackList(Integer.parseInt(input.nextLine()));
        initSequencer(tracks);
    }

    // MODIFIES: this
    // EFFECTS: initializes the sequencer with updated tracks
    private void initSequencer(DrumTrackList tracks) throws MidiUnavailableException, InvalidMidiDataException {
        sequencer = MidiSystem.getSequencer();
        sequencer.open();
        sequencer.setTempoInBPM(tracks.getBPM());
        sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);
        sequencer.setSequence(tracks.getSequence());
    }

    // EFFECTS: displays the user's options
    private void displayMenu() {
        printBPM();
        printTrackList();
        System.out.println("\nSelect from:");
        System.out.println("\ta -> add a track");
        System.out.println("\tr -> remove a track");
        System.out.println("\tm -> modify a track");
        System.out.println("\tp -> play the loop");
        System.out.println("\ts -> stop the loop");
        System.out.println("\tb -> change the bpm");
        System.out.println("\tq -> quit");
    }

    // EFFECTS: processes user's commands
    private void processCommand(String command) throws Exception {
        switch (command) {
            case "m":
                changeTrack();
                break;
            case "a":
                addTrack();
                break;
            case "r":
                removeTrack();
                break;
            case "p":
                playLoop();
                break;
            case "s":
                stopLoop();
                break;
            case "b":
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
        stopLoop();
        int instrumentNum = addInstrumentNum();
        String instrumentNotes = addInstrumentNotes();
        Instrument choice = new Instrument(instrumentNum, instrumentNotes);
        tracks.addTrack(choice);
    }

    // EFFECTS: returns a number to create a new instrument
    public int addInstrumentNum() {
        int n = 1;
        for (String str : INSTRUMENT_LIST) {
            System.out.print(str);
            if ((n % 4) != 0) {
                for (int i = 0; i < (25 - str.length()); i++) {
                    System.out.print(" ");
                }
            } else {
                System.out.println(" ");
            }
            n += 1;
        }
        System.out.println("\n");
        System.out.println("Choose an instrument");
        String instrumentChoice = input.nextLine();
        return INSTRUMENT_LIST.indexOf(instrumentChoice) + 35;
    }

    // EFFECTS: returns notes to create a new instrument
    public String addInstrumentNotes() {
        System.out.println("Enter what you would like the instrument to play. 'x' is a note, and '-' is a rest.");
        return input.nextLine();
    }

    // MODIFIES: this
    // EFFECTS: removes a track from the loop
    private void removeTrack()  {
        stopLoop();
        System.out.println("Which track would you like to remove? Please type the instrument number.");
        printTrackList();
        int removeChoice = Integer.parseInt(input.nextLine());
        System.out.println("Removing instrument " + removeChoice + " from the tracklist.");
        tracks.removeTrack(removeChoice - 1);
    }

    // MODIFIES: this
    // EFFECTS: plays the drum loop
    private void playLoop() throws MidiUnavailableException, InvalidMidiDataException {
        initSequencer(this.tracks);
        sequencer.start();
    }

    // MODIFIES: this
    // EFFECTS: stops the drum loop
    private void stopLoop() {
        sequencer.stop();
    }

    // MODIFIES: this
    // EFFECTS: change the BPM of the drum loop
    private void changeBPM() {
        stopLoop();
        System.out.println("Current BPM is " + tracks.getBPM() + "\n");
        System.out.println("Enter the new value you would like for the BPM: ");
        tracks.setBPM(Integer.parseInt(input.nextLine()));
    }

    // MODIFIES: this
    // EFFECTS: prompts user to modify a given track
    private void changeTrack() throws Exception {
        stopLoop();
        System.out.println("Select the instrument number of the track you would like to modify:");
        printTrackList();
        int choice = Integer.parseInt(input.nextLine());
        System.out.println("You have selected instrument " + choice);
        Instrument instrumentToModify = tracks.getInstruments().get(choice - 1);
        tracks.removeTrack(choice - 1);
        System.out.println("Please choose what you would like to modify:");
        System.out.println("\ti for instrument type");
        System.out.println("\tn for instrument notes");
        String choice2 = input.nextLine();
        if (choice2.equals("i")) {
            int newNum = addInstrumentNum();
            instrumentToModify.setInstrument(newNum);
        } else {
            String newNotes = addInstrumentNotes();
            instrumentToModify.setInstrumentNotes(newNotes);
        }
        tracks.addTrack(instrumentToModify);
    }

    // EFFECTS: print information about the tracks, including (a) number, (b) midi code, (c) name, and (d) notes
    private void printTrackList() {
        System.out.println();
        System.out.println("INSTRUMENTS: ");
        int i = 1;
        for (Instrument instr : tracks.getInstruments()) {
            System.out.println();
            System.out.println("\tInstrument number: " + i);
            System.out.println("\tMIDI code of instrument: " + instr.getInstrumentNumber());
            System.out.println("\tInstrument name: " + instr.getInstrumentName());
            System.out.println("\tInstrument notes: " + Arrays.toString(instr.getInstrumentNotes()));
            i += 1;
        }
    }

    // EFFECTS: print information about BPM
    private void printBPM() {
        System.out.println("BPM: " + tracks.getBPM());
    }
}
