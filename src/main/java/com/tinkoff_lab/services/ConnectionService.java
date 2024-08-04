package com.tinkoff_lab.services;


import com.tinkoff_lab.config.AppConfig;
import com.tinkoff_lab.exceptions.DatabaseConnectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Service
public class ConnectionService {    // separate small class for establishing connection with database
    private AppConfig config;
    private Logger logger = LoggerFactory.getLogger(ConnectionService.class);

    @Autowired
    public ConnectionService(AppConfig config) {
        this.config = config;
    }

    public Connection getConnection(){
        try {
            return DriverManager.getConnection(    //getting connection and sending to DAO
                    config.getDatabaseURL(),
                    config.getDatabaseUsername(),
                    config.getDatabasePassword());
        } catch (SQLException e) {
            logger.error("Something goes wrong with connection to database!");
            throw new DatabaseConnectionException("Something goes wrong with connection to database!");
        }
    }
}
