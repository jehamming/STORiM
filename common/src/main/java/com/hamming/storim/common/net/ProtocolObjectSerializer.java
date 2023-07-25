package com.hamming.storim.common.net;

import com.google.gson.*;
import com.hamming.storim.common.dto.protocol.Protocol;
import com.hamming.storim.common.util.Logger;

import java.lang.reflect.Type;

public class ProtocolObjectSerializer<T> implements JsonDeserializer<T> {

    public static final String CLASS_PROPERTY_NAME = "protocolMessage";

    @Override
    public T deserialize(
            JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        JsonPrimitive classNamePrimitive = (JsonPrimitive) jsonObject.get(CLASS_PROPERTY_NAME);

        String className = classNamePrimitive.getAsString();

        Class<?> clazz = Protocol.getInstance().getClass(className);

        if (clazz == null) {
            Logger.error(this, "Exception : Did you add an [Action] to deal with " + className + "?");
            Logger.error(this, "Or did you mistake 'send()' and 'sendReceive()'?");
            throw new JsonParseException("Could not find a suitable class for :" + className);
        }

        return context.deserialize(jsonObject, clazz);
    }

}
