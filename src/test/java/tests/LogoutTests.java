package tests;

import models.login.LoginBodyModel;
import models.logout.EmptyRefreshTokenModel;
import models.logout.LogoutBodyModel;
import models.logout.NotValidTokenResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.testdata.TestData.*;

@DisplayName("Тесты ")
public class LogoutTests extends TestBase {

    @Test
    @DisplayName("Успешный выход из системы")
    public void successfulLogoutTest() {

        LogoutBodyModel logoutData  = step("Подготовка данных для выхода из системы", ()-> {
            LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
            String refreshToken =  api.auth.loginAndGetRefreshToken(loginData);
            return new LogoutBodyModel(refreshToken);
        });

        step("Выход из системы", ()-> api.auth.logout(logoutData));
    }

    @Test
    @DisplayName("Повторный выход из системы")
    public void repeatedLogoutTest() {
        LogoutBodyModel logoutData = step("Подготовка данных для выхода из системы", () -> {
            LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
            String refreshToken = api.auth.loginAndGetRefreshToken(loginData);
            return new LogoutBodyModel(refreshToken);
        });

        step("Первый выход из системы", () -> api.auth.logout(logoutData));

        NotValidTokenResponseModel response = step("Повторная попытка выхода из системы", () ->
                api.auth.logoutWithInvalidToken(logoutData)
        );

        step("Проверка ответа при повторном выходе", () -> {
            assertThat(response.detail()).isNotBlank();
            assertThat(response.code()).isNotBlank();
        });

    }

    @Test
    @DisplayName("Выход из системы с пустым refresh-токеном")
    public void logoutWithEmptyRefreshTokenTest() {
        LogoutBodyModel logoutData = step("Подготовка данных для выхода из системы", () ->
                new LogoutBodyModel("")
        );

        EmptyRefreshTokenModel emptyTokenResponse = step("Попытка выхода с пустым refresh-токеном", () ->
                api.auth.logoutWithEmptyRefreshToken(logoutData)
        );

        step("Проверка ответа при пустом refresh-токене", () -> {
            String expectedEmptyRefreshTokenError = EXPECTED_EMPTY_CREDENTIAL_ERROR;
            String actualEmptyRefreshTokenError = emptyTokenResponse.refresh().get(0);
            assertThat(actualEmptyRefreshTokenError)
                    .isEqualTo(expectedEmptyRefreshTokenError);
        });
    }
}