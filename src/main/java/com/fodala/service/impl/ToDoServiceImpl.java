package com.fodala.service.impl;

import com.fodala.mapper.ToDoMapper;
import com.fodala.pojo.ToDo;
import com.fodala.pojo.ToDoHistory;
import com.fodala.service.ToDoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class ToDoServiceImpl implements ToDoService {
    private static final Logger logger = LoggerFactory.getLogger(ToDoServiceImpl.class);

    final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");


    @Autowired
    ToDoMapper mapper;

    @Override
    public List<ToDo> all() {
        return mapper.all();
    }

    @Override
    public ToDo findById(Integer id) {
        return mapper.findById(id);
    }

    @Override
    public void important(Integer id) {
        mapper.important(id);
    }

    @Override
    public void completed(Integer id) {
        mapper.completed(id);
    }

    @Override
    public Integer insert(ToDo toDo) {
        mapper.insert(toDo);
        return mapper.lastInsert();
    }

    @Override
    public void update(ToDo toDo) {
        mapper.update(toDo);
    }

    @Override
    public void delete(Integer id) {
        mapper.delete(id);
    }

    @Override
    public List<ToDoHistory> history(Integer id) {
        return mapper.history(id);
    }

    @Override
    public ToDo createEmpty() {
        return new ToDo();
    }

    @Override
    public Map<String, Object> count() {
        return mapper.count();
    }


    @Override
    public List<ToDo> findByDate(Timestamp start, Timestamp end) {
        return mapper.findByDate(start, end);
    }

    @Override
    public List<ToDo> filter(Map<String, Object> map) {
        return mapper.filter(map);
    }

    @Override
    public List<ToDo> search(String search) {
        return mapper.search("%" + search + "%");
    }

    @Override
    public List<Map<String, Object>> todoHistory() {
        return mapper.todoHistory();
    }

    @Override
    public List<Map<String, Object>> listNames() {
        return mapper.listNames();
    }

    @Override
    public List<ToDo> list(String name) {
        return mapper.list(name);
    }

    @Override
    public void createList(String list, String createdTime) {
        mapper.createList(list, createdTime);
    }

    @Override
    public Integer findListByCreated(String createdTime) {
        return mapper.findListByCreated(createdTime);
    }

    @Override
    public List<ToDo> listItems(Integer id) {
        return mapper.listItems(id);
    }

    @Override
    public void insertListItem(Integer toDoId, Integer listId) {
        mapper.insertListItem(toDoId, listId);
    }

    @Override
    public List<ToDo> getToDos(Map<String, String> params) {
        Filter filter = Filter.parse(params.get("filter"));
        Section section = Section.parse(params.get("section"));
        List<ToDo> result;
        switch (section) {
            case PLANNED -> {
                LocalDateTime start = LocalDateTime.now();
                LocalDateTime end = LocalDateTime.now().plusYears(100L);
                result = findByDate(Timestamp.valueOf(start), Timestamp.valueOf(end));
            }
            case MY_DAY -> {
                LocalDateTime start = LocalDateTime.now();
                LocalDateTime end = LocalDateTime.now().plusDays(1L);
                result = findByDate(Timestamp.valueOf(start), Timestamp.valueOf(end));
            }
            case IMPORTANT -> result = filter(Collections.singletonMap("important", 1));
            case LIST -> result = listItems(Integer.valueOf(params.get("list_id")));
            case SEARCH -> result = search(params.get("search"));
            default -> result = all();
        }
        return Filter.filter(filter, result);
    }
}
