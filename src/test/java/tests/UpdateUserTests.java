package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.update_user.UpdateUserPatchBodyModel;
import models.update_user.UpdateUserPatchResponseModel;
import models.update_user.UpdateUserPutBodyModel;
import models.update_user.UpdateUserPutResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class UpdateUserTests extends TestBase {

    private String username;
    private String password;
    private String accessToken;

    @BeforeEach
    public void prepareTestData() {

        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
        RegistrationBodyModel registrationData =
                new RegistrationBodyModel(username, password);
        api.users.register(registrationData);

        LoginBodyModel loginData = new LoginBodyModel(username, password);
        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        accessToken = loginResponse.access();
    }


    @Test
    public void successfulPutUpdateUserTest() {
        String updatedUsername = "updatedUser_" + System.currentTimeMillis();

        UpdateUserPutBodyModel updateUserBodyModel = new UpdateUserPutBodyModel(
                updatedUsername,
                UPDATE_FIRST_NAME,
                UPDATE_LAST_NAME,
                UPDATE_EMAIL
        );

        UpdateUserPutResponseModel updateUserResponse =
                api.users.putUpdateUser(updateUserBodyModel, accessToken);

        assertThat(updateUserResponse.id()).isGreaterThan(0);
        assertThat(updateUserResponse.username()).isEqualTo(updatedUsername);
        assertThat(updateUserResponse.firstName()).isEqualTo(UPDATE_FIRST_NAME);
        assertThat(updateUserResponse.lastName()).isEqualTo(UPDATE_LAST_NAME);
        assertThat(updateUserResponse.email()).isEqualTo(UPDATE_EMAIL);
        assertThat(updateUserResponse.remoteAddr()).isNotBlank();
    }

    @Test
    public void successfulPatchUpdateUserTest() {
        UpdateUserPatchBodyModel updateUserBodyModel =
                new UpdateUserPatchBodyModel(UPDATE_FIRST_NAME);

        UpdateUserPatchResponseModel updateUserResponse =
                api.users.patchUpdateUser(updateUserBodyModel, accessToken);

        assertThat(updateUserResponse.firstName()).isEqualTo(UPDATE_FIRST_NAME);
    }
}
