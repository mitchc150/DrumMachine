package ui.gui;

import model.DrumTrackList;
import model.Instrument;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.*;

// Drum machine GUI application
public class DrumMachineGui {
    private static final String JSON_STORE = "./data/tracks.json";
    private static final int DEFAULT_BPM = 90;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final Color BOTTOM_COLOR = Color.GRAY;
    private static final Color TEXT_COLOR = Color.WHITE;
    private static final Font FONT = new Font("Arial", Font.ITALIC, 15);

    private final JFrame frame;
    private JPanel topPanel;
    private JPanel bottomPanel;

    private final List<ToggleButtonRow> toggleButtonRows;

    private DrumTrackList tracks;
    private Sequencer sequencer;
    private final JsonWriter jsonWriter;
    private final JsonReader jsonReader;

    // MODIFIES: this
    // EFFECTS: constructs a new DrumMachineGui with empty tracklist of BPM 90; initializes panels
    public DrumMachineGui() throws InvalidMidiDataException, MidiUnavailableException, IOException {
        // Initialize empty DrumTrackList with default bpm & sequencer which will play it
        tracks = new DrumTrackList(DEFAULT_BPM);
        initSequencer(tracks);

        // Initialize reader & writer to save and load files
        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);

        // Create list of rows of toggle buttons to represent instruments
        toggleButtonRows = new ArrayList<>();

