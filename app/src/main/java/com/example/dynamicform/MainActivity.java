package com.example.dynamicform;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dynamicform.models.FormData;
import com.example.dynamicform.models.FormField;
import com.example.dynamicform.room.AppDatabase;
import com.example.dynamicform.utils.HashUtils;
import com.example.dynamicform.utils.JsonFormParser;
import com.example.dynamicform.views.FormFieldView;

import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private LinearLayout formContainer;
    private List<FormField> formFields;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        formContainer = findViewById(R.id.formContainer);
        Button loadFormButton = findViewById(R.id.loadFormButton);
        loadFormButton.setOnClickListener(v -> loadFormFromJson());
    }

    private void loadFormFromJson() {
        try {
            InputStream is = getAssets().open("form_template.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();

            String json = new String(buffer, StandardCharsets.UTF_8);
            JSONObject jsonObject = new JSONObject(json);
            String formTitle = jsonObject.getString("title");

            formFields = JsonFormParser.parseFormJson(json);
            generateForm(formTitle);
        } catch (Exception e) {
            Toast.makeText(this, "Error loading form: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void generateForm(String formTitle) {
        formContainer.removeAllViews();

        for (FormField field : formFields) {
            FormFieldView fieldView = new FormFieldView(this, field);
            fieldView.setTag(field.getLabel());
            formContainer.addView(fieldView);
        }

        Button submitButton = new Button(this);
        submitButton.setText("Submit");
        submitButton.setOnClickListener(v -> validateAndSubmit(formTitle));
        formContainer.addView(submitButton);
    }

    private void validateAndSubmit(String formTitle) {
        try {
            for (FormField field : formFields) {
                View view = formContainer.findViewWithTag(field.getLabel());
                if (view instanceof FormFieldView) {
                    FormFieldView fieldView = (FormFieldView) view;
                    String value = fieldView.getValue();

                    if (field.isRequired() && value.isEmpty()) {
                        Toast.makeText(this, field.getLabel() + " is required!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    field.setValue(value);
                } else {
                    throw new IllegalStateException("View for field " + field.getLabel() + " is not a FormFieldView.");
                }
            }

            saveFormData(formTitle);
        } catch (Exception e) {
            Toast.makeText(this, "Error submitting form: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void saveFormData(String formTitle) {
        AppDatabase db = AppDatabase.getInstance(this);
        String formId = HashUtils.generateMD5(System.currentTimeMillis() + formTitle);

        new Thread(() -> {
            for (FormField field : formFields) {
                View view = formContainer.findViewWithTag(field.getLabel());
                if (view instanceof FormFieldView) {
                    FormFieldView fieldView = (FormFieldView) view;
                    String value = fieldView.getValue();

                    FormData formData = new FormData();
                    formData.formId = formId;
                    formData.formTitle = formTitle;
                    formData.fieldName = field.getLabel();
                    formData.fieldValue = value;
                    formData.insertedOn = System.currentTimeMillis();
                    formData.synced = 0;
                    formData.syncedOn = 0;

                    db.formDataDao().insert(formData);

                    runOnUiThread(() -> fieldView.setValue(""));
                }
            }
            runOnUiThread(() -> Toast.makeText(this, "Form saved locally!", Toast.LENGTH_LONG).show());
        }).start();
    }

    private void syncData() {
        AppDatabase db = AppDatabase.getInstance(this);

        new Thread(() -> {
            List<FormData> unsyncedData = db.formDataDao().getUnsyncedData();
            for (FormData data : unsyncedData) {
                db.formDataDao().markAsSynced(data.formId, true, System.currentTimeMillis());
            }
            runOnUiThread(() -> Toast.makeText(this, "Data synced successfully!", Toast.LENGTH_LONG).show());
        }).start();
    }

    private String sanitizeFieldName(String label) {
        String[] words = label.split("\\s+|_");
        StringBuilder sanitized = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i].toLowerCase();
            if (i == 0) {
                sanitized.append(word);
            } else {
                sanitized.append(capitalize(word));
            }
        }
        return sanitized.toString();
    }

    private String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}