package ui.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Represents the row of JToggleButtons and information related to each instrument
public class ToggleButtonRow extends JPanel {
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
    private static final int INSTRUMENT_DEFAULT = 35;

    private DrumMachineGui gui;

    private static final Color BACKGROUND_COLOR = Color.black;
    private static final Color FONT_COLOR = Color.WHITE;

    private int instrumentNumber;
    private List<JToggleButton> toggleButtons;
    private JButton changeInstrumentButton;
    private JButton removeButton;
    private JLabel instrumentName;

    // MODIFIES: this
    // EFFECTS: creates an empty ToggleButtonRow with the selected instrument and no notes to play
    public ToggleButtonRow(DrumMachineGui gui) {
        this.gui = gui;
        instrumentNumber = INSTRUMENT_DEFAULT;
        toggleButtons = new ArrayList<>();

        setSize(new Dimension(400, ROW_HEIGHT));
        setBackground(BACKGROUND_COLOR);
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
        instrumentName.setForeground(FONT_COLOR);
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
            toggleButton.setBackground(BACKGROUND_COLOR);
            toggleButton.setForeground(FONT_COLOR);
            add(toggleButton);
            toggleButtons.add(toggleButton);
        }
    }

    // MODIFIES: this, this.ui
    // EFFECTS: creates a button to remove the row/instrument
    public void initializeRemoveButton() {
        removeButton = new JButton("Remove");
        removeButton.setBackground(new Color(105, 15, 241));
        removeButton.setForeground(FONT_COLOR);
        removeButton.setFocusPainted(false);
        removeButton.setBorderPainted(false);
        removeButton.setFont(new Font("Arial", Font.PLAIN, 12));
        removeButton.setPreferredSize(new Dimension(120, 30));
        removeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gui.removeRow(ToggleButtonRow.this);
            }
        });
        add(removeButton);
    }

    // MODIFIES: this
    // EFFECTS: creates a button to change the instrument
    public void initializeChangeInstrumentButton() {
        changeInstrumentButton = new JButton("Change Instrument");
        changeInstrumentButton.setBackground(new Color(241, 15, 203));
        changeInstrumentButton.setForeground(FONT_COLOR);
        changeInstrumentButton.setFocusPainted(false);
        changeInstrumentButton.setBorderPainted(false);
        changeInstrumentButton.setFont(new Font("Arial", Font.PLAIN, 12));
        changeInstrumentButton.setPreferredSize(new Dimension(150, 30));
        changeInstrumentButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                changeInstrumentNumber();
            }
        });
        add(changeInstrumentButton);
    }

    // MODIFIES: this
    // EFFECTS: changes the instrument number for a given row
    public void changeInstrumentNumber() {
        int newInstrumentNumber = Integer.parseInt(
                JOptionPane.showInputDialog(gui.getFrame(), "Enter new instrument number:"));
        setInstrumentNumber(newInstrumentNumber);
    }

    // MODIFIES: this
    // EFFECTS: changes the instrument number associated with the row and updates the text describing it
    public void setInstrumentNumber(int instrumentNumber) {
        this.instrumentNumber = instrumentNumber;
        instrumentName.setText(instrumentList.get(instrumentNumber - 35));
        validate();
        repaint();
    }

    // EFFECTS: returns the instrument number of the given row
    public int getInstrumentNumber() {
        return instrumentNumber;
    }

    // REQUIRES: buttonString is a string of length 8, made of 'x' or '-' characters; 35 <= number <= ??
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
            if (toggleButton.isSelected()) {
                sb.append("x");
            } else {
                sb.append("-");
            }
        }
        return sb.toString();
    }
}
