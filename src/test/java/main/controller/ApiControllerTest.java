package main.controller;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import main.AbstractMainTest;
import main.dto.response.ErrorResponse;
import main.mapper.TaskMapper;
import main.model.Task;
import main.model.User;
import main.model.enums.Role;
import main.repository.TaskRepository;
import main.repository.UserRepository;

public class ApiControllerTest extends AbstractMainTest {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserRepository userRepository;


    private Task task;


    @BeforeEach
    public void setUpTest() {
        super.setup();
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(12);
        User test = new User();
        test.setUsername("test");
        test.setFirstname("Иван");
        test.setLastname("Иванов");
        test.setPassword(passwordEncoder.encode("password"));
        test.setRole(Role.USER);

        userRepository.save(test);
        task = new Task("taskText");
        task.setUser(test);
        taskRepository.save(task);
    }

    @AfterEach
    public void tearDown() {
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    /**
     * Проверяем, что задачи могут быть успешно получены
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(value = "test", authorities = "user:write")
    public void testGetTasksSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/tasks")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getMapper().writeValueAsString(List.of(TaskMapper.map(task)))));
    }

    /**
     * Проверяем, что можем получить задачу по идентификатору
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(value = "test", authorities = "user:write")
    public void testGetTaskByIdSuccess() throws Exception {
        long id = task.getId();   // абракадабра какая-то
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/tasks/{id}", id)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(getMapper().writeValueAsString(TaskMapper.map(task))));
    }

    /**
     * Проверяем, что происходит исключение, если нет задачи с таким id
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(value = "test", authorities = "user:write")
    public void testGetTaskByIdFailure() throws Exception {
        taskRepository.deleteAll();  // для этого теста нужны особые условия
        mockMvc.perform(MockMvcRequestBuilders
                        .get("/api/tasks/{id}", 100)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(getMapper().writeValueAsString(new ErrorResponse("not_found", "Task is not exist"))));
    }

    /**
     * Проверяем, что задачу можно добавить
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(value = "test", authorities = "user:write")
    public void testAddTaskSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/tasks")
                        .content(getMapper().writeValueAsString(new Task("titleText")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").exists());
    }

    /**
     * Проверяем, что приходит код 400 при ошибке валидации запроса при добавлении задачи
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(value = "test", authorities = "user:write")
    public void testAddTaskFailure() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post("/api/tasks")
                        .content(getMapper().writeValueAsString(new Task("")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Проверяем, что задачу можно изменить
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(value = "test", authorities = "user:write")
    public void testEditTaskSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/tasks")
                        .content(getMapper().writeValueAsString(new Task("newTitleText")))  //меняем текст на этот
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title").value("newTitleText")); //проверяем, что сменился
    }

    /**
     * Проверяем, что приходит код 400 при ошибке валидации запроса при изменении задачи
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(value = "test", authorities = "user:write")
    public void testEditTaskFailure() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .put("/api/tasks")
                        .content(getMapper().writeValueAsString(new Task("")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    /**
     * Проверяем, что задачу можно удалить
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(value = "test", authorities = "user:write")
    public void testDeleteTaskByIdSuccess() throws Exception {
        long id = task.getId();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/tasks/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Проверяем, что происходит исключение, если нет задачи с таким id
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(value = "test", authorities = "user:write")
    public void testDeleteTaskByIdFailure() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/tasks/{id}", 100))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().string("Task is not exist"));
    }

    /**
     * Проверяем, что все задачи пользователя можно удалить
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(value = "test", authorities = "user:write")
    public void testDeleteTasksSuccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/tasks"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    /**
     * Проверяем, что происходит исключение, если нет ни одной задачи у пользователя
     *
     * @throws Exception
     */
    @Test
    @WithMockUser(value = "test", authorities = "user:write")
    public void testDeleteTasksFailure() throws Exception {
        taskRepository.deleteAll();
        mockMvc.perform(MockMvcRequestBuilders
                        .delete("/api/tasks"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.content().json(getMapper().writeValueAsString(new ErrorResponse("not_found", "Tasks is not exist"))));
    }
}
