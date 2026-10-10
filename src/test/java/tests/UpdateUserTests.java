package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.update_user.UpdateUserPatchBodyModel;
import models.update_user.UpdateUserPatchResponseModel;
import models.update_user.UpdateUserPutBodyModel;
import models.update_user.UpdateUserPutResponseModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class UpdateUserTests extends TestBase {

    @Test
    public void successfulPathUpdateUserTest(){

        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);
        String accessToken = loginResponse.access();

        UpdateUserPutBodyModel updateUserBodyModel = new UpdateUserPutBodyModel(
                UPDATE_USERNAME,
                UPDATE_FIRST_NAME,
                UPDATE_LAST_NAME,
                UPDATE_EMAIL);

        UpdateUserPutResponseModel updateUserResponse = api.users.putUpdateUser(updateUserBodyModel, accessToken);

        assertThat(updateUserResponse.id()).isGreaterThan(0);
        assertThat(updateUserResponse.username()).isEqualTo(UPDATE_USERNAME);
        assertThat(updateUserResponse.firstName()).isEqualTo(UPDATE_FIRST_NAME);
        assertThat(updateUserResponse.lastName()).isEqualTo(UPDATE_LAST_NAME);
        assertThat(updateUserResponse.email()).isEqualTo(UPDATE_EMAIL);
        assertThat(updateUserResponse.remoteAddr()).isNotBlank();
    }

    @Test

    public void successfulPatchUpdateUserTest() {

        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);
        String accessToken = loginResponse.access();

        UpdateUserPatchBodyModel updateUserBodyModel =
                new UpdateUserPatchBodyModel(UPDATE_FIRST_NAME);

        UpdateUserPatchResponseModel updateUserResponse =
                api.users.patchUpdateUser(updateUserBodyModel, accessToken);

        assertThat(updateUserResponse.firstName()).isEqualTo(UPDATE_FIRST_NAME);
    }
}
