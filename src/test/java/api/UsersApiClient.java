package api;

import models.registration.*;
import models.update_user.UpdateUserPatchBodyModel;
import models.update_user.UpdateUserPatchResponseModel;
import models.update_user.UpdateUserPutBodyModel;
import models.update_user.UpdateUserPutResponseModel;

import static io.restassured.RestAssured.given;
import static specs.UpdateUser.UpdateUserSpec.successfulUpdateUserResponseSpec;
import static specs.UpdateUser.UpdateUserSpec.updateUserRequestSpec;
import static specs.registration.RegistrationSpec.*;

public class UsersApiClient {

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

    public UpdateUserPutResponseModel putUpdateUser(UpdateUserPutBodyModel body, String accessToken) {
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

    public UpdateUserPatchResponseModel patchUpdateUser(UpdateUserPatchBodyModel body, String accessToken) {
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
