package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

class InstrumentTest {
    Instrument instrument1;
    Instrument instrument2;
    Instrument instrument3;

    @BeforeEach
    public void init() {
        instrument1 = new Instrument(35, "x-x-x-x-x");
        instrument2 = new Instrument(81, "-x-x-x-x-x-x");
        instrument3 = new Instrument(50, "x-x-x-x");
    }

    @Test
    public void constructorTest() {
        assertArrayEquals(instrument1.getInstrumentNotes(), "x-x-x-x-x".toCharArray());
        assertEquals(instrument1.getInstrumentNumber(), 35);
        assertEquals(instrument1.getInstrumentName(), "Acoustic Bass Drum");
        assertArrayEquals(instrument2.getInstrumentNotes(), "-x-x-x-x-x-x".toCharArray());
        assertEquals(instrument2.getInstrumentNumber(), 81);
        assertEquals(instrument2.getInstrumentName(), "Open Triangle");
        assertArrayEquals(instrument3.getInstrumentNotes(), "x-x-x-x".toCharArray());
        assertEquals(instrument3.getInstrumentNumber(), 50);
        assertEquals(instrument3.getInstrumentName(), "High Tom");
    }

    @Test
    public void setNotesTest() {
        assertArrayEquals(instrument1.getInstrumentNotes(), "x-x-x-x-x".toCharArray());
        instrument1.setInstrumentNotes("-x-x-x-x-x-x");
        assertArrayEquals(instrument1.getInstrumentNotes(), "-x-x-x-x-x-x".toCharArray());
    }

    @Test
    public void setNumberTest() {
        assertEquals(instrument1.getInstrumentNumber(), 35);
        instrument1.setInstrument(49);
        assertEquals(instrument1.getInstrumentNumber(), 49);
        assertEquals(instrument1.getInstrumentName(), "Crash Cymbal 1");
    }

}