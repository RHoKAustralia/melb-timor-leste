package org.rhok.linguist.network;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import net.servicestack.client.Utils;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by bramleyt on 6/08/2015.
 */
public class PCJsonSerializers {
    public static JsonDeserializer<Date> getDateDeserializer() {
        return new JsonDeserializer() {
            public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                return json == null ? null : parseDate(json.getAsString());
            }
        };
    }

    public static Date parseDate(String string) {
        String str = string.startsWith("\\") ? string.substring(1) : string;
        if (str.startsWith("/Date(")) {
            //eg -300740400000
            String body = Utils.splitOnLast(Utils.splitOnFirst(str, '(')[1], ')')[0];
            boolean neg = body.charAt(0) == '-';
            if (neg || body.charAt(0) == '+') body = body.substring(1);
            String unixTimeStr = Utils.splitOnLast(body.replace('+', '-'), '-')[0];
            long unixTime = Long.parseLong(unixTimeStr);
            if (neg) unixTime = -unixTime;
            return new Date(unixTime);
        } else {
            return Utils.fromIsoDateString(string);
        }
    }

    /**
     * excludes fields that start with __
     */
    public static ExclusionStrategy getUnderscoreExclusionStrategy() {
        return new ExclusionStrategy() {

            @Override
            public boolean shouldSkipField(FieldAttributes f) {
                return f.getName().startsWith("__");
            }

            @Override
            public boolean shouldSkipClass(Class<?> clazz) {
                return false;
            }
        };
    }
}
