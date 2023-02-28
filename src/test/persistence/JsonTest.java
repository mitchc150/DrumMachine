package persistence;

import model.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkInstrument(Instrument instrument, int instrumentNum, String instrumentNotes) {
        assertEquals(instrumentNum, instrument.getInstrumentNumber());
        assertEquals(instrumentNotes, instrument.getInstrumentNotesString());
    }
}
