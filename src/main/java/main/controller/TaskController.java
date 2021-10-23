package main.controller;

import main.dto.TaskModel;
import main.mapper.TaskMapper;
import main.repository.UserRepository;
import main.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class TaskController {

    private final TaskService service;

    private final UserRepository userRepository;

    public TaskController(TaskService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @GetMapping("/")
    public String start(Model model) {
        List<TaskModel> tasks = service.findAll().stream().map(TaskMapper::map).collect(Collectors.toList());
        model.addAttribute("tasks", tasks);
        return "index";
    }

    @GetMapping("/tasks")
    public String getTasks(Model model) {
        List<TaskModel> tasks = service.findAll().stream().map(TaskMapper::map).collect(Collectors.toList());
        model.addAttribute("tasks", tasks);
        return "index";
    }

    @PostMapping("/tasks")
    public String addTask(@Valid @RequestBody TaskModel task, Model model) {
        service.save(TaskMapper.reverseMap(task, userRepository));
        return getTasks(model);
    }

    @PutMapping("/tasks")
    public String editTask(@Valid @RequestBody TaskModel task, Model model) {
        service.save(TaskMapper.reverseMap(task, userRepository));
        return getTasks(model);
    }

    @DeleteMapping("/tasks/{id}")
    public String deleteTaskById(@PathVariable Long id, Model model) {
        service.deleteById(id);
        return getTasks(model);
    }

    @DeleteMapping("/tasks")
    public String deleteTasks(Model model) {
        service.deleteAll();
        return getTasks(model);
    }
}