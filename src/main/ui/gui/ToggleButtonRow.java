package ui.gui;

import javax.swing.*;
import java.awt.*;
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

    private static final int BUTTON_WIDTH = 40;
    private static final int BUTTON_HEIGHT = 40;
    private static final int ROW_HEIGHT = 80;
    private static final int INSTRUMENT_DEFAULT = 35;
    private static final Font FONT = new Font("Arial", Font.PLAIN, 15);
    private static final Color BACKGROUND_COLOR = Color.black;
    private static final Color FONT_COLOR = Color.WHITE;
    private static final Color BUTTON_COLOR = Color.GRAY;

    private final DrumMachineGui gui;
    private int instrumentNumber;
    private final List<JToggleButton> toggleButtons;
    private JLabel instrumentName;

    // MODIFIES: this
    // EFFECTS: creates an empty ToggleButtonRow with the selected instrument and no notes to play
    public ToggleButtonRow(DrumMachineGui gui) {
        // the gui that the ToggleButtonRow is on
        this.gui = gui;

        // set the instrument number as default to avoid errors
        this.instrumentNumber = INSTRUMENT_DEFAULT;

        // create the list which will hold all of our ToggleButtons; necessary for looping
        toggleButtons = new ArrayList<>();

        // set size, background, and layout of the ToggleButtonRow
        setSize(new Dimension(400, ROW_HEIGHT));
        setBackground(BACKGROUND_COLOR);
        setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));

        // initialize the instrument name panel, the row of buttons, the remove button and the change instrument button
        initializeInstrumentName();
        initializeToggleButtonRow();
        initializeRemoveButton();
        initializeChangeInstrumentButton();
    }

    // MODIFIES: this
    // EFFECTS: creates a JLabel to display the instrument's name
    public void initializeInstrumentName() {
        // Get the instrument name from the list; remove 35 because instrument numbers begin at 35
        instrumentName = new JLabel(instrumentList.get(instrumentNumber - 35));

        // set font color, alignment, and size for the label
        instrumentName.setForeground(FONT_COLOR);
        instrumentName.setAlignmentX(Component.CENTER_ALIGNMENT);
        instrumentName.setFont(FONT);
        instrumentName.setPreferredSize(new Dimension(200, instrumentName.getPreferredSize().height));

        // add the instrument name panel to the toggle button row
        add(instrumentName);
    }

    // MODIFIES: this
    // EFFECTS: creates a row of 8 toggle buttons
    public void initializeToggleButtonRow() {
        // create 8 new JToggleButtons with default features
        for (int i = 0; i < 8; i++) {
            // text set to "-" to make it easier to perceive that it can be pushed
            JToggleButton toggleButton = new JToggleButton("-");
            toggleButton.setPreferredSize(new Dimension(BUTTON_WIDTH, BUTTON_HEIGHT));
            toggleButton.setBackground(BUTTON_COLOR);
            toggleButton.setForeground(FONT_COLOR);
            toggleButton.setFocusPainted(false);

            // add the ToggleButton to the row and to the list of toggle buttons
            add(toggleButton);
            toggleButtons.add(toggleButton);
        }
    }

    // MODIFIES: this, this.ui
    // EFFECTS: creates a button to remove the row/instrument
    public void initializeRemoveButton() {
        // Create button to remove an instrument/row with
        //   - text "Remove",
        //   - purple color,
        //   - default text color and font,
        //   - no border or focus painting,
        //   - preferred size which matches the size of the text
        JButton removeButton = new JButton("Remove");
        removeButton.setBackground(new Color(105, 15, 241));
        removeButton.setForeground(FONT_COLOR);
        removeButton.setFocusPainted(false);
        removeButton.setBorderPainted(false);
        removeButton.setFont(FONT);
        removeButton.setPreferredSize(new Dimension(120, 30));

        // add lambda action listener which calls removeRow function on the gui object
        removeButton.addActionListener(e -> gui.removeRow(ToggleButtonRow.this));

        // add the button to the ToggleButtonRow
        add(removeButton);
    }

    // MODIFIES: this
    // EFFECTS: creates a button to change the instrument
    public void initializeChangeInstrumentButton() {
        // Create button to change the instrument of a row with
        //   - text "Change Instrument",
        //   - pink color,
        //   - default text color and font,
        //   - no border or focus painting,
        //   - preferred size which matches the size of the text
        JButton changeInstrumentButton = new JButton("Change Instrument");
        changeInstrumentButton.setBackground(new Color(241, 15, 203));
        changeInstrumentButton.setForeground(FONT_COLOR);
        changeInstrumentButton.setFocusPainted(false);
        changeInstrumentButton.setBorderPainted(false);
        changeInstrumentButton.setFont(FONT);
        changeInstrumentButton.setPreferredSize(new Dimension(150, 30));

        // add a lambda action listener calling the changeInstrumentNumber function
        changeInstrumentButton.addActionListener(e -> changeInstrumentNumber());
        add(changeInstrumentButton);
    }

    // MODIFIES: this
    // EFFECTS: changes the instrument number for a given row
    public void changeInstrumentNumber() {
        // prompt the user with a dialog box to enter a new number
        int newInstrumentNumber = Integer.parseInt(
                JOptionPane.showInputDialog(gui.getFrame(), "Enter new instrument number:"));

        // update the number
        setInstrumentNumber(newInstrumentNumber);
    }

    // MODIFIES: this
    // EFFECTS: changes the instrument number associated with the row and updates the text describing it
    public void setInstrumentNumber(int instrumentNumber) {
        // set the field accordingly
        this.instrumentNumber = instrumentNumber;

        // set the text to make it match
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
        // for each element in the buttonString, make the toggleButton at that position selected if and only if
        // the element is 'x'
        for (int i = 0; i < buttonString.length(); i++) {
            char c = buttonString.charAt(i);
            toggleButtons.get(i).setSelected(c == 'x');
        }
    }

    // EFFECTS: returns the string which represents a given toggle button row
    public String getToggleButtonString() {
        StringBuilder sb = new StringBuilder();

        // for each ToggleButton, if it is selected, append 'x' else append '-'
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
