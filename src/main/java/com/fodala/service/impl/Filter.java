package com.fodala.service.impl;

import com.fodala.pojo.ToDo;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public enum Filter {
    ALL("all", -1, " 1 = 1 "),
    ACTIVE("active", 0, " completed = 0 "),
    COMPLETED("completed", 1, " completed = 1 ");

    public final String name;
    public final Integer completed;

    public final String where;

    Filter(String name, Integer completed, String where) {
        this.name = name;
        this.completed = completed;
        this.where = where;
    }

    public static Filter parse(String name) {
        for (Filter t : Filter.values()) {
            if (t.name.equals(name)) {
                return t;
            }
        }
        return Filter.ACTIVE;
    }

    public static List<ToDo> filter(Filter filter, List<ToDo> toDos) {
        if (Filter.ALL == filter) {
            return toDos;
        } else {
            return toDos.stream()
                    .filter(toDo -> Objects.equals(filter.completed, toDo.getCompleted()))
                    .collect(Collectors.toList());
        }
    }
}
