package persistence;

import org.json.JSONObject;

//Citation: JsonSerializationDemo

public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}


