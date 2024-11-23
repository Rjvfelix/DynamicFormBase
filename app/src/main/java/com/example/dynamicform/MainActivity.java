package com.example.dynamicform;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dynamicform.models.FormField;
import com.example.dynamicform.utils.JsonFormParser;
import com.example.dynamicform.views.FormFieldView;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeSpec;

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

            formFields = JsonFormParser.parseFormJson(json);
            generateForm();
        } catch (Exception e) {
            Toast.makeText(this, "Error loading form: " + e.getMessage(), 
                         Toast.LENGTH_LONG).show();
        }
    }

    private void generateForm() {
        formContainer.removeAllViews();
        
        for (FormField field : formFields) {
            formContainer.addView(FormFieldView.createField(this, field));
        }

        Button submitButton = new Button(this);
        submitButton.setText("Submit");
        submitButton.setOnClickListener(v -> validateAndSubmit());
        formContainer.addView(submitButton);
    }

    private void validateAndSubmit() {
        Toast.makeText(this, "Validé", Toast.LENGTH_LONG).show();
    }

    // Utility method to sanitize field labels into valid Java variable names
    private String sanitizeFieldName(String label) {
        // Remove non-alphanumeric characters and convert to camelCase
        String[] words = label.split("\\s+|_"); // Split on spaces or underscores
        StringBuilder sanitized = new StringBuilder();
        for (int i = 0; i < words.length; i++) {
            String word = words[i].toLowerCase();
            if (i == 0) {
                sanitized.append(word); // First word lowercase
            } else {
                sanitized.append(capitalize(word)); // Subsequent words capitalized
            }
        }
        return sanitized.toString();
    }

    // Utility method to capitalize strings
    private String capitalize(String input) {
        if (input == null || input.isEmpty()) return input;
        return input.substring(0, 1).toUpperCase() + input.substring(1);
    }
}