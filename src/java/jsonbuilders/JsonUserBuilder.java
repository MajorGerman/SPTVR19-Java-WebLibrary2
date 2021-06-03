package jsonbuilders;

import entity.User;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class JsonUserBuilder {

    public JsonObject createJsonUser(User user){
        if (user.getCover() == null) {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("id", user.getId())
            .add("login", user.getLogin())
            .add("name", user.getPerson().getName())
            .add("surname", user.getPerson().getSurname())
            .add("money", user.getPerson().getMoney())
            .add("phone", user.getPerson().getPhone());
            return job.build();            
        } else {
            JsonObjectBuilder job = Json.createObjectBuilder();
            job.add("id", user.getId())
            .add("login", user.getLogin())
            .add("name", user.getPerson().getName())
            .add("surname", user.getPerson().getSurname())
            .add("cover", new JsonCoverBuilder().createJsonCover(user.getCover()))
            .add("coverpath", user.getCover().getPath())
            .add("money", user.getPerson().getMoney())
            .add("phone", user.getPerson().getPhone());
            return job.build();
    }
    }
}