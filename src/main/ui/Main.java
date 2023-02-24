package ui;

import model.*;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiUnavailableException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InvalidMidiDataException, MidiUnavailableException, InterruptedException {
        new DrumMachineApp();
    }
}
