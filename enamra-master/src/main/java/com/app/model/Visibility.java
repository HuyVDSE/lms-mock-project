package com.app.model;

import java.awt.*;

public enum Visibility {
    SHOW("Show"),
    HIDDEN("Hidden");

    private final String displayValue;

    private Visibility(String displayValue) {
        this.displayValue = displayValue;
    }

    public String getDisplayValue() {
        return displayValue;
    }
}
