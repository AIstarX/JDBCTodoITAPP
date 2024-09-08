package com.todoit;

import com.todoit.dao.PersonDAO;
import com.todoit.dao.TodoItemDAO;
import com.todoit.model.People;
import com.todoit.model.TodoItem;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppTest {

    private PersonDAO personDAO;
    private TodoItemDAO todoItemDAO;

    @BeforeEach
    void setUp() {
        personDAO = new PersonDAO();
        todoItemDAO = new TodoItemDAO();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void testCreateAndRetrievePerson() {
        People person = new People(0, "Carlos", "Sánchez");
        personDAO.createPerson(person);

        List<People> persons = personDAO.getAllPersons();
        assertNotNull(persons);
        assertTrue(persons.size() > 0);
        assertTrue(persons.stream().anyMatch(p -> "Carlos".equals(p.getFirstName()) && "Sánchez".equals(p.getLastName())));
    }

    @Test
    void testCreateAndRetrieveTodoItem() {
        People person = new People(0, "Sven", "Johansson");
        personDAO.createPerson(person);
        List<People> persons = personDAO.getAllPersons();
        People savedPerson = persons.stream().filter(p -> "Sven".equals(p.getFirstName()) && "Johansson".equals(p.getLastName())).findFirst().orElse(null);
        assertNotNull(savedPerson);

        TodoItem item = new TodoItem(0, "Review PR", "Review the pull request for the new feature", LocalDate.now().plusDays(1), false, savedPerson);
        todoItemDAO.createTodoItem(item);

        List<TodoItem> todoItems = todoItemDAO.getAllTodoItems();
        assertNotNull(todoItems);
        assertTrue(todoItems.size() > 0);
        assertTrue(todoItems.stream().anyMatch(t -> "Review PR".equals(t.getTitle())));
    }

    @Test
    void testUpdateTodoItem() {
        People person = new People(0, "Tony", "Luna");
        personDAO.createPerson(person);
        List<People> persons = personDAO.getAllPersons();
        People savedPerson = persons.stream().filter(p -> "Tony".equals(p.getFirstName()) && "Luna".equals(p.getLastName())).findFirst().orElse(null);
        assertNotNull(savedPerson);

        TodoItem item = new TodoItem(0, "Initial Task", "This is the initial task", LocalDate.now().plusDays(1), false, savedPerson);
        todoItemDAO.createTodoItem(item);

        List<TodoItem> todoItems = todoItemDAO.getAllTodoItems();
        TodoItem itemToUpdate = todoItems.stream().filter(t -> "Initial Task".equals(t.getTitle())).findFirst().orElse(null);
        assertNotNull(itemToUpdate);

        itemToUpdate.setDone(true);
        todoItemDAO.updateTodoItem(itemToUpdate);

        List<TodoItem> updatedItems = todoItemDAO.getAllTodoItems();
        assertTrue(updatedItems.stream().anyMatch(t -> t.isDone() && "Initial Task".equals(t.getTitle())));
    }
}
