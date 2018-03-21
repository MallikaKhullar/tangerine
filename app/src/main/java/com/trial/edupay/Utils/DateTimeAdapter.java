package com.trial.edupay.Utils;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import org.joda.time.DateTime;

import java.lang.reflect.Type;

/**
 * Created by mallikapriyakhullar on 25/12/17.
 */

public class DateTimeAdapter implements JsonSerializer<DateTime>, JsonDeserializer<DateTime> {
    public JsonElement serialize(DateTime src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    public DateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        return DateTime.parse(json.getAsJsonPrimitive().getAsString());
    }
}
