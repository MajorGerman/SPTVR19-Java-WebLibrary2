package jsonbuilders;

import entity.Product;
import java.math.BigDecimal;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class JsonProductBuilder {

    public JsonObject createJsonProduct(Product product){
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", product.getId())
            .add("name", product.getName())
            .add("price", product.getPrice())
            .add("cover", new JsonCoverBuilder().createJsonCover(product.getCover()))
            .add("tag", product.getTags().toString())
            .add("discount", product.getDiscount())
            .add("discountDuration", product.getDiscountDuration())
            .add("count", product.getCount());   
        return job.build();
    }
}

