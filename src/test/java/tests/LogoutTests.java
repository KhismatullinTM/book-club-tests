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

@DisplayName("Тесты выхода из аккаунта")
public class LogoutTests extends TestBase {

    @Test
    @DisplayName("Успешный выход из системы")
    public void successfulLogoutTest() {
        LogoutBodyModel logoutData = step(
                "Подготовка данных для выхода из системы",
                () -> {
                    LoginBodyModel loginData =
                            new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

                    String refreshToken =
                            api.auth.loginAndGetRefreshToken(loginData);

                    return new LogoutBodyModel(refreshToken);
                }
        );

        api.auth.logout(logoutData);
    }

    @Test
    @DisplayName("Повторный выход из системы")
    public void repeatedLogoutTest() {
        LogoutBodyModel logoutData = step(
                "Подготовка данных для выхода из системы",
                () -> {
                    LoginBodyModel loginData =
                            new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

                    String refreshToken =
                            api.auth.loginAndGetRefreshToken(loginData);

                    return new LogoutBodyModel(refreshToken);
                }
        );

        api.auth.logout(logoutData);

        NotValidTokenResponseModel response =
                api.auth.logoutWithInvalidToken(logoutData);

        step("Проверка ответа при повторном выходе", () -> {
            assertThat(response.detail()).isNotBlank();
            assertThat(response.code()).isNotBlank();
        });
    }

    @Test
    @DisplayName("Выход из системы с пустым refresh-токеном")
    public void logoutWithEmptyRefreshTokenTest() {
        LogoutBodyModel logoutData = step(
                "Подготовка данных для выхода из системы",
                () -> new LogoutBodyModel("")
        );

        EmptyRefreshTokenModel emptyTokenResponse =
                api.auth.logoutWithEmptyRefreshToken(logoutData);

        step("Проверка ответа при пустом refresh-токене", () -> {
            String actualEmptyRefreshTokenError =
                    emptyTokenResponse.refresh().get(0);

            assertThat(actualEmptyRefreshTokenError)
                    .isEqualTo(EXPECTED_EMPTY_CREDENTIAL_ERROR);
        });
    }
}