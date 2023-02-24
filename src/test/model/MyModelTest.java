package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InstrumentTest {
    Instrument instrument1;
    Instrument instrument2;

    @BeforeEach
    public void init() {
        instrument1 = new Instrument(15, "x-x-x-x-x");
        instrument2 = new Instrument(12, "-x-x-x-x-x-x");
    }

    @Test
    public void constructorTest() {
        assertArrayEquals(instrument1.getInstrumentNotes(), "x-x-x-x-x".toCharArray());
        assertEquals(instrument1.getInstrumentNumber(), 15);
        assertArrayEquals(instrument2.getInstrumentNotes(), "-x-x-x-x-x-x".toCharArray());
        assertEquals(instrument2.getInstrumentNumber(), 12);
    }

    @Test
    public void setNotesTest() {
        assertArrayEquals(instrument1.getInstrumentNotes(), "x-x-x-x-x".toCharArray());
        instrument1.setInstrumentNotes("-x-x-x-x-x-x");
        assertArrayEquals(instrument1.getInstrumentNotes(), "-x-x-x-x-x-x".toCharArray());
    }

    @Test
    public void setNumberTest() {
        assertEquals(instrument1.getInstrumentNumber(), 15);
        instrument1.setInstrumentNumber(10);
        assertEquals(instrument1.getInstrumentNumber(), 10);
    }

}