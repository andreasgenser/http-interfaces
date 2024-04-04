package dev.genser.httpinterfaces.model;

public record Todo(
        Integer id,
        Integer userId,
        String title,
        boolean completed
) {
}
