package main.controller;

import main.dto.TaskModel;
import main.mapper.TaskMapper;
import main.repository.UserRepository;
import main.service.TaskService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ApiController {

    private final TaskService service;

    private final UserRepository userRepository;

    public ApiController(TaskService service, UserRepository userRepository) {
        this.service = service;
        this.userRepository = userRepository;
    }

    @GetMapping("/api/tasks")
    public List<TaskModel> getTasks() {
        return service.findAll().stream().map(TaskMapper::map).collect(Collectors.toList());
    }

    @GetMapping("/api/tasks/{id}")           // а вдруг мы изменим фронт
    public TaskModel getTaskById(@PathVariable Long id) {
        return TaskMapper.map(service.findById(id));
    }

    @PostMapping("/api/tasks")
    public TaskModel addTask(@Valid @RequestBody TaskModel task) {
        return TaskMapper.map(service.save(TaskMapper.reverseMap(task, userRepository)));
    }

    @PutMapping("/api/tasks")
    public TaskModel editTask(@Valid @RequestBody TaskModel task) {
        return TaskMapper.map(service.save(TaskMapper.reverseMap(task, userRepository)));
    }

    @DeleteMapping("/api/tasks/{id}")
    public void deleteTaskById(@PathVariable Long id) {
        service.deleteById(id);
    }

    @DeleteMapping("/api/tasks")
    public void deleteTasks() {
        service.deleteAll();
    }
}