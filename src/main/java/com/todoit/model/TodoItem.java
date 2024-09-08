package com.todoit.model;

import java.time.LocalDate;

public class TodoItem {
    private int todoId;
    private String title;
    private String description;
    private LocalDate deadline;
    private boolean done;
    private People assignee;

    public TodoItem(int todoId, String title, String description, LocalDate deadline, boolean done, People assignee) {
        this.todoId = todoId;
        this.title = title;
        this.description = description;
        this.deadline = deadline;
        this.done = done;
        this.assignee = assignee;
    }

    public void setDone(boolean done) {
        this.done = done;
    }


    public int getTodoId() {
        return todoId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public boolean isDone() {
        return done;
    }

    public People getAssignee() {
        return assignee;
    }

    @Override
    public String toString() {
        return "TodoItem{" +
                "todoId=" + todoId +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", deadline=" + deadline +
                ", done=" + done +
                ", assignee=" + assignee +
                '}';
    }
}
