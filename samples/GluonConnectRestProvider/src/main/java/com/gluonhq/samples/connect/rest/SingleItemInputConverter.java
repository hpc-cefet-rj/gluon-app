package com.gluonhq.samples.connect.rest;

import com.gluonhq.connect.converter.InputStreamInputConverter;
import com.gluonhq.connect.converter.JsonConverter;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

public class SingleItemInputConverter<T> extends InputStreamInputConverter<T> {

    private final JsonConverter<T> jsonConverter;

    public SingleItemInputConverter(Class<T> targetClass) {
        this.jsonConverter = new JsonConverter<>(targetClass);
    }

    @Override
    public T read() {
        try (JsonReader reader = Json.createReader(getInputStream())) {
            JsonObject jsonObject = reader.readObject();
            JsonArray jsonArray = jsonObject.getJsonArray("items");
            if (jsonArray.size() > 0) {
                return jsonConverter.readFromJson(jsonArray.getJsonObject(0));
            }
        }

        return null;
    }
}
