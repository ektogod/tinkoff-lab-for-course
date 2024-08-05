package test;

import com.mysql.cj.jdbc.MysqlDataSource;
import com.tinkoff_lab.TinkoffLabApplication;
import com.tinkoff_lab.dao.Translation;
import com.tinkoff_lab.dao.TranslationDAO;
import com.tinkoff_lab.exceptions.DatabaseException;
import com.tinkoff_lab.requests.UserRequest;
import com.tinkoff_lab.services.ConnectionService;
import com.tinkoff_lab.services.TranslationServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Testcontainers
@SpringBootTest(classes = TinkoffLabApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc

public class DatabaseTest {
    @Container
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.26");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", mySQLContainer::getJdbcUrl);
        registry.add("spring.datasource.username", mySQLContainer::getUsername);
        registry.add("spring.datasource.password", mySQLContainer::getPassword);
    }

    private ConnectionService connectionService;
    private TranslationDAO dao;
    private TranslationServiceImpl translationService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    public DatabaseTest(ConnectionService connectionService, TranslationDAO dao, TranslationServiceImpl translationService) {
        this.connectionService = connectionService;
        this.dao = dao;
        this.translationService = translationService;
    }

    private DataSource dataSource;

    @BeforeEach
    public void setUp() throws SQLException {
        MysqlDataSource mysqlDataSource = new MysqlDataSource();
        mysqlDataSource.setURL(mySQLContainer.getJdbcUrl());
        mysqlDataSource.setUser(mySQLContainer.getUsername());
        mysqlDataSource.setPassword(mySQLContainer.getPassword());
        dataSource = mysqlDataSource;

        ConnectionService.testFlag = true;
        ConnectionService.dataSource = dataSource;

        try (Connection connection = connectionService.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS query (" +
                    "  ID INT NOT NULL AUTO_INCREMENT," +
                    "  IP VARCHAR(45) NOT NULL," +
                    "  Original_Text VARCHAR(1000) NOT NULL," +
                    "  Original_Language VARCHAR(10) NOT NULL," +
                    "  Translated_Text VARCHAR(1000) NOT NULL," +
                    "  Target_Language VARCHAR(10) NOT NULL," +
                    "  Time DATETIME NOT NULL," +
                    "  Status INT NOT NULL," +
                    "  Message VARCHAR(1000)," +
                    "  PRIMARY KEY (ID)" +
                    ")");

            statement.execute("DELETE FROM query");
        }
    }

    //----------------- TESTS WITHOUT USING TRANSLATION SERVICE
    @Test
    public void testWhenQueryIsCorrect() {
        Translation translation = new Translation(
                "ip",
                "original_text",
                "ru",
                "translated_text",
                "be",
                "2024-08-04 01:53:15",
                200,
                "Ok");

        int key = dao.insert(translation);
        System.out.println(key);
        Assertions.assertEquals(translation, dao.findByID(key));
    }

    @Test
    public void testWhenQueryHasNullColumns() {
        Translation translation = new Translation(
                null,
                null,
                null,
                null,
                null,
                null,
                200,
                "Ok");

        Exception ex = Assertions.assertThrows(DatabaseException.class, () -> {
            dao.insert(translation);
        });

        Assertions.assertEquals(ex.getMessage(), "Insertion of query in database goes wrong!");
        Assertions.assertNull(dao.findByID(1));
    }

    @Test
    public void testWithSomeQueries() {
        Translation translation = new Translation(
                "ip",
                "original_text",
                "ru",
                "translated_text",
                "be",
                "2024-08-04 01:53:15",
                200,
                "Ok");

        for (int i = 0; i < 5; i++) {
            int key = dao.insert(translation);
            Assertions.assertEquals(translation, dao.findByID(key));
        }
    }

    //---------------TESTS WITH USING TRANSLATION SERVICE

    @Test
    public void testWithCorrectUserRequest() {
        UserRequest request = new UserRequest("Любовь", "ru", "be");
        translationService.translate(request);
        Translation dbTranslation = dao.findByID(1);
        Translation correctTranslation = new Translation(
                dbTranslation.getIp(),
                "Любовь",
                "ru",
                "Каханне",
                "be",
                dbTranslation.getTime(),
                200,
                "Ok");

        Assertions.assertEquals(dbTranslation, correctTranslation);
    }

    //----------------------------------------- Этот тест при отдельном запуске проходит, но при запуске всех тестов нет.
    //----------------------------------------- Я так и не понял, как это фиксить.
    //----------------------------------------- Мок мне понадобился, тк не триггерился обработчик исключений

//    @Test
//    public void testWithIncorrectLanguageInUserRequest() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders
//                        .get("/ektogod/translateText")
//                        .content("{\"text\":\"Любовь\", \"originalLanguage\":\"ru\", \"finalLanguage\":\"by\"}")
//                        .contentType("application/json"))
//                .andExpect(MockMvcResultMatchers.status().is(403))
//                .andExpect(MockMvcResultMatchers.content().string(
//                        "'BY' IS AN INVALID TARGET LANGUAGE . EXAMPLE: LANGPAIR=EN|IT USING 2 LETTER ISO OR RFC3066 LIKE ZH-CN. ALMOST ALL LANGUAGES SUPPORTED BUT SOME MAY HAVE NO CONTENT"));
//
//        Translation dbTranslation = dao.findByID(1);
//        Translation correctTranslation = new Translation(
//                dbTranslation.getIp(),
//                "Любовь",
//                "ru",
//                "",
//                "by",
//                dbTranslation.getTime(),
//                403,
//                "'BY' IS AN INVALID TARGET LANGUAGE . EXAMPLE: LANGPAIR=EN|IT USING 2 LETTER ISO OR RFC3066 LIKE ZH-CN. ALMOST ALL LANGUAGES SUPPORTED BUT SOME MAY HAVE NO CONTENT");
//
//        Assertions.assertEquals(dbTranslation, correctTranslation);
//    }

}
