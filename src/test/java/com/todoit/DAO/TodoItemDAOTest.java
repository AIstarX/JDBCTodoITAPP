
package com.todoit.DAO;

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

public class TodoItemDAOTest {

    private TodoItemDAO todoItemDAO;
    private PersonDAO personDAO;

    @BeforeEach
    public void setUp() {
        todoItemDAO = new TodoItemDAO();
        personDAO = new PersonDAO();

        People testPerson = new People(0, "Test", "User");
        personDAO.createPerson(testPerson);
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCreateTodoItem() {
        People assignee = personDAO.getAllPersons().stream()
                .filter(p -> "Test".equals(p.getFirstName()) && "User".equals(p.getLastName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Test user not found"));

        TodoItem newTodo = new TodoItem(
                0,
                "New Task",
                "This is a test task",
                LocalDate.now().plusDays(7),
                false,
                assignee
        );

        todoItemDAO.createTodoItem(newTodo);

        List<TodoItem> todoItems = todoItemDAO.getAllTodoItems();
        assertTrue(todoItems.stream().anyMatch(todo -> "New Task".equals(todo.getTitle()) && "This is a test task".equals(todo.getDescription())));
    }

    @Test
    public void testGetAllTodoItems() {
        List<TodoItem> todoItems = todoItemDAO.getAllTodoItems();
        assertNotNull(todoItems);
        assertTrue(todoItems.size() > 0);
    }

    @Test
    public void testUpdateTodoItem() {
        List<TodoItem> todoItems = todoItemDAO.getAllTodoItems();
        assertFalse(todoItems.isEmpty());  // Asumimos que hay al menos un item de tarea en la base de datos

        TodoItem itemToUpdate = todoItems.get(0);  // Obtener el primer item de tarea de la lista
        itemToUpdate.setDone(true);
        todoItemDAO.updateTodoItem(itemToUpdate);

        // Verificar que el item de tarea ha sido actualizado
        TodoItem updatedItem = todoItemDAO.getAllTodoItems().stream()
                .filter(todo -> todo.getTodoId() == itemToUpdate.getTodoId())
                .findFirst()
                .orElse(null);
        assertNotNull(updatedItem);
        assertTrue(updatedItem.isDone());
    }

    @Test
    public void testDeleteTodoItem() {
        // Crear un nuevo item de tarea para eliminar
        People assignee = personDAO.getAllPersons().stream()
                .filter(p -> "Test".equals(p.getFirstName()) && "User".equals(p.getLastName()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Test user not found"));

        TodoItem itemToDelete = new TodoItem(
                0,
                "Temp Task",
                "Temporary task for deletion",
                LocalDate.now().plusDays(7),
                false,
                assignee
        );
        todoItemDAO.createTodoItem(itemToDelete);

        // Verificar que el item de tarea fue creado
        List<TodoItem> todoItems = todoItemDAO.getAllTodoItems();
        TodoItem fetchedItem = todoItems.stream().filter(todo -> "Temp Task".equals(todo.getTitle()) && "Temporary task for deletion".equals(todo.getDescription())).findFirst().orElse(null);
        assertNotNull(fetchedItem);

        // Eliminar el item de tarea
        todoItemDAO.updateTodoItem(fetchedItem);

        // Verificar que el item de tarea fue eliminado
        TodoItem deletedItem = todoItemDAO.getAllTodoItems().stream()
                .filter(todo -> todo.getTodoId() == fetchedItem.getTodoId())
                .findFirst()
                .orElse(null);
        assertNull(deletedItem);
    }
}
