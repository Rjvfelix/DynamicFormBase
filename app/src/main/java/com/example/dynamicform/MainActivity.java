package com.example.dynamicform;

import android.os.Bundle;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.dynamicform.models.FormField;
import com.example.dynamicform.utils.JsonFormParser;
import com.example.dynamicform.views.FormFieldView;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.TypeSpec;

import org.json.JSONObject;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import javax.lang.model.element.Modifier;

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
            formContainer.addView(FormFieldView.createField(this, field));
        }

        Button submitButton = new Button(this);
        submitButton.setText("Submit");
        submitButton.setOnClickListener(v -> validateAndSubmit(formTitle));
        formContainer.addView(submitButton);
    }

    private void validateAndSubmit(String formTitle) {
        try {
            // Generate dynamic class
            String className = capitalize(sanitizeFieldName(formTitle));
            TypeSpec dynamicClass = generateDynamicClass(className, formFields);

            // Write the generated class to a file
            JavaFile javaFile = JavaFile.builder("com.example.dynamicform.generated", dynamicClass)
                    .build();

            // Write the file to disk
            javaFile.writeTo(System.out); // Replace with actual output destination

            Toast.makeText(this, "Dynamic class generated: " + className, Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error generating dynamic class: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }
    }

    private TypeSpec generateDynamicClass(String className, List<FormField> formFields) {
        ClassName dynamicClassName = ClassName.get("com.example.dynamicform.generated", className);

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(dynamicClassName)
                .addModifiers(Modifier.PUBLIC);

        // Constructor
        MethodSpec constructor = MethodSpec.constructorBuilder()
                .addParameters(getConstructorParams(formFields))
                .addCode(getConstructorBody(formFields))
                .build();
        classBuilder.addMethod(constructor);

        // Fields
        for (FormField field : formFields) {
            FieldSpec fieldSpec = FieldSpec.builder(
                    ClassName.get("", sanitizeFieldName(field.getLabel())),
                    sanitizeFieldName(field.getLabel()),
                    Modifier.PRIVATE
            ).build();
            classBuilder.addField(fieldSpec);

            // Getter
            MethodSpec getter = MethodSpec.methodBuilder("get" + capitalize(sanitizeFieldName(field.getLabel())))
                    .addModifiers(Modifier.PUBLIC)
                    .returns(ClassName.get("", sanitizeFieldName(field.getLabel())))
                    .addStatement("return $N", sanitizeFieldName(field.getLabel()))
                    .build();
            classBuilder.addMethod(getter);

            // Setter
            MethodSpec setter = MethodSpec.methodBuilder("set" + capitalize(sanitizeFieldName(field.getLabel())))
                    .addModifiers(Modifier.PUBLIC)
                    .addParameter(ClassName.get("", sanitizeFieldName(field.getLabel())), sanitizeFieldName(field.getLabel()))
                    .addStatement("$N.$N = $N", "this", sanitizeFieldName(field.getLabel()), sanitizeFieldName(field.getLabel()))
                    .build();
            classBuilder.addMethod(setter);
        }

        return classBuilder.build();
    }

    private List<ParameterSpec> getConstructorParams(List<FormField> formFields) {
        List<ParameterSpec> params = new ArrayList<>();
        for (FormField field : formFields) {
            params.add(ParameterSpec.builder(
                    ClassName.get("", sanitizeFieldName(field.getLabel())),
                    sanitizeFieldName(field.getLabel())
            ).build());
        }
        return params;
    }

    private CodeBlock getConstructorBody(List<FormField> formFields) {
        CodeBlock.Builder body = CodeBlock.builder();
        for (FormField field : formFields) {
            body.addStatement("$N.$N = $N", "this", sanitizeFieldName(field.getLabel()), sanitizeFieldName(field.getLabel()));
        }
        return body.build();
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