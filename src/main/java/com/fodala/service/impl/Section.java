package com.fodala.service.impl;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public enum Section {
    CALENDAR("calendar", "Calendar"),
    IMPORTANT("important", "Important"),
    LIST("list", "List"),
    MY_DAY("myday", "My Day " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("EEE d MMM uuuu"))),
    HISTORY("history", "History"),
    PLANNED("planned", "Planned"),
    SEARCH("search", "Search"),
    TASKS("tasks", "Tasks");
    public final String value;

    public final String title;

    Section(String value, String title) {
        this.value = value;
        this.title = title;
    }

    public static Section parse(String tabVal) {
        for (Section t : Section.values()) {
            if (t.value.equals(tabVal)) {
                return t;
            }
        }
        return Section.TASKS;
    }
}
