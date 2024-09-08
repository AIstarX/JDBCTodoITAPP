package com.todoit.dao;

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

public class PersonDAO {

    private static final Logger logger = LoggerFactory.getLogger(PersonDAO.class);

    public List<People> getAllPersons() {
        List<People> persons = new ArrayList<>();
        String query = "SELECT * FROM person";

        try (Connection connection = DataBaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                People person = new People(
                        resultSet.getInt("person_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name")
                );
                persons.add(person);
            }
        } catch (SQLException e) {
            logger.error("Error retrieving all persons", e);
        }

        return persons;
    }

    public People getPersonById(int personId) {
        String query = "SELECT * FROM person WHERE person_id = ?";
        try (Connection connection = DataBaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, personId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return new People(
                        resultSet.getInt("person_id"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name")
                );
            }
        } catch (SQLException e) {
            logger.error("Error retrieving person by ID", e);
        }
        return null;
    }

    public void createPerson(People person) {
        String query = "INSERT INTO person (first_name, last_name) VALUES (?, ?)";
        try (Connection connection = DataBaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, person.getFirstName());
            statement.setString(2, person.getLastName());
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error creating person", e);
        }
    }

    public void updatePerson(People person) {
        String query = "UPDATE person SET first_name = ?, last_name = ? WHERE person_id = ?";
        try (Connection connection = DataBaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, person.getFirstName());
            statement.setString(2, person.getLastName());
            statement.setInt(3, person.getPersonId());
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error updating person", e);
        }
    }

    public void deletePerson(int personId) {
        String query = "DELETE FROM person WHERE person_id = ?";
        try (Connection connection = DataBaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setInt(1, personId);
            statement.executeUpdate();

        } catch (SQLException e) {
            logger.error("Error deleting person", e);
        }
    }

    public void deletePersonWithTasks(int personId) {
        String deleteTasksQuery = "DELETE FROM todo_item WHERE assignee_id = ?";
        String deletePersonQuery = "DELETE FROM person WHERE person_id = ?";

        try (Connection connection = DataBaseManager.getConnection()) {
            connection.setAutoCommit(false);

            try (PreparedStatement deleteTasksStmt = connection.prepareStatement(deleteTasksQuery);
                 PreparedStatement deletePersonStmt = connection.prepareStatement(deletePersonQuery)) {

                deleteTasksStmt.setInt(1, personId);
                deleteTasksStmt.executeUpdate();

                deletePersonStmt.setInt(1, personId);
                deletePersonStmt.executeUpdate();

                connection.commit();

            } catch (SQLException e) {
                connection.rollback();
                logger.error("Error deleting person with tasks", e);
            }
        } catch (SQLException e) {
            logger.error("Error establishing connection for deletion", e);
        }
    }
}