        // Set up new frame with border layout
        frame = new JFrame("Drum Machine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());

        // Initialize the three panels which make up the gui, and add them to the frame
        initializeTopPanel();
        initializeRightPanel();
        initializeBottomPanel();

        // Make frame visible
        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: creates a new top panel, which will host ToggleButtonRows representing instruments
    private void initializeTopPanel() {
        // Create new panel with GridLayout; there will be a single column, which is populated with an arbitrary number
        // of ToggleButtonRows
        topPanel = new JPanel(new GridLayout(0, 1, 10, 10));

        // Set the panel's color to the default
        topPanel.setBackground(BACKGROUND_COLOR);

        // Add the panel to our main frame
        frame.add(topPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: creates a right panel which has the image of the MIDI code legend
    // image sources:
    //   - drum machine: http://www.generaldiscussion.us/general/808-drum-machines.html
    //   - drum kit: https://purepng.com/photo/10462/objects-primer-drums-kit
    //   - midi legend: https://computermusicresource.com/GM.Percussion.KeyMap.html
    private void initializeRightPanel() throws IOException {
        // Create right panel with default layout. This panel will contain the image
        JPanel rightPanel = new JPanel();

        // Read the image and resize so that it fits on the screen
        Image image = ImageIO.read(new File("./src/main/ui/gui/drum machine legend.PNG"));
        image = image.getScaledInstance(700, 840, Image.SCALE_SMOOTH);

        // Load image into an icon, then add it to a label and add the label to the panel.
        ImageIcon imageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(imageIcon);
        rightPanel.add(imageLabel);

        // Add our finished panel to the frame
        frame.add(rightPanel, BorderLayout.EAST);
    }

    // MODIFIES: this
    // EFFECTS: creates bottom panel with buttons
    private void initializeBottomPanel() {
        // Create new bottom panel to house the buttons which perform global actions
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(BOTTOM_COLOR);

        // Create the buttons and add them to the panel
        initializeAddRowButton();
        initializePlayButton();
        initializeStopButton();
        initializeLoadButton();
        initializeSaveButton();

        // Add the bottom panel to our frame
        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    // MODIFIES: this
    // EFFECTS: creates a button to add new rows/instruments
    private void initializeAddRowButton() {
        // Create button to add instruments with
        //   - text "Add Instrument",
        //   - yellow color,
        //   - default text color and font,
        //   - no border or focus painting,
        //   - preferred size which matches the size of the text
        JButton addRowButton = new JButton("Add Instrument");
        addRowButton.setBackground(new Color(241, 196, 15));
        addRowButton.setForeground(TEXT_COLOR);
        addRowButton.setFocusPainted(false);
        addRowButton.setBorderPainted(false);
        addRowButton.setFont(FONT);
        addRowButton.setPreferredSize(new Dimension(150, 70));

        // lambda action listener for addNewRow function defined elsewhere
        addRowButton.addActionListener(e -> addNewRow());

        // add the button to the bottom panel
        bottomPanel.add(addRowButton);
    }

    // MODIFIES: this
    // EFFECTS: creates a play button
    private void initializePlayButton() {
        // Create button to play the beat with
        //   - text "Play",
        //   - green color,
        //   - default text color and font,
        //   - no border or focus painting,
        //   - preferred size which matches the size of the text
        JButton playButton = new JButton("Play");
        playButton.setBackground(new Color(46, 204, 113));
        playButton.setForeground(TEXT_COLOR);
        playButton.setFocusPainted(false);
        playButton.setBorderPainted(false);
        playButton.setFont(FONT);
        playButton.setPreferredSize(new Dimension(70, 70));

        // create lambda action listener for playButtonClicked function defined elsewhere; throw exception if
        // playButtonClicked does
        playButton.addActionListener(e -> {
            try {
                playButtonClicked();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // add the play button to the bottom panel
        bottomPanel.add(playButton);
    }

    // MODIFIES: this
    // EFFECTS: creates a stop button
    private void initializeStopButton() {
        // Create button to stop the beat with
        //   - text "Stop",
        //   - red color,
        //   - default text color and font,
        //   - no border or focus painting,
        //   - preferred size which matches the size of the text
        JButton stopButton = new JButton("Stop");
        stopButton.setBackground(new Color(192, 57, 43));
        stopButton.setForeground(TEXT_COLOR);
        stopButton.setFocusPainted(false);
        stopButton.setBorderPainted(false);
        stopButton.setFont(FONT);
        stopButton.setPreferredSize(new Dimension(70, 70));

        // create lambda action listener for stopButtonClicked defined elsewhere
        stopButton.addActionListener(e -> stopButtonClicked());

        // add the stop button to the bottom panel
        bottomPanel.add(stopButton);
    }

    // MODIFIES: this
    // EFFECTS: creates a load button
    private void initializeLoadButton() {
        // Create button to load a previously saved beat with
        //   - text "Load",
        //   - blue color,
        //   - default text color and font,
        //   - no border or focus painting,
        //   - preferred size which matches the size of the text
        JButton loadButton = new JButton("Load");
        loadButton.setBackground(new Color(6, 66, 124));
        loadButton.setForeground(TEXT_COLOR);
        loadButton.setFocusPainted(false);
        loadButton.setBorderPainted(false);
        loadButton.setFont(FONT);
        loadButton.setPreferredSize(new Dimension(70, 70));

        // Create lambda action listener for load function defined elsewhere; throw exception iff load does
        loadButton.addActionListener(e -> {
            try {
                load();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });

        // add the button to our bottom panel
        bottomPanel.add(loadButton);
    }

    // MODIFIES: this
    // EFFECTS: creates a save button
    private void initializeSaveButton() {
        // Create button to save the file with
        //   - text "Save",
        //   - yellow color,
        //   - default text color and font,
        //   - no border or focus painting,
        //   - preferred size which matches the size of the text
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(245, 96, 10));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setFont(FONT);
        saveButton.setPreferredSize(new Dimension(70, 70));

        // Create lambda action listener for save method; throw exception iff save cannot find the right file
        saveButton.addActionListener(e -> {
            try {
                save();
            } catch (FileNotFoundException ex) {
                throw new RuntimeException(ex);
            }
        });
        bottomPanel.add(saveButton);
    }

    // MODIFIES: this
    // EFFECTS: adds a new row to the screen
    private void addNewRow() {
        // create a new row representing an instrument which will be situated on this gui
        ToggleButtonRow row = new ToggleButtonRow(this);

        // add the new row to our list of toggle button rows so that we can access and modify its state
        toggleButtonRows.add(row);

        // prompt the user to choose an instrument number for the row
        row.changeInstrumentNumber();

        // add the row to the top panel, revalidate and repaint
        topPanel.add(row);
        topPanel.revalidate();
        topPanel.repaint();
    }

    // MODIFIES: this
    // EFFECTS: loads track list from buttons and plays it
    private void playButtonClicked() throws Exception {
        // stop the sequencer if it's already playing, so that we don't have multiple tracks playing over each other
        sequencer.stop();

        // create a new drum track list with default bpm
        tracks = new DrumTrackList(DEFAULT_BPM);

        // create a new instrument for each toggle button row, where
        //   a) instrumentNumber is from the row's local data;
        //   b) instrumentNotes is a string built of "x" and "-" determined by whether a given row's buttons are
        //      pressed or not
        for (ToggleButtonRow row : toggleButtonRows) {
            Instrument instrument = new Instrument(row.getInstrumentNumber(),
                    row.getToggleButtonString());
            tracks.addTrack(instrument);
        }

        // load the new track list into the sequencer and begin playing
        initSequencer(tracks);
        sequencer.start();
    }

    // MODIFIES: this
    // EFFECTS: stops playback
    private void stopButtonClicked() {
        sequencer.stop();
    }

    // MODIFIES: this
    // EFFECTS: deletes all rows
    public void deleteAllRows() {
        for (ToggleButtonRow row : toggleButtonRows) {
            topPanel.remove(row);
        }
        toggleButtonRows.clear();
        topPanel.revalidate();
        topPanel.repaint();
    }

    // MODIFIES: this, toggleButtonRows
    // EFFECTS: loads from a saved JSON file
    private void load() throws Exception {
        // delete all rows so that we don't end up with extra ones
        deleteAllRows();

        // read the track list and write it, with each track in the list represented by a toggle button row
        tracks = jsonReader.read();
        updateToggleButtons();
    }

    // MODIFIES: this
    // EFFECTS: saves to a JSON file
    private void save() throws FileNotFoundException {
        jsonWriter.open();
        jsonWriter.write(tracks);
        jsonWriter.close();
    }

    // MODIFIES: this
    // EFFECTS: updates the state of ToggleButtonRows to match tracks
    private void updateToggleButtons() {
        deleteAllRows();
        for (int i = 0; i < tracks.getInstruments().size(); i++) {
            // get the instrument notes and number
            String buttonString = tracks.getInstruments().get(i).getInstrumentNotesString();
            int instrumentNumber = tracks.getInstruments().get(i).getInstrumentNumber();

            // create a new toggle button row which will be situated on this gui, add it to this panel
            ToggleButtonRow newRow = new ToggleButtonRow(this);
            topPanel.add(newRow);

            // set the instrument number and button status on the new row
            newRow.setInstrumentNumber(instrumentNumber);
            newRow.updateButtons(buttonString);

            // add the new row to list of toggle button rows
            toggleButtonRows.add(newRow);

        }
        // revalidate and repaint top panel
        topPanel.revalidate();
        topPanel.repaint();
    }

    // MODIFIES: this
    // EFFECTS: removes a row from the screen
    public void removeRow(ToggleButtonRow row) {
        // remove row from the panel
        topPanel.remove(row);
        toggleButtonRows.remove(row);

        // revalidate and repaint top panel
        topPanel.revalidate();
        topPanel.repaint();
    }

    // MODIFIES: this
    // EFFECTS: initializes the sequencer with updated tracks
    private void initSequencer(DrumTrackList tracks) throws MidiUnavailableException, InvalidMidiDataException {
        // Initialize and open a new sequencer
        sequencer = MidiSystem.getSequencer();
        sequencer.open();

        // Set the sequencer BPM to the track list BPM
        sequencer.setTempoInBPM(tracks.getBPM());

        // Set sequencer to loop
        sequencer.setLoopCount(Sequencer.LOOP_CONTINUOUSLY);

        // Set sequencer to play the sequence defined by track list
        sequencer.setSequence(tracks.getSequence());
    }

    // EFFECTS: returns the frame
    public JFrame getFrame() {
        return frame;
    }
}
