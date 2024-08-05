package com.tinkoff_lab.services;


import com.tinkoff_lab.config.AppConfig;
import com.tinkoff_lab.exceptions.DatabaseConnectionException;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


@Service
public class ConnectionService {    // separate small class for establishing connection with database
    private AppConfig config;
    private Logger logger = LoggerFactory.getLogger(ConnectionService.class);

    public static boolean testFlag = false;  // это костыли. я просто не знал, как по-другому в тестах получать
    public static DataSource dataSource;  // соединение не от драйвера, а от DataSource

    @Autowired
    public ConnectionService(AppConfig config) {
        this.config = config;
    }

    public Connection getConnection() {
        try {
            if(!testFlag) {
                return DriverManager.getConnection(    //getting connection and sending to DAO
                        config.getDatabaseURL(),
                        config.getDatabaseUsername(),
                        config.getDatabasePassword());
            }
            else if(dataSource != null) {
                return getConnection(dataSource);
            }
        } catch (SQLException e) {
            logger.error("Something goes wrong with connection to database!");
            throw new DatabaseConnectionException("Something goes wrong with connection to database!");
        }

        return null;
    }

    private Connection getConnection(DataSource dataSource) {   // this method will be used in test class
        try {
            return dataSource.getConnection();   //getting connection and sending to DAO
        } catch (SQLException e) {
            logger.error("Something goes wrong with connection to database!");
            throw new DatabaseConnectionException("Something goes wrong with connection to database!");
        }
    }
}
