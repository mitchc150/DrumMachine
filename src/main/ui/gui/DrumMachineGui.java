package ui.gui;

import model.DrumTrackList;
import model.Instrument;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;
import javax.sound.midi.Sequencer;
import javax.swing.*;

// Drum machine GUI application
public class DrumMachineGui {
    private static final int INSTRUMENT_DEFAULT = 35;

    private JFrame frame;
    private JPanel topPanel;
    private JPanel bottomPanel;

    private List<ToggleButtonRow> toggleButtonRows;

    private DrumTrackList tracks;
    private Sequencer sequencer;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private static final String JSON_STORE = "./data/tracks.json";

    // MODIFIES: this
    // EFFECTS: constructs a new DrumMachineGui with empty tracklist of BPM 90; initializes panels
    public DrumMachineGui() throws InvalidMidiDataException, MidiUnavailableException, IOException {
        tracks = new DrumTrackList(90);
        initSequencer(tracks);

        jsonReader = new JsonReader(JSON_STORE);
        jsonWriter = new JsonWriter(JSON_STORE);

        toggleButtonRows = new ArrayList<>();

        frame = new JFrame("Drum Machine");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(600, 400));
        frame.setLayout(new BorderLayout());

        initializeTopPanel();
        initializeRightPanel();
        initializeBottomPanel();

