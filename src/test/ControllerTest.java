package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkoff_lab.TinkoffLabApplication;
import com.tinkoff_lab.dao.TranslationDAO;
import com.tinkoff_lab.dto.Translation;
import com.tinkoff_lab.dto.requests.UserRequest;
import com.tinkoff_lab.dto.responses.ErrorResponse;
import com.tinkoff_lab.dto.responses.UserResponse;
import com.tinkoff_lab.services.ConnectionService;
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

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@SpringBootTest(classes = TinkoffLabApplication.class)
@TestPropertySource(locations = "classpath:application.properties")
@AutoConfigureMockMvc
@Testcontainers

public class ControllerTest {
    private final MockMvc mockMvc;
    private final ConnectionService connectionService;
    private final TranslationDAO dao;

    @Autowired
    public ControllerTest(MockMvc mockMvc, ConnectionService connectionService, TranslationDAO dao) {
        this.mockMvc = mockMvc;
        this.connectionService = connectionService;
        this.dao = dao;
    }

    private final ObjectMapper mapper = new ObjectMapper();

    @Container
    private static final MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.26");

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("database.url", mySQLContainer::getJdbcUrl);
        registry.add("database.username", mySQLContainer::getUsername);
        registry.add("database.password", mySQLContainer::getPassword);
    }

    @BeforeEach
    public void setUp() throws SQLException {
        try (Connection connection = connectionService.getConnection()) {
            Statement statement = connection.createStatement();
            statement.execute("CREATE TABLE IF NOT EXISTS query (" +
                    "  ID INT NOT NULL AUTO_INCREMENT," +
                    "  IP VARCHAR(45) NOT NULL," +
                    "  Original_Text VARCHAR(1000)," +
                    "  Original_Language VARCHAR(10)," +
                    "  Translated_Text VARCHAR(1000)," +
                    "  Target_Language VARCHAR(10)," +
                    "  Time DATETIME NOT NULL," +
                    "  Status INT NOT NULL," +
                    "  Message VARCHAR(1000)," +
                    "  PRIMARY KEY (ID)" +
                    ")");

            statement.execute("DELETE FROM query");
            statement.execute("ALTER TABLE query AUTO_INCREMENT = 1");
        }
    }

    @Test
    public void testWhenResponseIsCorrect() throws Exception {
        UserRequest request = new UserRequest("Любовь", "ru", "en");
        UserResponse response = new UserResponse("Love");
        compareResponses(request, mapper.writeValueAsString(response), 200);

        Translation dbTranslation = dao.findByID(1);
        Translation correctTranslation = new Translation(
                dbTranslation.ip(),
                "Любовь",
                "ru",
                "Love",
                "en",
                dbTranslation.time(),
                200,
                "Ok");

        Assertions.assertEquals(dbTranslation, correctTranslation);
    }

    @Test
    public void testWhereTextIsEmpty() throws Exception {
        UserRequest request = new UserRequest("", "ru", "en");
        ErrorResponse errorResponse = new ErrorResponse("NO QUERY SPECIFIED. EXAMPLE REQUEST: GET?Q=HELLO&LANGPAIR=EN|IT", 403);
        compareResponses(request, mapper.writeValueAsString(errorResponse), 403);

        Translation dbTranslation = dao.findByID(1);
        Translation correctTranslation = new Translation(
                dbTranslation.ip(),
                "",
                "ru",
                "",
                "en",
                dbTranslation.time(),
                403,
                "NO QUERY SPECIFIED. EXAMPLE REQUEST: GET?Q=HELLO&LANGPAIR=EN|IT");

        Assertions.assertEquals(dbTranslation, correctTranslation);
    }

    @Test
    public void testWhereOriginalLangIsEmpty() throws Exception {
        UserRequest request = new UserRequest("a", "", "en");
        ErrorResponse errorResponse = new ErrorResponse("INVALID LANGUAGE PAIR SPECIFIED. EXAMPLE: LANGPAIR=EN|IT USING 2 LETTER ISO OR RFC3066 LIKE ZH-CN. ALMOST ALL LANGUAGES SUPPORTED BUT SOME MAY HAVE NO CONTENT", 403);
        compareResponses(request, mapper.writeValueAsString(errorResponse), 403);

        Translation dbTranslation = dao.findByID(1);
        Translation correctTranslation = new Translation(
                dbTranslation.ip(),
                "a",
                "",
                "",
                "en",
                dbTranslation.time(),
                403,
                "INVALID LANGUAGE PAIR SPECIFIED. EXAMPLE: LANGPAIR=EN|IT USING 2 LETTER ISO OR RFC3066 LIKE ZH-CN. ALMOST ALL LANGUAGES SUPPORTED BUT SOME MAY HAVE NO CONTENT");

        Assertions.assertEquals(dbTranslation, correctTranslation);
    }

    @Test
    public void testWhereFinalLangIsEmpty() throws Exception {
        UserRequest request = new UserRequest("a", "ru", "");
        ErrorResponse errorResponse = new ErrorResponse("INVALID LANGUAGE PAIR SPECIFIED. EXAMPLE: LANGPAIR=EN|IT USING 2 LETTER ISO OR RFC3066 LIKE ZH-CN. ALMOST ALL LANGUAGES SUPPORTED BUT SOME MAY HAVE NO CONTENT", 403);
        compareResponses(request, mapper.writeValueAsString(errorResponse), 403);

        Translation dbTranslation = dao.findByID(1);
        Translation correctTranslation = new Translation(
                dbTranslation.ip(),
                "a",
                "ru",
                "",
                "",
                dbTranslation.time(),
                403,
                "INVALID LANGUAGE PAIR SPECIFIED. EXAMPLE: LANGPAIR=EN|IT USING 2 LETTER ISO OR RFC3066 LIKE ZH-CN. ALMOST ALL LANGUAGES SUPPORTED BUT SOME MAY HAVE NO CONTENT");

        Assertions.assertEquals(dbTranslation, correctTranslation);
    }

    @Test
    public void testWhereTextIsNull() throws Exception {
        UserRequest request = new UserRequest(null, "ru", "en");
        ErrorResponse errorResponse = new ErrorResponse("Translation went wrong because something from parameters is null!", 500);
        compareResponses(request, mapper.writeValueAsString(errorResponse), 500);

        Translation dbTranslation = dao.findByID(1);
        Translation correctTranslation = new Translation(
                dbTranslation.ip(),
                null,
                "ru",
                "",
                "en",
                dbTranslation.time(),
                500,
                "Translation went wrong because something from parameters is null!");

        Assertions.assertEquals(dbTranslation, correctTranslation);
    }

    @Test
    public void testWhereOriginalLangIsNull() throws Exception {

        UserRequest request = new UserRequest("a", null, "en");
        ErrorResponse errorResponse = new ErrorResponse("Translation went wrong because something from parameters is null!", 500);
        compareResponses(request, mapper.writeValueAsString(errorResponse), 500);

        Translation dbTranslation = dao.findByID(1);
        Translation correctTranslation = new Translation(
                dbTranslation.ip(),
                "a",
                null,
                "",
                "en",
                dbTranslation.time(),
                500,
                "Translation went wrong because something from parameters is null!");

        Assertions.assertEquals(dbTranslation, correctTranslation);
    }

    @Test
    public void testWhereFinalLangIsNull() throws Exception {
        UserRequest request = new UserRequest("a", "ru", null);
        ErrorResponse errorResponse = new ErrorResponse("Translation went wrong because something from parameters is null!", 500);
        compareResponses(request, mapper.writeValueAsString(errorResponse), 500);

        Translation dbTranslation = dao.findByID(1);
        Translation correctTranslation = new Translation(
                dbTranslation.ip(),
                "a",
                "ru",
                "",
                null,
                dbTranslation.time(),
                500,
                "Translation went wrong because something from parameters is null!");

        Assertions.assertEquals(dbTranslation, correctTranslation);
    }

    @Test
    public void testWhereLanguageIsIncorrect() throws Exception {
        UserRequest request = new UserRequest("любовь", "ru", "by");
        ErrorResponse errorResponse = new ErrorResponse("'BY' IS AN INVALID TARGET LANGUAGE . EXAMPLE: LANGPAIR=EN|IT USING 2 LETTER ISO OR RFC3066 LIKE ZH-CN. ALMOST ALL LANGUAGES SUPPORTED BUT SOME MAY HAVE NO CONTENT", 403);
        compareResponses(request, mapper.writeValueAsString(errorResponse), 403);

        Translation dbTranslation = dao.findByID(1);
        Translation correctTranslation = new Translation(
                dbTranslation.ip(),
                "любовь",
                "ru",
                "",
                "by",
                dbTranslation.time(),
                403,
                "'BY' IS AN INVALID TARGET LANGUAGE . EXAMPLE: LANGPAIR=EN|IT USING 2 LETTER ISO OR RFC3066 LIKE ZH-CN. ALMOST ALL LANGUAGES SUPPORTED BUT SOME MAY HAVE NO CONTENT");

        Assertions.assertEquals(dbTranslation, correctTranslation);
    }

    @Test
    public void testWhereTextIsLargerThan1000() throws Exception {
        UserRequest request = new UserRequest("a".repeat(1001), "ru", "en");
        ErrorResponse errorResponse = new ErrorResponse("Insertion of query in database goes wrong!", 500);
        compareResponses(request, mapper.writeValueAsString(errorResponse), 500);
    }

    private void compareResponses(UserRequest request, String response, int status) throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/ektogod/translateText")
                        .contentType("application/json")
                        .characterEncoding("UTF-8")
                        .content(mapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().is(status))
                .andExpect(MockMvcResultMatchers.content().string(response));
    }
}
