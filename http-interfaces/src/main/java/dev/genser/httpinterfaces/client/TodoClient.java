package dev.genser.httpinterfaces.client;

import dev.genser.httpinterfaces.model.Todo;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;
import java.util.Optional;

public interface TodoClient {

    @GetExchange("/todos")
    List<Todo> getAllTodos();

    @GetExchange("/todos/{id}")
    Optional<Todo> getTodoById(@PathVariable final long id);

}