        frame.setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: creates a new top panel, which will host ToggleButtonRows representing instruments
    private void initializeTopPanel() {
        topPanel = new JPanel(new GridLayout(0, 1, 10, 10));
        topPanel.setBackground(new Color(46, 49, 49));
        frame.add(topPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: creates a right panel which has the image of the MIDI code legend
    // image sources:
    //   - drum machine: http://www.generaldiscussion.us/general/808-drum-machines.html
    //   - drum kit: https://purepng.com/photo/10462/objects-primer-drums-kit
    private void initializeRightPanel() throws IOException {
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        Image image = ImageIO.read(new File("./src/main/ui/gui/drum machine legend.PNG"));
        image = image.getScaledInstance(700, 840, Image.SCALE_SMOOTH);
        ImageIcon imageIcon = new ImageIcon(image);
        JLabel imageLabel = new JLabel(imageIcon);
        rightPanel.add(imageLabel);
        frame.add(rightPanel, BorderLayout.EAST);
    }

    // MODIFIES: this
    // EFFECTS: creates bottom panel with buttons
    private void initializeBottomPanel() {
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        bottomPanel.setBackground(new Color(46, 49, 49));
        frame.add(bottomPanel, BorderLayout.SOUTH);

        initializeAddRowButton();
        initializePlayButton();
        initializeStopButton();
        initializeLoadButton();
        initializeSaveButton();
    }

    // MODIFIES: this
    // EFFECTS: creates a button for us to add new rows/instruments
    private void initializeAddRowButton() {
        JButton addRowButton = new JButton("Add Instrument");
        addRowButton.setBackground(new Color(241, 196, 15));
        addRowButton.setForeground(Color.WHITE);
        addRowButton.setFocusPainted(false);
        addRowButton.setBorderPainted(false);
        addRowButton.setFont(new Font("Arial", Font.PLAIN, 15));
        addRowButton.setPreferredSize(new Dimension(150, 70));
        addRowButton.addActionListener(e -> addNewRow());
        bottomPanel.add(addRowButton);
    }

    // MODIFIES: this
    // EFFECTS: creates a play button
    private void initializePlayButton() {
        JButton playButton = new JButton("Play");
        playButton.setBackground(new Color(46, 204, 113));
        playButton.setForeground(Color.WHITE);
        playButton.setFocusPainted(false);
        playButton.setBorderPainted(false);
        playButton.setFont(new Font("Arial", Font.PLAIN, 15));
        playButton.setPreferredSize(new Dimension(70, 70));
        playButton.addActionListener(e -> {
            try {
                playButtonClicked();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        bottomPanel.add(playButton);
    }

    // MODIFIES: this
    // EFFECTS: creates a stop button
    private void initializeStopButton() {
        JButton stopButton = new JButton("Stop");
        stopButton.setBackground(new Color(192, 57, 43));
        stopButton.setForeground(Color.WHITE);
        stopButton.setFocusPainted(false);
        stopButton.setBorderPainted(false);
        stopButton.setFont(new Font("Arial", Font.PLAIN, 15));
        stopButton.setPreferredSize(new Dimension(70, 70));
        stopButton.addActionListener(e -> stopButtonClicked());
        bottomPanel.add(stopButton);
    }

    // MODIFIES: this
    // EFFECTS: creates a load button
    private void initializeLoadButton() {
        JButton loadButton = new JButton("Load");
        loadButton.setBackground(new Color(4, 86, 83));
        loadButton.setForeground(Color.WHITE);
        loadButton.setFocusPainted(false);
        loadButton.setBorderPainted(false);
        loadButton.setFont(new Font("Arial", Font.PLAIN, 15));
        loadButton.setPreferredSize(new Dimension(70, 70));
        loadButton.addActionListener(e -> {
            try {
                load();
            } catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        });
        bottomPanel.add(loadButton);
    }

    // MODIFIES: this
    // EFFECTS: creates a save button
    private void initializeSaveButton() {
        JButton saveButton = new JButton("Save");
        saveButton.setBackground(new Color(245, 96, 10));
        saveButton.setForeground(Color.WHITE);
        saveButton.setFocusPainted(false);
        saveButton.setBorderPainted(false);
        saveButton.setFont(new Font("Arial", Font.PLAIN, 15));
        saveButton.setPreferredSize(new Dimension(70, 70));
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
        ToggleButtonRow row = new ToggleButtonRow();
        topPanel.add(row);
        topPanel.revalidate();
        toggleButtonRows.add(row);
    }

    // MODIFIES: this
    // EFFECTS: loads track list from buttons and plays it
    private void playButtonClicked() throws Exception {
        sequencer.stop();
        tracks = new DrumTrackList(90);
        for (ToggleButtonRow row : toggleButtonRows) {
            Instrument instrument = new Instrument(row.getInstrumentNumber(),
                    row.getToggleButtonString());
            tracks.addTrack(instrument);
        }
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

    // MODIFIES: this
    // EFFECTS: loads from a saved JSON file
    private void load() throws Exception {
        deleteAllRows();
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
            String buttonString = tracks.getInstruments().get(i).getInstrumentNotesString();
            ToggleButtonRow newRow = new ToggleButtonRow();
            topPanel.add(newRow);
            toggleButtonRows.add(newRow);
            newRow.updateButtons(buttonString);
        }
        topPanel.revalidate();
        topPanel.repaint();
    }

    // MODIFIES: this
    // EFFECTS: removes a row from the screen
    public void removeRow(ToggleButtonRow row) {
        topPanel.remove(row);
        toggleButtonRows.remove(row);
        topPanel.revalidate();
        topPanel.repaint();
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

    // Represents the row of JToggleButtons used to change what the instrument plays
    private class ToggleButtonRow extends JPanel {
        private final List<String> instrumentList = Arrays.asList("Acoustic Bass Drum",
                "Bass Drum 1", "Side Stick", "Acoustic Snare", "Hand Clap", "Electric Snare",
                "Low Floor Tom", "Closed Hi-Hat", "High Floor Tom", "Pedal Hi-Hat", "Low Tom",
                "Open Hi-Hat", "Low Mid-Tom", "Hi-Mid Tom", "Crash Cymbal 1", "High Tom", "Ride Cymbal 1",
                "Chinese Cymbal", "Ride Bell", "Tambourine", "Splash Cymbal", "Cowbell",
                "Crash Cymbal 2", "Vibraslap", "Ride Cymbal 2", "Hi Bongo", "Low Bongo",
                "Mute Hi Conga", "Open Hi Conga", "Low Conga", "High Timbale", "Low Timbale",
                "High Agogo", "Low Agogo", "Cabasa", "Maracas", "Short Whistle", "Long Whistle",
                "Short Guiro", "Long Guiro", "Claves", "Hi Wood Block", "Low Wood Block",
                "Mute Cuica", "Open Cuica", "Mute Triangle", "Open Triangle");

        private static final int BUTTON_WIDTH = 60;
        private static final int BUTTON_HEIGHT = 60;
        private static final int ROW_HEIGHT = 80;

        private Color backgroundColor = new Color(46, 49, 49);
        private Color foregroundColor = Color.WHITE;

        private int instrumentNumber;
        private List<JToggleButton> toggleButtons;
        private JButton changeInstrumentButton;
        private JButton removeButton;
        private JLabel instrumentName;

        // MODIFIES: this
        // EFFECTS: creates an empty ToggleButtonRow with the default instrument and no notes selected to play
        public ToggleButtonRow() {
            instrumentNumber = INSTRUMENT_DEFAULT;
            toggleButtons = new ArrayList<>();

            setPreferredSize(new Dimension(400, ROW_HEIGHT));
            setBackground(backgroundColor);
            setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

            initializeInstrumentName();
            initializeToggleButtonRow();
            initializeRemoveButton();
            initializeChangeInstrumentButton();
        }

        // MODIFIES: this
        // EFFECTS: creates a JLabel to display the instrument's name
        public void initializeInstrumentName() {
            instrumentName = new JLabel(instrumentList.get(instrumentNumber - 35));
            instrumentName.setForeground(foregroundColor);
            instrumentName.setAlignmentX(Component.CENTER_ALIGNMENT);
            instrumentName.setFont(new Font("Arial", Font.PLAIN, 15));
            instrumentName.setPreferredSize(new Dimension(200, instrumentName.getPreferredSize().height));
            add(instrumentName);
        }

        // MODIFIES: this
        // EFFECTS: creates a row of 8 toggle buttons
        public void initializeToggleButtonRow() {
            for (int i = 0; i < 8; i++) {
                JToggleButton toggleButton = new JToggleButton("-");
                toggleButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
                toggleButton.setBackground(backgroundColor);
                toggleButton.setForeground(foregroundColor);
                add(toggleButton);
                toggleButtons.add(toggleButton);
            }
        }

        // MODIFIES: this
        // EFFECTS: creates a button to remove the row/instrument
        public void initializeRemoveButton() {
            removeButton = new JButton("Remove");
            removeButton.setBackground(new Color(105, 15, 241));
            removeButton.setForeground(foregroundColor);
            removeButton.setFocusPainted(false);
            removeButton.setBorderPainted(false);
            removeButton.setFont(new Font("Arial", Font.PLAIN, 12));
            removeButton.setPreferredSize(new Dimension(120, 30));
            removeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    removeRow(ToggleButtonRow.this);
                }
            });
            add(removeButton);
        }

        // MODIFIES: this
        // EFFECTS: creates a button to change the instrument
        public void initializeChangeInstrumentButton() {
            changeInstrumentButton = new JButton("Change Instrument");
            changeInstrumentButton.setBackground(new Color(241, 15, 203));
            changeInstrumentButton.setForeground(foregroundColor);
            changeInstrumentButton.setFocusPainted(false);
            changeInstrumentButton.setBorderPainted(false);
            changeInstrumentButton.setFont(new Font("Arial", Font.PLAIN, 12));
            changeInstrumentButton.setPreferredSize(new Dimension(150, 30));
            changeInstrumentButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int newInstrumentNumber = Integer.parseInt(
                            JOptionPane.showInputDialog(frame, "Enter new instrument number:"));
                    instrumentNumber = newInstrumentNumber;
                    instrumentName.setText(instrumentList.get(instrumentNumber - 35));
                    validate();
                    repaint();
                }
            });
            add(changeInstrumentButton);
        }

        // EFFECTS: returns the instrument number of the given row
        public int getInstrumentNumber() {
            return instrumentNumber;
        }

        // REQUIRES: buttonString is a string of length 8, made of 'x' or '-' characters
        // MODIFIES: this
        // EFFECTS: given a string, creates the button row which represents it
        public void updateButtons(String buttonString) {
            for (int i = 0; i < buttonString.length(); i++) {
                char c = buttonString.charAt(i);
                toggleButtons.get(i).setSelected(c == 'x');
            }
        }

        // EFFECTS: returns the string which represents a given toggle button row
        public String getToggleButtonString() {
            StringBuilder sb = new StringBuilder();
            for (JToggleButton toggleButton : toggleButtons) {
                sb.append(toggleButton.isSelected() ? "x" : "-");
            }
            return sb.toString();
        }
    }
}
