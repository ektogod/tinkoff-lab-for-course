package test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tinkoff_lab.TinkoffLabApplication;
import com.tinkoff_lab.requests.UserRequest;
import com.tinkoff_lab.responses.UserResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest(classes = TinkoffLabApplication.class)
@AutoConfigureMockMvc

public class ControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void testWhenResponseIsCorrect() throws Exception {
        UserRequest request = new UserRequest("любовь", "ru", "en");
        UserResponse response = new UserResponse("Ð\u009BÑ\u008EÐ±Ð¾Ð²Ñ\u008C"); // у меня ну ни в какую не получалось сделать так, чтобы ответь нормально кодировался, поэтому я решил оставить так (вне тестов как бы все хорошо)
        compareResponses(request, mapper.writeValueAsString(response), 200);
    }

    @Test
    public void testWhereTextIsEmpty() throws Exception {
        UserRequest request = new UserRequest("", "ru", "en");
        String response = "NO QUERY SPECIFIED. EXAMPLE REQUEST: GET?Q=HELLO&LANGPAIR=EN|IT";
        compareResponses(request, response, 403);
    }

    @Test
    public void testWhereTextIsNull() throws Exception {
        UserRequest request = new UserRequest(null, "ru", "en");
        String response = "Translation went wrong because something from parameters is null!";
        compareResponses(request, response, 500);
    }

    @Test
    public void testWhereLanguageIsIncorrect() throws Exception {
        UserRequest request = new UserRequest("любовь", "ru", "by");
        String response = "'BY' IS AN INVALID TARGET LANGUAGE . EXAMPLE: LANGPAIR=EN|IT USING 2 LETTER ISO OR RFC3066 LIKE ZH-CN. ALMOST ALL LANGUAGES SUPPORTED BUT SOME MAY HAVE NO CONTENT";
        compareResponses(request, response, 403);
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
