package com.todoit.dao;

import com.todoit.model.TodoItem;
import com.todoit.model.People;
import com.todoit.util.DataBaseManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TodoItemDAO {
    private static final Logger logger = LoggerFactory.getLogger(TodoItemDAO.class);

    // Crear un nuevo item de tarea
    public void createTodoItem(TodoItem todoItem) {
        String query = "INSERT INTO todo_item (title, description, deadline, done, assignee_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DataBaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, todoItem.getTitle());
            statement.setString(2, todoItem.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(todoItem.getDeadline()));
            statement.setBoolean(4, todoItem.isDone());
            statement.setInt(5, todoItem.getAssignee().getPersonId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error creating TodoItem", e);
        }
    }

    public void updateTodoItem(TodoItem todoItem) {
        String query = "UPDATE todo_item SET title = ?, description = ?, deadline = ?, done = ?, assignee_id = ? WHERE todo_id = ?";
        try (Connection connection = DataBaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, todoItem.getTitle());
            statement.setString(2, todoItem.getDescription());
            statement.setDate(3, java.sql.Date.valueOf(todoItem.getDeadline()));
            statement.setBoolean(4, todoItem.isDone());
            statement.setInt(5, todoItem.getAssignee().getPersonId());
            statement.setInt(6, todoItem.getTodoId());
            statement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Error updating TodoItem", e);
        }
    }

    // Obtener todos los items de tarea
    public List<TodoItem> getAllTodoItems() {
        List<TodoItem> todoItems = new ArrayList<>();
        String query = "SELECT * FROM todo_item";
        try (Connection connection = DataBaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            PersonDAO personDAO = new PersonDAO();

            while (resultSet.next()) {
                People assignee = personDAO.getPersonById(resultSet.getInt("assignee_id"));
                todoItems.add(new TodoItem(
                        resultSet.getInt("todo_id"),
                        resultSet.getString("title"),
                        resultSet.getString("description"),
                        resultSet.getDate("deadline").toLocalDate(),
                        resultSet.getBoolean("done"),
                        assignee
                ));
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all TodoItems", e);
        }
        return todoItems;
    }
}
