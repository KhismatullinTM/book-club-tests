package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.user.UpdateUserPatchBodyModel;
import models.user.UpdateUserPatchResponseModel;
import models.user.UpdateUserPutBodyModel;
import models.user.UpdateUserPutResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.testdata.TestData.*;

public class UpdateUserTests extends TestBase {

    private String username;
    private String password;
    private String accessToken;

    @BeforeEach
    public void prepareTestData() {

        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();

        RegistrationBodyModel registrationData = step("Подготовка данных для регистрации", () ->
                new RegistrationBodyModel(username, password)
        );

        step("Регистрация пользователя", () -> api.users.register(registrationData));

        LoginBodyModel loginData = step("Подготовка данных для авторизации", () ->
                new LoginBodyModel(username, password)
        );

        SuccessfulLoginResponseModel loginResponse = step("Авторизация пользователя", () ->
                api.auth.login(loginData)
        );

        accessToken = loginResponse.access();
    }

    @Test
    @DisplayName("Успешное полное изменение данных пользователя")
    public void successfulPutUpdateUserTest() {
        String updatedUsername = "updatedUser_" + System.currentTimeMillis();

        UpdateUserPutBodyModel updateUserBodyModel = step("Подготовка данных для обновления пользователя", () ->
                new UpdateUserPutBodyModel(
                        updatedUsername,
                        UPDATE_FIRST_NAME,
                        UPDATE_LAST_NAME,
                        UPDATE_EMAIL
                )
        );

        UpdateUserPutResponseModel updateUserResponse = step("Полное обновление данных пользователя (PUT)", () ->
                api.users.putUpdateUser(updateUserBodyModel, accessToken)
        );

        step("Проверка обновлённых данных пользователя", () -> {
            assertThat(updateUserResponse.id()).isGreaterThan(0);
            assertThat(updateUserResponse.username()).isEqualTo(updatedUsername);
            assertThat(updateUserResponse.firstName()).isEqualTo(UPDATE_FIRST_NAME);
            assertThat(updateUserResponse.lastName()).isEqualTo(UPDATE_LAST_NAME);
            assertThat(updateUserResponse.email()).isEqualTo(UPDATE_EMAIL);
            assertThat(updateUserResponse.remoteAddr()).isNotBlank();
        });
    }

    @Test
    @DisplayName("Успешное изменение имени пользователя")
    public void successfulPatchUpdateUserTest() {
        UpdateUserPatchBodyModel updateUserBodyModel = step("Подготовка данных для частичного обновления пользователя", () ->
                new UpdateUserPatchBodyModel(UPDATE_FIRST_NAME)
        );

        UpdateUserPatchResponseModel updateUserResponse = step("Частичное обновление данных пользователя (PATCH)", () ->
                api.users.patchUpdateUser(updateUserBodyModel, accessToken)
        );

        step("Проверка обновлённого имени пользователя", () -> {
            assertThat(updateUserResponse.firstName()).isEqualTo(UPDATE_FIRST_NAME);
        });
    }
}
