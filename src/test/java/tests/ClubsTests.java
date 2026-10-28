package tests;

import models.clubs.*;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import tests.testdata.ClubTestData;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.testdata.TestData.*;

@DisplayName("Тесты клубов")
public class ClubsTests extends TestBase {

    private final ClubTestData clubTestData = new ClubTestData();

    @Test
    @Disabled
    @DisplayName("Получение списка клубов с валидной структурой")
    public void getClubsReturns200AndValidStructure() {
        ClubsListResponseModel response = api.clubs.getClubs();

        step("Проверка структуры ответа", () -> {
            assertThat(response).isNotNull();
            assertThat(response.count()).isGreaterThanOrEqualTo(0);
            assertThat(response.results()).isNotNull();
            assertThat(response.results()).hasSize(response.count());
        });
    }

    @Test
    @Disabled
    @DisplayName("Количество клубов соответствует размеру списка")
    public void getClubsCountMatchesResultsSize() {
        ClubsListResponseModel response = api.clubs.getClubs();

        step("Проверка соответствия количества клубов размеру списка", () ->
                assertThat(response.results()).hasSize(response.count())
        );
    }

    @Test
    @DisplayName("Все клубы содержат обязательные поля")
    public void getClubsEachClubHasRequiredFields() {
        ClubsListResponseModel response = api.clubs.getClubs();

        step("Проверка обязательных полей каждого клуба", () -> {
            for (ClubResponseModel club : response.results()) {
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
        });
    }

    @Test
    @DisplayName("Получение списка клубов с данными пагинации")
    public void getClubsPaginationFieldsPresent() {
        ClubsListResponseModel response = api.clubs.getClubs();

        step("Проверка наличия данных для пагинации", () -> {
            assertThat(response.count()).isNotNull();
            assertThat(response.results()).isNotNull();
        });
    }

    @Test
    @DisplayName("Поиск клуба по ID")
    public void getClubByIdTest() {
        ClubResponseModel response = api.clubs.getClubsById(CLUBS_ID);

        step("Проверка идентификатора клуба", () ->
                assertThat(response.id()).isEqualTo(CLUBS_ID)
        );
    }

    @Test
    @DisplayName("Успешное создание клуба")
    public void successfulCreateClub() {
        String accessToken = getAccessToken();

        ClubRequestModel requestModel = clubTestData.newClubBody();

        ClubResponseModel createdClub = api.clubs.postCreateClub(requestModel, accessToken);

        step("Проверка данных созданного клуба", () -> {
            assertThat(createdClub.bookTitle()).isEqualTo(requestModel.bookTitle());
            assertThat(createdClub.bookAuthors()).isEqualTo(requestModel.bookAuthors());
            assertThat(createdClub.publicationYear()).isEqualTo(requestModel.publicationYear());
            assertThat(createdClub.description()).isEqualTo(requestModel.description());
            assertThat(createdClub.telegramChatLink()).isEqualTo(requestModel.telegramChatLink());
        });
    }

    @Test
    @DisplayName("Полное изменение данных клуба")
    public void successfulFullUpdateClubsById() {
        String accessToken = getAccessToken();

        ClubRequestModel requestModel = clubTestData.newClubBody();

        ClubResponseModel createdClub = api.clubs.postCreateClub(requestModel, accessToken);

        ClubResponseModel response =
                api.clubs.fullUpdateClubsById(
                        createdClub.id(),
                        requestModel,
                        accessToken
                );

        step("Проверка обновленных данных клуба", () -> {
            assertThat(response.id()).isEqualTo(createdClub.id());
            assertThat(response.bookTitle()).isEqualTo(requestModel.bookTitle());
            assertThat(response.bookAuthors()).isEqualTo(requestModel.bookAuthors());
            assertThat(response.publicationYear()).isEqualTo(requestModel.publicationYear());
            assertThat(response.description()).isEqualTo(requestModel.description());
            assertThat(response.telegramChatLink()).isEqualTo(requestModel.telegramChatLink());
        });
    }

    @Test
    @DisplayName("Частичное изменение данных клуба")
    public void successfulPatchClubsById() {
        String accessToken = getAccessToken();

        ClubPatchRequestModel updateClubBody =
                clubTestData.patchUpdateClubBody();

        ClubResponseModel responseModel =
                api.clubs.patchUpdateClubsById(
                        CLUBS_ID,
                        updateClubBody,
                        accessToken
                );

        step("Проверка результатов частичного изменения клуба", () -> {
            assertThat(responseModel.id()).isEqualTo(CLUBS_ID);
            assertThat(responseModel.description()).isEqualTo(updateClubBody.description());
        });
    }

    @Test
    @DisplayName("Удаление клуба")
    public void successfulDeleteClubsById() {
        String accessToken = getAccessToken();

        ClubRequestModel requestBody = clubTestData.newClubBody();

        ClubResponseModel createdClub =
                api.clubs.postCreateClub(requestBody, accessToken);

        int clubId = createdClub.id();

        api.clubs.deleteClubsById(clubId, accessToken);
    }

    @Test
    @DisplayName("Запрет полного изменения клуба без необходимых прав")
    public void forbiddenFullUpdateClubsById() {
        String accessToken = getAccessToken();

        ClubRequestModel requestModel = clubTestData.newClubBody();

        ClubForbiddenResponseModel response =
                api.clubs.forbiddenUpdateClubsById(
                        CLUB_ID_FORBIDDEN_TO_EDIT,
                        requestModel,
                        accessToken
                );

        step("Проверка отказа в изменении клуба", () ->
                assertThat(response.detail()).isEqualTo(FORBIDDEN_ERROR_TEXT)
        );
    }
}