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
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
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
        topPanel.setBackground(new Color(0, 0, 0));
        frame.add(topPanel, BorderLayout.CENTER);
    }

    // MODIFIES: this
    // EFFECTS: creates a right panel which has the image of the MIDI code legend
    // image sources:
    //   - drum machine: http://www.generaldiscussion.us/general/808-drum-machines.html
    //   - drum kit: https://purepng.com/photo/10462/objects-primer-drums-kit
    //   - midi legend: https://computermusicresource.com/GM.Percussion.KeyMap.html
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
    // EFFECTS: creates a button to add new rows/instruments
    private void initializeAddRowButton() {
        JButton addRowButton = new JButton("Add Instrument");
        addRowButton.setBackground(new Color(241, 196, 15));
        addRowButton.setForeground(Color.WHITE);
        addRowButton.setFocusPainted(false);
        addRowButton.setBorderPainted(false);
        addRowButton.setFont(new Font("Arial", Font.ITALIC, 15));
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
        playButton.setFont(new Font("Arial", Font.ITALIC, 15));
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
        stopButton.setFont(new Font("Arial", Font.ITALIC, 15));
        stopButton.setPreferredSize(new Dimension(70, 70));
        stopButton.addActionListener(e -> stopButtonClicked());
        bottomPanel.add(stopButton);
    }

    // MODIFIES: this
    // EFFECTS: creates a load button
    private void initializeLoadButton() {
        JButton loadButton = new JButton("Load");
        loadButton.setBackground(new Color(6, 66, 124));
        loadButton.setForeground(Color.WHITE);
        loadButton.setFocusPainted(false);
        loadButton.setBorderPainted(false);
        loadButton.setFont(new Font("Arial", Font.ITALIC, 15));
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
        saveButton.setFont(new Font("Arial", Font.ITALIC, 15));
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
        ToggleButtonRow row = new ToggleButtonRow(this);
        row.changeInstrumentNumber();
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

    // MODIFIES: this, toggleButtonRows
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
            int instrumentNumber = tracks.getInstruments().get(i).getInstrumentNumber();
            ToggleButtonRow newRow = new ToggleButtonRow(this);
            topPanel.add(newRow);
            newRow.setInstrumentNumber(instrumentNumber);
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

    // EFFECTS: returns the frame
    public JFrame getFrame() {
        return frame;
    }
}
