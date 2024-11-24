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

            // Parse the JSON string to a JSONObject
            JSONObject jsonObject = new JSONObject(json);

            // Get the title from the JSON object
            String formTitle = jsonObject.getString("title");


            formFields = JsonFormParser.parseFormJson(json);
            generateForm(formTitle);
        } catch (Exception e) {
            Toast.makeText(this, "Error loading form: " + e.getMessage(), 
                         Toast.LENGTH_LONG).show();
        }
    }

    private void generateForm(String formTitle) {
        formContainer.removeAllViews();

        for (FormField field : formFields) {
            FormFieldView fieldView = new FormFieldView(this, field);
            fieldView.setTag(field.getLabel()); // Assign a unique tag for reference
            formContainer.addView(fieldView);
        }

        Button submitButton = new Button(this);
        submitButton.setText("Submit");
        submitButton.setOnClickListener(v -> validateAndSubmit(formTitle));
        formContainer.addView(submitButton);
    }

    private void validateAndSubmit(String formTitle) {
        try {
            // Collect values from form fields
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

            // Generate the dynamic class using JavaPoet
            String className = capitalize(sanitizeFieldName(formTitle));

            saveFormData(className);

            // Toast the value of the first field
            //Toast.makeText(this, "Form submitted successfully!", Toast.LENGTH_LONG).show();

        } catch (Exception e) {
            Toast.makeText(this, "Error submitting form: " + e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    private void saveFormData(String formTitle) {
        // Get database instance
        AppDatabase db = AppDatabase.getInstance(this);
        String formId = HashUtils.generateMD5(System.currentTimeMillis() + formTitle);


        // Use a separate thread for database operations
        new Thread(() -> {
            for (FormField field : formFields) { // Assuming `formFields` contains your fields
                Log.d("FormFieldSize", "Size: "+formFields.size());
                View view = formContainer.findViewWithTag(field.getLabel());
                Log.d("FormFieldType", "Type: "+field.getType());


                Log.d("FormFieldView", "view: "+view);

                if (view instanceof FormFieldView) { // Adjust based on your field type
                    FormFieldView editText = (FormFieldView) view;
                    String value = editText.getValue();

                    Log.d("FormField", "saveFormData: "+value);

                    // Create FormData object
                    FormData formData = new FormData();
                    formData.formId = formId;
                    formData.formTitle = formTitle;
                    formData.fieldName = field.getLabel();
                    formData.fieldValue = value;
                    formData.insertedOn = System.currentTimeMillis(); // Record creation time
                    formData.synced = 0; // Not synced initially
                    formData.syncedOn = 0; // No sync timestamp initially

                    // Save to database
                    db.formDataDao().insert(formData);
                }
            }
            runOnUiThread(() ->
                    Toast.makeText(this, "Form saved locally!", Toast.LENGTH_LONG).show()
            );
        }).start();
    }

    private void markDataAsSynced(String formId) {
        AppDatabase db = AppDatabase.getInstance(this);

        new Thread(() -> {
            long syncTimestamp = System.currentTimeMillis();
            db.formDataDao().markAsSynced(formId, true, syncTimestamp);

            runOnUiThread(() ->
                    Toast.makeText(this, "Data synced successfully!", Toast.LENGTH_LONG).show()
            );
        }).start();
    }

    private void syncData() {
        AppDatabase db = AppDatabase.getInstance(this);

        new Thread(() -> {
            List<FormData> unsyncedData = db.formDataDao().getUnsyncedData();

            // Simulate syncing process
            for (FormData data : unsyncedData) {
                // Sync logic here (e.g., send data to the server)
                // If sync is successful, mark as synced
                db.formDataDao().markAsSynced(data.formId, true, System.currentTimeMillis());
            }

            runOnUiThread(() ->
                    Toast.makeText(this, "Data synced successfully!", Toast.LENGTH_LONG).show()
            );
        }).start();
    }

    private void loadFormData(String formTitle) {
        // Get database instance
        AppDatabase db = AppDatabase.getInstance(this);

        new Thread(() -> {
            List<FormData> formDataList = db.formDataDao().getFormData(formTitle);
            runOnUiThread(() -> {
                for (FormData formData : formDataList) {
                    View view = formContainer.findViewWithTag(formData.fieldName);
                    if (view instanceof EditText) { // Adjust based on your field type
                        EditText editText = (EditText) view;
                        editText.setText(formData.fieldValue);
                    }
                }
            });
        }).start();
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