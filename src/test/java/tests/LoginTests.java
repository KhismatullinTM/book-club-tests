package tests;

import models.login.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.testdata.TestData.*;

@DisplayName("Тесты авторизации")
public class LoginTests extends TestBase {

    @Test
    @DisplayName("Успешная авторизация")
    public void successfulLoginTest() {

        LoginBodyModel loginData = step("Подготовка данных для авторизации", () ->
                new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD)
        );

        SuccessfulLoginResponseModel loginResponse = step("Успешная авторизация", () ->
                api.auth.login(loginData)
        );

        step("Проверка ответа авторизации", () -> {
            String actualAccess = loginResponse.access();
            String actualRefresh = loginResponse.refresh();
            assertThat(actualAccess).startsWith(LOGIN_TOKEN_PREFIX);
            assertThat(actualRefresh).startsWith(LOGIN_TOKEN_PREFIX);
            assertThat(actualAccess).isNotEqualTo(actualRefresh);
        });
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = step("Подготовка данных для авторизации", () ->
                new LoginBodyModel(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD)
        );

        WrongCredentialsLoginResponseModel loginResponse = step("Попытка авторизации с неверным паролем", () ->
                api.auth.loginWrongCredentials(loginData)
        );

        step("Проверка ответа авторизации", () -> {
            String expectedDetailError = LOGIN_WRONG_CREDENTIALS_ERROR;
            String actualDetailError = loginResponse.detail();
            assertThat(actualDetailError).isEqualTo(expectedDetailError);
        });
    }

    @Test
    @DisplayName("Авторизация с пустым логином")
    public void emptyUsernameLoginTest() {
        LoginBodyModel loginData = step("Подготовка данных для авторизации", () ->
                new LoginBodyModel(LOGIN_EMPTY_USERNAME, LOGIN_PASSWORD)
        );

        EmptyLoginCredentialLoginResponseModel loginResponse = step("Попытка авторизации с пустым логином", () ->
                api.auth.loginEmptyCredentials(loginData)
        );

        step("Проверка ответа авторизации", () -> {
            assertThat(loginResponse.username()).isNotEmpty();
            String expectedEmptyLoginError = EXPECTED_EMPTY_CREDENTIAL_ERROR;
            String actualEmptyLoginError = loginResponse.username().get(0);
            assertThat(actualEmptyLoginError).isEqualTo(expectedEmptyLoginError);
        });
    }

    @Test
    @DisplayName("Авторизация с несуществующим пользователем")
    public void nonexistentUserLoginTest() {
        LoginBodyModel loginData = step("Подготовка данных для авторизации", () ->
                new LoginBodyModel(LOGIN_NONEXISTENT_USERNAME, LOGIN_PASSWORD)
        );

        WrongCredentialsLoginResponseModel loginResponse = step("Попытка авторизации с несуществующим логином", () ->
                api.auth.loginWrongCredentials(loginData)
        );

        step("Проверка ответа авторизации", () -> {
            assertThat(loginResponse.detail())
                    .isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR);
        });
    }

    @Test
    @DisplayName("Авторизация с пустым паролем")
    public void emptyPasswordLoginTest() {
        LoginBodyModel loginData = step("Подготовка данных для авторизации", () ->
                new LoginBodyModel(LOGIN_USERNAME, LOGIN_EMPTY_PASSWORD)
        );

        EmptyPasswordCredentialLoginResponseModel loginResponse = step("Попытка авторизации с пустым паролем", () ->
                api.auth.passwordEmptyCredentials(loginData)
        );

        step("Проверка ответа авторизации", () -> {
            assertThat(loginResponse.password()).isNotEmpty();
            String expectedEmptyPasswordError = EXPECTED_EMPTY_CREDENTIAL_ERROR;
            String actualEmptyPasswordError = loginResponse.password().get(0);
            assertThat(actualEmptyPasswordError).isEqualTo(expectedEmptyPasswordError);
        });
    }
}