package com.example.kinoteka.ui;

import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;

public class ClearableTextField extends HorizontalLayout {
    private final TextField textField;
    private final Button button = new Button(VaadinIcon.SEARCH.create());

    public ClearableTextField() {
        this(null);
    }

    public ClearableTextField(String caption) {
        this(caption, false);
    }

    public ClearableTextField(String caption, boolean captionAsHtml) {
        setSpacing(false);
        textField = new TextField(caption);
        button.setVisible(false);
        add(textField, button);

        textField.addValueChangeListener(event -> button.setVisible(!event.getValue().trim().isEmpty()));
        button.addClickListener(event -> textField.clear());
    }

    public TextField getTextField() {
        return textField;
    }

    public String getCaption() {
        return textField.getValue();
    }

    public void setCaption(String caption) {
        textField.setValue(caption);
    }

    public void setPlaceholder(String placeholder) {
        textField.setPlaceholder(placeholder);
    }

    public String getValue() {
        return textField.getValue();
    }

    public void setValue(String value) {
        textField.setValue(value);
    }

    public boolean isEmpty() {
        return textField.isEmpty();
    }

    public void clear() {
        textField.clear();
    }

    public Registration addValueChangeListener(HasValue.ValueChangeListener<HasValue.ValueChangeEvent<? extends String>> listener
    ) {
        return textField.addValueChangeListener(listener);
    }
}