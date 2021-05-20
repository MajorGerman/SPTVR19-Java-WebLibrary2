package jsonbuilders;

import entity.Cover;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class JsonCoverBuilder {
    public JsonObject createJsonCover(Cover cover){
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", cover.getId())
            .add("description", cover.getDescription())
            .add("path", cover.getPath());
        return job.build();
    }
}