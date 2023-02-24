package model;

public class Instrument {
    private int instrumentNumber;
    private char[] instrumentNotes;

    public Instrument(int instrumentNumber, String instrumentNotes) {
        this.instrumentNotes = instrumentNotes.toCharArray();
        this.instrumentNumber = instrumentNumber;
    }

    public int getInstrumentNumber() {
        return this.instrumentNumber;
    }

    public char[] getInstrumentNotes() {
        return this.instrumentNotes;
    }

    public void setInstrumentNumber(int newNumber) {
        this.instrumentNumber = newNumber;
    }

    public void setInstrumentNotes(String newNotes) {
        this.instrumentNotes = newNotes.toCharArray();
    }
}