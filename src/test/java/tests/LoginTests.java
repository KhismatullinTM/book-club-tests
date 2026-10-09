package tests;

import models.login.*;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class LoginTests extends TestBase {

    @Test
    public void successfulLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);

        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        String actualAccess = loginResponse.access();
        String actualRefresh = loginResponse.refresh();
        assertThat(actualAccess).startsWith(LOGIN_TOKEN_PREFIX);
        assertThat(actualRefresh).startsWith(LOGIN_TOKEN_PREFIX);
        assertThat(actualAccess).isNotEqualTo(actualRefresh);
    }

    @Test
    public void wrongCredentialsLoginTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD);

        WrongCredentialsLoginResponseModel loginResponse = api.auth.loginWrongCredentials(loginData);

        String expectedDetailError = LOGIN_WRONG_CREDENTIALS_ERROR;
        String actualDetailError = loginResponse.detail();
        assertThat(actualDetailError).isEqualTo(expectedDetailError);
    }

    @Test
    public void emptyUsernameLoginTest() {
        LoginBodyModel loginData =
                new LoginBodyModel(LOGIN_EMPTY_USERNAME, LOGIN_PASSWORD);

        EmptyLoginCredentialLoginResponseModel loginResponse =
                api.auth.loginEmptyCredentials(loginData);

        assertThat(loginResponse.username())
                .isNotEmpty();

        String expectedEmptyLoginError = EXPECTED_EMPTY_CREDENTIAL_ERROR;
        String actualEmptyLoginError = loginResponse.username().get(0);
        assertThat(actualEmptyLoginError).isEqualTo(expectedEmptyLoginError);
    }

    @Test
    public void nonexistentUserLoginTest() {
        LoginBodyModel loginData =
                new LoginBodyModel(LOGIN_NONEXISTENT_USERNAME, LOGIN_PASSWORD);

        WrongCredentialsLoginResponseModel loginResponse =
                api.auth.loginWrongCredentials(loginData);

        assertThat(loginResponse.detail())
                .isEqualTo(LOGIN_WRONG_CREDENTIALS_ERROR);
    }

    @Test
    public void emptyPasswordLoginTest() {
        LoginBodyModel loginData =
                new LoginBodyModel(LOGIN_USERNAME, LOGIN_EMPTY_PASSWORD);

        EmptyPasswordCredentialLoginResponseModel loginResponse =
                api.auth.passwordEmptyCredentials(loginData);

        assertThat(loginResponse.password())
                .isNotEmpty();

        String expectedEmptyPasswordError = EXPECTED_EMPTY_CREDENTIAL_ERROR;
        String actualEmptyLoginError = loginResponse.password().get(0);
        assertThat(actualEmptyLoginError).isEqualTo(expectedEmptyPasswordError);
    }

    // todo add more negative tests

}
