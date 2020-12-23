package me.kostevych.widgets.exceptions;

import java.util.UUID;

public class WidgetNotFoundException extends RuntimeException {
    public WidgetNotFoundException(UUID id) {
        super("Could not find widget " + id);
    }
}
