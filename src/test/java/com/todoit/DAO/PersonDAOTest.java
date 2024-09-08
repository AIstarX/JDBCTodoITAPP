package com.todoit.DAO;

import com.todoit.dao.PersonDAO;
import com.todoit.model.People;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PersonDAOTest {

    private PersonDAO personDAO;

    @BeforeEach
    public void setUp() {
        personDAO = new PersonDAO();
    }

    @AfterEach
    public void tearDown() {
    }

    @Test
    public void testCreatePerson() {
        People newPerson = new People(0, "Carlos", "Sánchez");
        personDAO.createPerson(newPerson);

        List<People> persons = personDAO.getAllPersons();
        assertTrue(persons.stream().anyMatch(p -> "Carlos".equals(p.getFirstName()) && "Sánchez".equals(p.getLastName())));
    }

    @Test
    public void testGetAllPersons() {
        List<People> persons = personDAO.getAllPersons();
        assertNotNull(persons);
        assertTrue(persons.size() > 0);
    }

    @Test
    public void testUpdatePerson() {
        List<People> persons = personDAO.getAllPersons();
        assertFalse(persons.isEmpty());

        People personToUpdate = persons.get(0);
        personToUpdate = new People(personToUpdate.getPersonId(), "Anna", "López");
        personDAO.updatePerson(personToUpdate);

        People updatedPerson = personDAO.getPersonById(personToUpdate.getPersonId());
        assertEquals("Anna", updatedPerson.getFirstName());
        assertEquals("López", updatedPerson.getLastName());
    }

    @Test
    public void testDeletePerson() {
        People personToDelete = new People(0, "Temporal", "User");
        personDAO.createPerson(personToDelete);

        List<People> persons = personDAO.getAllPersons();
        People fetchedPerson = persons.stream().filter(p -> "Temporal".equals(p.getFirstName()) && "User".equals(p.getLastName())).findFirst().orElse(null);
        assertNotNull(fetchedPerson);

        personDAO.deletePerson(fetchedPerson.getPersonId());

        People deletedPerson = personDAO.getPersonById(fetchedPerson.getPersonId());
        assertNull(deletedPerson);
    }
}
