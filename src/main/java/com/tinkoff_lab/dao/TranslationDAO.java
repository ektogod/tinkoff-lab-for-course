package com.tinkoff_lab.dao;

import com.tinkoff_lab.exceptions.DatabaseException;
import com.tinkoff_lab.services.ConnectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Component
public class TranslationDAO implements DAO<Translation>{
    private ConnectionService connectionService;

    @Autowired
    public TranslationDAO(ConnectionService connectionService) {
        this.connectionService = connectionService;
    }

    @Override
    public void insert(Translation entity) {
        String sql = "INSERT INTO query (IP, Original_Text, Original_Language, Translated_Text, Target_Language, Time, Status) VALUES(?, ?, ?, ?, ?, ?, ?)";
        try(Connection connection = connectionService.getConnection()){
            PreparedStatement statement = connection.prepareStatement(sql);

            statement.setString(1, entity.getIp());
            statement.setString(2, entity.getOriginalText());
            statement.setString(3, entity.getOriginalLang());
            statement.setString(4, entity.getTranslatedText());
            statement.setString(5, entity.getTargetLang());
            statement.setString(6, entity.getTime());
            statement.setInt(7, entity.getStatus());

            statement.executeUpdate();
        }
        catch (SQLException ex){
            throw new DatabaseException("Insertion of query in database goes wrong!");
        }
    }
}
