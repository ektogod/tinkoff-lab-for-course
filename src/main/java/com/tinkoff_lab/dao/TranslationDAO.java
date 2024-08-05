package com.tinkoff_lab.dao;

import com.tinkoff_lab.exceptions.DatabaseException;
import com.tinkoff_lab.services.ConnectionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.*;

@Component
public class TranslationDAO implements DAO<Translation> {            // class for saving records in database
    private ConnectionService connectionService;
    private Logger logger = LoggerFactory.getLogger(TranslationDAO.class);

    @Autowired
    public TranslationDAO(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public int insert(Translation entity) {
        logger.info("Start writing to database of query: {}", entity);

        String sql = "INSERT INTO query (IP, Original_Text, Original_Language, Translated_Text, Target_Language, Time, Status, Message) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection connection = connectionService.getConnection()) {    // getting connection with db
            logger.info("Connection with database established");
            PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            statement.setString(1, entity.getIp());  // preparing the statement
            statement.setString(2, entity.getOriginalText());
            statement.setString(3, entity.getOriginalLang());
            statement.setString(4, entity.getTranslatedText());
            statement.setString(5, entity.getTargetLang());
            statement.setString(6, entity.getTime());
            statement.setInt(7, entity.getStatus());
            statement.setString(8, entity.getMessage());

            statement.executeUpdate();  // sending to db

            ResultSet generatedKeys = statement.getGeneratedKeys();
            int key;
            if (generatedKeys != null && generatedKeys.next()) {
                key = generatedKeys.getInt(1); // getting ID
            } else {
                logger.error("Creating translation failed, no ID obtained. Translation: {}", entity);
                throw new DatabaseException("Creating translation failed, no ID obtained.");
            }

            logger.info("Query has been successfully recorded");
            return key;
        } catch (SQLException ex) {
            logger.error("Insertion of query in database goes wrong!");
            throw new DatabaseException("Insertion of query in database goes wrong!");
        }
    }

    @Override       // this method I use in test class
    public Translation findByID(int id) {
        String sql = "SELECT * FROM query WHERE ID = ?";
        try (Connection connection = connectionService.getConnection()) {    // getting connection with db
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet != null && resultSet.next()) {
                return new Translation(
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getString(4),
                        resultSet.getString(5),
                        resultSet.getString(6),
                        resultSet.getString(7),
                        resultSet.getInt(8),
                        resultSet.getString(9));
            }

        } catch (SQLException ex) {
            throw new DatabaseException("Getting query by id <" + id + "> goes wrong!");
        }

        return null;
    }
}
