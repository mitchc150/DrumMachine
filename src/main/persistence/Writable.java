package persistence;

import org.json.JSONObject;

// Represents a JSON writable
public interface Writable {
    // EFFECTS: returns this as a JSON object
    JSONObject toJson();
}
