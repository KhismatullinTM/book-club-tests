package api;

import io.qameta.allure.Step;
import models.registration.*;
import models.user.UpdateUserPatchBodyModel;
import models.user.UpdateUserPatchResponseModel;
import models.user.UpdateUserPutBodyModel;
import models.user.UpdateUserPutResponseModel;

import static io.restassured.RestAssured.given;
import static specs.UpdateUser.UpdateUserSpec.successfulUpdateUserResponseSpec;
import static specs.UpdateUser.UpdateUserSpec.updateUserRequestSpec;
import static specs.registration.RegistrationSpec.*;

public class UsersApiClient {

    @Step("Регистрация нового пользователя")
    public SuccessfulRegistrationResponseModel register(RegistrationBodyModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);
    }

    @Step("Попытка регистрации уже существующего пользователя")
    public ExistingUserResponseModel registerExistingUser(RegistrationBodyModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingUserRegistrationResponseSpec)
                .extract()
                .as(ExistingUserResponseModel.class);
    }

    @Step("Регистрация пользователя с пустым именем")
    public EmptyUsernameRegistrationResponseModel registerWithEmptyUsername(
            RegistrationBodyModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(emptyUsernameRegistrationResponseSpec)
                .extract()
                .as(EmptyUsernameRegistrationResponseModel.class);
    }

    @Step("Регистрация пользователя с пустым паролем")
    public EmptyPasswordRegistrationResponseModel registerWithEmptyPassword(
            RegistrationBodyModel body) {
        return given(registrationRequestSpec)
                .body(body)
                .when()
                .post("/users/register/")
                .then()
                .spec(emptyPasswordRegistrationResponseSpec)
                .extract()
                .as(EmptyPasswordRegistrationResponseModel.class);
    }

    @Step("Полное обновление данных пользователя PUT /users/me/")
    public UpdateUserPutResponseModel putUpdateUser(
            UpdateUserPutBodyModel body, String accessToken) {
        return given(updateUserRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract()
                .as(UpdateUserPutResponseModel.class);
    }

    @Step("Частичное обновление данных пользователя PATCH /users/me/")
    public UpdateUserPatchResponseModel patchUpdateUser(
            UpdateUserPatchBodyModel body, String accessToken) {
        return given(updateUserRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .patch("/users/me/")
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract()
                .as(UpdateUserPatchResponseModel.class);
    }
}