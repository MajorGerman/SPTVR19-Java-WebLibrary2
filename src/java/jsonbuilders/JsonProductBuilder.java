/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsonbuilders;

import entity.Product;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

/**
 *
 * @author Georg
 */
public class JsonProductBuilder {

    public JsonObject createJsonProduct(Product product){
        JsonObjectBuilder job = Json.createObjectBuilder();
        job.add("id", product.getId())
            .add("name", product.getName())
            .add("price", product.getPrice())
            .add("cover", new JsonCoverBuilder().createJsonCover(product.getCover()))
            .add("discount", product.getDiscount())
            .add("discountDuration", product.getDiscountDuration());   
        return job.build();
    }
}

