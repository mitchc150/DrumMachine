package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import javax.sound.midi.InvalidMidiDataException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonWriterTest extends JsonTest {
    //NOTE TO CPSC 210 STUDENTS: the strategy in designing tests for the JsonWriter is to
    //write data to a file and then use the reader to read it back in and check that we
    //read in a copy of what was written out.

    @Test
    void testWriterInvalidFile() throws InvalidMidiDataException {
        try {
            DrumTrackList tracks = new DrumTrackList(120);
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            DrumTrackList tracks = new DrumTrackList(120);
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyDrumTrackList.json");
            writer.open();
            writer.write(tracks);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyDrumTrackList.json");
            tracks = reader.read();
            assertEquals(120, tracks.getBPM());
            assertEquals(0, tracks.getInstruments().size());
        } catch (Exception e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() throws Exception {
        try {
            DrumTrackList tracks = new DrumTrackList(85);
            tracks.addTrack(new Instrument(35, "x-x-"));
            tracks.addTrack(new Instrument(60, "-x-x"));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralDrumTrackList.json");
            writer.open();
            writer.write(tracks);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralDrumTrackList.json");
            tracks = reader.read();
            assertEquals(85, tracks.getBPM());
            List<Instrument> instruments = tracks.getInstruments();
            assertEquals(2, instruments.size());
            checkInstrument(instruments.get(0), 35, "x-x-");
            checkInstrument(instruments.get(1), 60, "-x-x");

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}