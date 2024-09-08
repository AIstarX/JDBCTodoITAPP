package com.todoit;

import com.todoit.dao.PersonDAO;
import com.todoit.dao.TodoItemDAO;
import com.todoit.model.People;
import com.todoit.model.TodoItem;

import java.time.LocalDate;
import java.util.List;

public class App {
    public static void main(String[] args) {
        PersonDAO personDAO = new PersonDAO();
        TodoItemDAO todoItemDAO = new TodoItemDAO();

        try {

            List<People> persons = personDAO.getAllPersons();
            if (persons.isEmpty()) {
                System.out.println("there are no people in the database.");
                return;
            }
            persons.forEach(System.out::println);

            People newPerson = persons.get(0);
            TodoItem newItem = new TodoItem(
                    0,
                    "Test Task",
                    "This is a test description",
                    LocalDate.of(2024, 12, 31),
                    false,
                    newPerson
            );
            todoItemDAO.createTodoItem(newItem);

            List<TodoItem> todoItems = todoItemDAO.getAllTodoItems();
            if (todoItems.isEmpty()) {
                System.out.println("There are no tasks in the database.");
                return;
            }
            todoItems.forEach(System.out::println);

            TodoItem itemToUpdate = todoItems.get(0);
            itemToUpdate.setDone(true);
            todoItemDAO.updateTodoItem(itemToUpdate);

            People personToDelete = persons.get(0);
            personDAO.deletePersonWithTasks(personToDelete.getPersonId());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
