package com.example.dynamicform.views;

import android.content.Context;
import android.text.InputType;
import android.widget.LinearLayout;

import com.example.dynamicform.models.FormField;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;

public class FormFieldView extends LinearLayout {
    private final TextInputLayout textInputLayout;
    private final TextInputEditText editText;

    public FormFieldView(Context context, FormField field) {
        super(context);

        // Create TextInputLayout and TextInputEditText
        textInputLayout = new TextInputLayout(context);
        editText = new TextInputEditText(context);

        // Set the layout width to match parent
        LayoutParams layoutParams = new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT
        );
        this.setLayoutParams(layoutParams); // Set this view to match_parent width

        // Configure the hint for the TextInputLayout
        textInputLayout.setHint(field.getLabel() + (field.isRequired() ? " *" : ""));
        configureInputType(field);

        // Ensure that TextInputEditText takes full width of its parent
        textInputLayout.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        editText.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        // Add the EditText to the TextInputLayout and the TextInputLayout to this view
        textInputLayout.addView(editText);
        this.addView(textInputLayout);
    }

    private void configureInputType(FormField field) {
        switch (field.getType()) {
            case "number":
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                break;
            case "email":
                editText.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                break;
            case "phone":
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
                break;
            default:
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
        }
    }

    public String getValue() {
        return editText.getText()==null?"":editText.getText().toString().trim();
    }

    public void setValue(String value) {
        editText.setText(value);
    }
}