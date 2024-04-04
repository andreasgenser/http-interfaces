package dev.genser.httpinterfaces.controller;

import dev.genser.httpinterfaces.client.TodoClient;
import dev.genser.httpinterfaces.model.Todo;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoClient todoClient;

    @GetMapping
    public List<Todo> getAllTodos() {
        return todoClient.getAllTodos();
    }

    @GetMapping("/{id}")
    public Todo getTodoById(@PathVariable final long id) {
        return todoClient.getTodoById(id).orElseThrow();
    }
}
