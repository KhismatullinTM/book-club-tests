package api;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import models.clubs.*;

import static io.restassured.RestAssured.given;
import static specs.clubs.ClubsSpec.*;

public class ClubsApiClient {

    @Step("Получение списка клубов GET /clubs/")
    public ClubsListResponseModel getClubs() {
        return given(clubsRequestSpec)
                .when()
                .get("/clubs/")
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    @Step("Получение списка клубов GET /clubs/")
    public ClubModel postCreateClub(ClubRequestModel body, String accessToken) {
        return given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .body(body)
                .post("/clubs/")
                .then()
                .spec(successfulCreateClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Получение данных клуба GET /clubs/{id}")
    public ClubModel getClubsById(int id) {
        return given(clubsRequestSpec)
                .pathParam("id", id)
                .when()
                .get("/clubs/{id}/")
                .then()
                .spec(successfulClubsByIdResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Полное изменение данных клуба PUT /clubs/{id}")
    public ClubModel fullUpdateClubsById(int id, ClubRequestModel body, String accessToken) {
        return given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .body(body)
                .when()
                .put("/clubs/{id}/")
                .then()
                .spec(successfulUpdateClubByIdResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Частичное изменение данных клуба PATCH /clubs/{id}")
    public ClubModel patchUpdateClubsById(int id, ClubPatchRequestModel body, String accessToken) {
        return given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .body(body)
                .when()
                .patch("/clubs/{id}/")
                .then()
                .spec(successfulUpdateClubByIdResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("Удаление клуба DELETE /clubs/{id}")
    public Response deleteClubsById(int id, String accessToken) {
         return given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .when()
                .delete("/clubs/{id}/");
    }

    @Step("Полное изменение данных клуба без прав PUT /clubs/{id}")
    public ClubForbiddenResponseModel forbiddenUpdateClubsById(int id, ClubRequestModel body, String accessToken) {
        return given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .body(body)
                .when()
                .patch("/clubs/{id}/")
                .then()
                .spec(forbiddenErrorResponseSpec)
                .extract()
                .as(ClubForbiddenResponseModel.class);
    }

}
