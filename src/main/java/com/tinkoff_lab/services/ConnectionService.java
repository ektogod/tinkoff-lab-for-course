package com.tinkoff_lab.services;


import com.tinkoff_lab.config.AppConfig;
import com.tinkoff_lab.exceptions.DatabaseConnectionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Service
public class ConnectionService {
    private AppConfig config;

    @Autowired
    public ConnectionService(AppConfig config) {
        this.config = config;
    }

    public Connection getConnection(){
        try {
            return DriverManager.getConnection(
                    config.getDatabaseURL(),
                    config.getDatabaseUsername(),
                    config.getDatabasePassword());
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Something goes wrong with connection to database!");
        }
    }
}
