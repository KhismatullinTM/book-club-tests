package tests;

import io.restassured.response.Response;
import models.clubs.ClubModel;
import models.clubs.ClubRequestModel;
import models.clubs.ClubsListResponseModel;
import models.login.LoginBodyModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class ClubsTests extends TestBase {

    TestData clubTestData = new TestData();

    @Test
    public void getClubsReturns200AndValidStructure() {
        ClubsListResponseModel response = api.clubs.getClubs();

        assertThat(response).isNotNull();
        assertThat(response.count()).isGreaterThanOrEqualTo(0);
        assertThat(response.results()).isNotNull();
        assertThat(response.results()).hasSize(response.count());
    }

    @Test
    public void getClubsCountMatchesResultsSize() {
        ClubsListResponseModel response = api.clubs.getClubs();

        assertThat(response.results())
                .as("count должно совпадать с размером results")
                .hasSize(response.count());
    }

    @Test
    public void getClubsEachClubHasRequiredFields() {
        ClubsListResponseModel response = api.clubs.getClubs();

        for (ClubModel club : response.results()) {
            assertThat(club.id()).isNotNull().isPositive();
            assertThat(club.bookTitle()).isNotNull();
            assertThat(club.bookAuthors()).isNotNull();
            assertThat(club.publicationYear()).isNotNull();
            assertThat(club.description()).isNotNull();
            assertThat(club.telegramChatLink()).isNotNull();
            assertThat(club.owner()).isNotNull().isPositive();
            assertThat(club.members()).isNotNull();
            assertThat(club.reviews()).isNotNull();
            assertThat(club.created()).isNotNull();
        }
    }

    @Test
    public void getClubsPaginationFieldsPresent() {
        ClubsListResponseModel response = api.clubs.getClubs();

        assertThat(response.count()).isNotNull();
        // next и previous могут быть null при одной странице
        assertThat(response.results()).isNotNull();
    }

    @Test
    @DisplayName("Поиск клуба по ID")
    public void getClubByIdTest() {
        ClubModel response = api.clubs.getClubsById(CLUBS_ID);

        assertThat(response.id()).isEqualTo(CLUBS_ID);
    }

    @Test
    @DisplayName("Успешное создание клуба")
    public void successfulCreateClub() {

        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        String accessToken =  api.auth.loginAndGetAccessToken(loginData);

        ClubRequestModel requestModel = clubTestData.newClubBody();

        ClubModel response = api.clubs.postCreateClub(requestModel, accessToken);
    }

    @Test
    @DisplayName("Полное изменение данных клуба")
    public void successfulFullUpdateClubsById() {

        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        String accessToken =  api.auth.loginAndGetAccessToken(loginData);

        ClubRequestModel requestModel = clubTestData.newClubBody();

        ClubModel response = api.clubs.fullUpdateClubsById(CLUBS_ID, requestModel, accessToken);

    }

    @Test
    @DisplayName("Удаление клуба")
    public void successfulDeleteClubsById() {

        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        String accessToken = api.auth.loginAndGetAccessToken(loginData);
        ClubRequestModel requestBody = clubTestData.newClubBody();
        ClubModel createdClub = api.clubs.postCreateClub(requestBody, accessToken);
        int clubId = createdClub.id();

        Response response = api.clubs.deleteClubsById(clubId, accessToken);

        assertThat(response.statusCode()).isEqualTo(204);
    }

}
