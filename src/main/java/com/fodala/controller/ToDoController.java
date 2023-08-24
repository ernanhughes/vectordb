package com.fodala.controller;

import com.fodala.pojo.Setting;
import com.fodala.pojo.ToDo;
import com.fodala.service.SettingsService;
import com.fodala.service.ToDoService;
import com.fodala.service.impl.Filter;
import com.fodala.service.impl.Section;
import com.fodala.validation.ToDoValidationError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/todo")
public class ToDoController {
    private static final Logger logger = LoggerFactory.getLogger(ToDoController.class);

    @Autowired
    private ToDoService toDoService;

    @Autowired
    private SettingsService settingsService;

    @Autowired
    private DateTimeFormatter createdFormatter;

    Optional<String> getPreviousPageByRequest(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("Referer")).map(requestUrl -> "redirect:" + requestUrl);
    }

    @GetMapping("/back")
    public ModelAndView back(HttpServletRequest request) {
        String referer = getPreviousPageByRequest(request).orElse("/");
        return new ModelAndView("redirect:" + referer);
    }

    @GetMapping("/calendar")
    public String calendar(Model model) {
        addAttributes(model, Filter.COMPLETED, Section.CALENDAR, toDoService.all(), null);
        return "calendar";
    }

    @GetMapping("/history")
    public String history(Model model) {
        addAttributes(model, Filter.ALL, Section.HISTORY, toDoService.all(), null);
        model.addAttribute("history", toDoService.todoHistory());
        return "history";
    }

    void addAttributes(Model model, Filter listFilter, Section section, List<ToDo> toDos, Integer page) {
        model.addAttribute("todo", toDoService.createEmpty());
        model.addAttribute("filter", listFilter.name);
        model.addAttribute("title", section.title);
        model.addAttribute("section", section.value);
        model.addAttribute("totalNumberOfToDos", toDos.size());
        List<Setting> list = settingsService.all();
        Map<String, String> settings = list.stream()
                .collect(Collectors.toMap(Setting::getName, Setting::getValue));
        model.addAttribute("settings", settings);
        double maxItems = Double.parseDouble(settings.get("Max ToDo Items"));
        model.addAttribute("maxToDoItems", (int) maxItems);
        model.addAttribute("count", toDoService.count());
        model.addAttribute("listNames", toDoService.listNames());
        int pages = (int) Math.ceil((double) toDos.size() / maxItems);
        model.addAttribute("pages", pages);
        if (page == null || page < 1) {
            page = 1;
        } else if (page > pages) {
            page = pages;
        }
        model.addAttribute("page", page);
        if (pages > 0) {
            int maxItemCount = (int) maxItems;
            int from = ((page - 1) * maxItemCount);
            int to = from + maxItemCount;
            if (to > toDos.size()) {
                to = toDos.size();
            }
            toDos = toDos.subList(from, to);
        }
        model.addAttribute("todos", toDos);
    }

    @ExceptionHandler
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ToDoValidationError handleException(Exception exception) {
        return new ToDoValidationError(exception.getMessage());
    }

    @GetMapping("/")
    public String home(Model model) {
        logger.info("Getting all todos");
        addAttributes(model, Filter.ACTIVE, Section.TASKS, toDoService.all(), 1);
        return "index";
    }

    @GetMapping("/todo")
    public String todo(@RequestParam(value = "id", required = false) Integer id,
                       Model model) {
        ToDo toDo;
        if (id != null) {
            toDo = toDoService.findById(id);
            model.addAttribute("history", toDoService.history(id));
        } else {
            toDo = toDoService.createEmpty();
        }
        model.addAttribute("section", "tasks");
        model.addAttribute("title", toDo.getTitle());
        model.addAttribute("todo", toDo);
        return "edit";
    }

    @PostMapping("/add")
    public String save(ToDo toDo,
                       @RequestParam(value = "section", required = false) String section,
                       @RequestParam(value = "list_id", required = false) Integer listId,
                       @RequestParam(value = "filter", required = false) String filter,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       BindingResult bindingResult, Model model) {
        if (!bindingResult.hasErrors()) {
            if (toDo.getId() == null) {
                toDo.setCreated(LocalDateTime.now().format(createdFormatter));
                toDo.setImportant(section.equals(Section.IMPORTANT.value) ? 1 : 0);
                toDo.setCompleted(0);
                if (section.equals(Section.MY_DAY.value)) {
                    toDo.setStart(LocalDateTime.now().format(createdFormatter));
                }
                toDo.setStatus("New");
                int id = toDoService.insert(toDo);
                if (section.equals(Section.LIST.value)) {
                    toDoService.insertListItem(listId, id);
                }
                logger.info("Inserted todo {}", toDo);
            } else {
                if (toDoService.findById(toDo.getId()) != null) {
                    toDoService.update(toDo);
                    logger.info("Updated todo {}", toDo);
                }
            }
        }
        model.addAttribute("section", section);
        model.addAttribute("title", section);
        model.addAttribute("list_id", listId);
        model.addAttribute("filter", filter);
        model.addAttribute("todo", new ToDo());
        return "index";
    }

    @PostMapping("/search")
    public String search(@RequestParam(value = "search", required = false) String search, Model model) {
        logger.info("Finding tasks containing text: {}", search);
        model.addAttribute("search", search);
        List<ToDo> toDos = toDoService.search("%" + search + "%");
        logger.info("Found {} todos matching text: {}", toDos.size(), search);
        model.addAttribute("title", "Find: " + search);
        addAttributes(model, Filter.ALL, Section.SEARCH, toDos, 1);
        return "index";
    }

    @GetMapping("/search")
    public String searchGet(@RequestParam(value = "search", required = false) String search, Model model) {
        logger.info("Getting tasks containing text {}", search);
        model.addAttribute("search", search);
        List<ToDo> toDos = toDoService.search("%" + search + "%");
        addAttributes(model, Filter.ALL, Section.SEARCH, toDos, 1);
        model.addAttribute("title", "Searching for " + search);
        return "index";
    }

    String getListName(Integer id) {
        List<Map<String, Object>> list = toDoService.listNames();
        for (Map<String, Object> e : list) {
            if (e.get("id").equals(id)) {
                return (String) e.get("name");
            }
        }
        return "missing...";
    }

    @PostMapping("/addList")
    public String addList(@RequestParam(value = "list") String list, Model model) {
        logger.info("Adding list {}", list);
        String createdDate = LocalDateTime.now().format(createdFormatter);
        toDoService.createList(list, createdDate);
        int id = toDoService.findListByCreated(createdDate);

        addAttributes(model, Filter.ALL, Section.LIST, Collections.emptyList(), 1);
        model.addAttribute("list", list);
        model.addAttribute("list_id", id);
        model.addAttribute("title", list);
        return "index";
    }

    @GetMapping(value = {"/{tab}", "/{tab}/{filter}", "/{tab}/{filter}/{page}"})
    public String getTodos(Model model, @PathVariable(value = "tab") String tab,
                           @PathVariable(value = "filter", required = false) String filter,
                           @PathVariable(value = "page", required = false) Integer page) {
        logger.info("Getting toDos: ");
        Section section1 = Section.parse(tab);
        List<ToDo> toDos = getToDos(tab);
        Filter filter1 = Filter.parse(filter);
        toDos = Filter.filter(filter1, toDos);
        addAttributes(model, filter1, section1, toDos, page);
        return "index";
    }

    @GetMapping(value = {"/list/{id}", "/list/{id}/{filter}", "/list/{id}/{filter}/{page}"})
    public String list(Model model,
                       @PathVariable(value = "id") Integer id,
                       @PathVariable(value = "filter", required = false) String filter,
                       @PathVariable(value = "page", required = false) Integer page) {
        logger.info("Finding tasks for list {}", id);
        List<ToDo> toDos = toDoService.listItems(id);
        Filter filter1 = Filter.parse(filter);
        toDos = Filter.filter(filter1, toDos);
        addAttributes(model, filter1, Section.LIST, toDos, page);
        model.addAttribute("list_id", id);
        model.addAttribute("title", getListName(id));
        return "index";
    }

    List<ToDo> getToDos(String tab) {
        try{

        switch (tab) {
            case "planned" -> {
                LocalDateTime start = LocalDateTime.now();
                LocalDateTime end = LocalDateTime.now().plusYears(100L);
                return toDoService.findByDate(Timestamp.valueOf(start), Timestamp.valueOf(end));
            }
            case "myday" -> {
                LocalDateTime start = LocalDateTime.now();
                LocalDateTime end = LocalDateTime.now().plusDays(1L);
                return toDoService.findByDate(Timestamp.valueOf(start), Timestamp.valueOf(end));
            }
            case "important" -> {
                return toDoService.filter(Collections.singletonMap("important", 1));
            }
            default -> {
                return toDoService.all();
            }
        }
        } catch(Exception e) {
            logger.error("Error getting todos", e);
            return Collections.emptyList();
        }
    }
}
