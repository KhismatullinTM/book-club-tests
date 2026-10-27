package tests.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.browser.BrowserAuthModel;

import static com.codeborne.selenide.Selenide.executeJavaScript;

public class BrowserAuthHelper {
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public void setBrowserAuth(BrowserAuthModel authModel) {
        try {
            String authJson = OBJECT_MAPPER.writeValueAsString(authModel);
            executeJavaScript(
                    "localStorage.setItem('book_club_auth', arguments[0]);",
                    authJson
            );
        } catch (JsonProcessingException e) {
            throw new IllegalStateException(
                    "Не удалось сформировать JSON авторизации", e
            );
        }
    }
}
