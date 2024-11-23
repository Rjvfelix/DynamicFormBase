package com.example.dynamicform.views;

import android.content.Context;
import android.text.InputType;
import android.view.View;
import com.example.dynamicform.models.FormField;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.textfield.TextInputEditText;

public class FormFieldView {
    public static View createField(Context context, FormField field) {
        TextInputLayout textInputLayout = new TextInputLayout(context);
        TextInputEditText editText = new TextInputEditText(context);
        
        textInputLayout.setHint(field.getLabel() + (field.isRequired() ? " *" : ""));
        
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
        
        textInputLayout.addView(editText);
        return textInputLayout;
    }
}