package com.example.dynamicform.utils;

import com.example.dynamicform.models.FormField;
import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class JsonFormParser {
    public static List<FormField> parseFormJson(String jsonString) throws Exception {
        List<FormField> fields = new ArrayList<>();
        JSONObject formJson = new JSONObject(jsonString);
        JSONArray jsonFields = formJson.getJSONArray("fields");

        for (int i = 0; i < jsonFields.length(); i++) {
            JSONObject field = jsonFields.getJSONObject(i);
            fields.add(new FormField(
                field.getString("type"),
                field.getString("label"),
                field.getBoolean("required")
            ));
        }
        return fields;
    }
}