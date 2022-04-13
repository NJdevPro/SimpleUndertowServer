package com.njdev.simplewebserver;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class JsonUtil {
    public static Gson gson = new GsonBuilder()
            .setObjectToNumberStrategy(ToNumberPolicy.BIG_DECIMAL)
            .setNumberToNumberStrategy(ToNumberPolicy.BIG_DECIMAL)
            .create();

    /*
     * Due to type erasure, we have to pass "new TypeToken<>(){}" as 2nd parameter.
     * Type inference magically figures the rest out.
     */
    public static <T> T fromJsonFile(String filePath, TypeToken<T> token) {
        try {
            var reader = new JsonReader(new FileReader(filePath));
            reader.setLenient(true);
            return gson.fromJson(reader, token.getType());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String json, TypeToken<T> token) {
        return gson.fromJson(json, token.getType());
    }

    public static String toJson(Object dto) {
        return gson.toJson(dto);
    }

    public static StringBuilder readTextFile(File file) throws IOException {
        StringBuilder str = new StringBuilder();
        try (var reader = new BufferedReader(new FileReader(file))) {
            String line;
            while (null != (line = reader.readLine())) {
                str.append(line).append('\n');
            }
        }
        return str;
    }

    public static <T> T deepCopy(T object, TypeToken<T> typeToken) {
        return fromJson(gson.toJson(object), typeToken);
    }
}
