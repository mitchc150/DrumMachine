package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            DrumTrackList tracks = reader.read();
            fail("IOException expected");
        } catch (Exception e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyDrumTrackList.json");
        try {
            DrumTrackList tracks = reader.read();
            assertEquals(90, tracks.getBPM());
            assertEquals(0, tracks.getInstruments().size());
        } catch (Exception e) {
            fail("Couldn't read from file");
        }
    }

    /*
    @Test
    void testReaderGeneralWorkRoom() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralWorkRoom.json");
        try {
            WorkRoom wr = reader.read();
            assertEquals("My work room", wr.getName());
            List<Thingy> thingies = wr.getThingies();
            assertEquals(2, thingies.size());
            checkThingy("needle", Category.STITCHING, thingies.get(0));
            checkThingy("saw", Category.WOODWORK, thingies.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }*/
}