package persistence;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

import javax.sound.midi.InvalidMidiDataException;

public class JsonReader {
    private String source;

    // EFFECTS: constructs a reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads DrumTrackList from file and returns it;
    // throws IOException if an error occurs reading data from the file
    public DrumTrackList read() throws Exception {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseDrumTrackList(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses DrumTrackList from JSON object and returns it
    private DrumTrackList parseDrumTrackList(JSONObject jsonObject) throws Exception {
        int bpm = jsonObject.getInt("bpm");
        DrumTrackList tracks = new DrumTrackList(bpm);
        addInstruments(tracks, jsonObject);
        return tracks;
    }

    // MODIFIES: tracks
    // EFFECTS: parses thingies from JSON object and adds them to workroom
    private void addInstruments(DrumTrackList tracks, JSONObject jsonObject) throws Exception {
        JSONArray jsonArray = jsonObject.getJSONArray("instruments");
        for (Object json : jsonArray) {
            JSONObject nextInstrument = (JSONObject) json;
            addInstrument(tracks, nextInstrument);
        }
    }

    // MODIFIES: tracks
    // EFFECTS: parses instrument from JSON object and adds it to workroom
    private void addInstrument(DrumTrackList tracks, JSONObject jsonObject) throws Exception {
        int instrumentNumber = jsonObject.getInt("number");
        String instrumentNotes = jsonObject.getString("notes");
        Instrument instrument = new Instrument(instrumentNumber, instrumentNotes);
        tracks.addTrack(instrument);
    }
}
