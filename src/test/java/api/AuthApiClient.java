package api;

import io.qameta.allure.Step;
import models.login.*;
import models.logout.EmptyRefreshTokenModel;
import models.logout.LogoutBodyModel;
import models.logout.NotValidTokenResponseModel;

import static io.restassured.RestAssured.given;
import static specs.login.LoginSpec.*;
import static specs.logout.LogoutSpec.*;

public class AuthApiClient {

    @Step("Авторизация пользователя")
    public SuccessfulLoginResponseModel login(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .as(SuccessfulLoginResponseModel.class);
    }

    @Step("Авторизация и получение refresh-токена")
    public String loginAndGetRefreshToken(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .path("refresh");
    }

    @Step("Авторизация и получение access-токена")
    public String loginAndGetAccessToken(LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract()
                .path("access");
    }

    @Step("Попытка авторизации с неверными учётными данными")
    public WrongCredentialsLoginResponseModel loginWrongCredentials(
            LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(wrongCredentialsLoginResponseSpec)
                .extract()
                .as(WrongCredentialsLoginResponseModel.class);
    }

    @Step("Выход из системы")
    public void logout(LogoutBodyModel logoutBody) {
        given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    @Step("Попытка авторизации с пустыми учётными данными")
    public EmptyLoginCredentialLoginResponseModel loginEmptyCredentials(
            LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyLoginCredentialsLoginResponseSpec)
                .extract()
                .as(EmptyLoginCredentialLoginResponseModel.class);
    }

    @Step("Попытка авторизации с пустым паролем")
    public EmptyPasswordCredentialLoginResponseModel passwordEmptyCredentials(
            LoginBodyModel loginBody) {
        return given(loginRequestSpec)
                .body(loginBody)
                .when()
                .post("/auth/token/")
                .then()
                .spec(emptyPasswordCredentialsLoginResponseSpec)
                .extract()
                .as(EmptyPasswordCredentialLoginResponseModel.class);
    }

    @Step("Попытка выхода из системы с невалидным токеном")
    public NotValidTokenResponseModel logoutWithInvalidToken(
            LogoutBodyModel logoutBody) {
        return given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(notValidTokenResponseSpec)
                .extract()
                .as(NotValidTokenResponseModel.class);
    }

    @Step("Попытка выхода из системы с пустым refresh-токеном")
    public EmptyRefreshTokenModel logoutWithEmptyRefreshToken(
            LogoutBodyModel logoutBody) {
        return given(logoutRequestSpec)
                .body(logoutBody)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(emptyRefreshTokenResponseSpec)
                .extract()
                .as(EmptyRefreshTokenModel.class);
    }
}