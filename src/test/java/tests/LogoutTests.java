package tests;

import models.login.LoginBodyModel;
import models.logout.EmptyRefreshTokenModel;
import models.logout.LogoutBodyModel;
import models.logout.NotValidTokenResponseModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class LogoutTests extends TestBase {

    @Test
    public void successfulLogoutTest() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        String refreshToken = api.auth.loginAndGetRefreshToken(loginData);

        LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
        api.auth.logout(logoutData);
    }

    @Test

    public void repeatedLogoutTest() {

        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        String refreshToken = api.auth.loginAndGetRefreshToken(loginData);

        LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
        api.auth.logout(logoutData);

        NotValidTokenResponseModel response =
                api.auth.logoutWithInvalidToken(logoutData);

        assertThat(response.detail()).isNotBlank();
        assertThat(response.code()).isNotBlank();

    }

    @Test

    public void logoutWithEmptyRefreshTokenTest() {

        LogoutBodyModel logoutData = new LogoutBodyModel("");

        EmptyRefreshTokenModel emptyTokenResponse =
                api.auth.logoutWithEmptyRefreshToken(logoutData);

        String expectedEmptyRefreshTokenError = EXPECTED_EMPTY_CREDENTIAL_ERROR;
        String actualEmptyRefreshTokenError = emptyTokenResponse.refresh().get(0);
        assertThat(actualEmptyRefreshTokenError).isEqualTo(expectedEmptyRefreshTokenError);

    }
    // todo add more negative tests
}

