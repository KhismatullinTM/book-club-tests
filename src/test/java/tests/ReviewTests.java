package tests;

import models.browser.BrowserAuthModel;
import models.clubs.ClubRequestModel;
import models.clubs.ClubResponseModel;
import models.login.SuccessfulLoginResponseModel;
import models.review.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.BookPage;
import tests.helpers.BrowserAuthHelper;
import tests.testdata.BrowserAuthData;
import tests.testdata.ClubTestData;
import tests.testdata.ReviewTestData;

import static com.codeborne.selenide.Selenide.open;
import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.testdata.PagesTestData.NO_REVIEW_MESSAGE;
import static tests.testdata.TestData.*;

public class ReviewTests extends TestBase {

    private final ReviewTestData reviewTestData = new ReviewTestData();
    private final ClubTestData clubTestData = new ClubTestData();
    private final BookPage bookPage = new BookPage();
    private final BrowserAuthHelper browserAuthHelper = new BrowserAuthHelper();

    @Test
    @DisplayName("Получение списка отзывов по параметрам запроса")
    void getAllReviewByQuery() {
        String accessToken = step(
                "Получение токена авторизации",
                this::getAccessToken
        );

        ReviewListResponseModel response = step(
                "Получение списка отзывов",
                () -> api.review.getAllReview(accessToken, 10, 1, 2)
        );

        step("Проверка списка отзывов", () -> {
            assertThat(response.count()).isPositive();
            assertThat(response.results()).hasSize(2);
            assertThat(response.results())
                    .allSatisfy(review ->
                            assertThat(review.club()).isEqualTo(10)
                    );
        });
    }

    @Test
    @DisplayName("500 — Ошибка сервера при создании отзыва")
    void given500() {
        String accessToken = step(
                "Получение токена авторизации",
                this::getAccessToken
        );

        CreateReviewRequestModel request = step(
                "Подготовка данных для создания отзыва",
                () -> reviewTestData.createNewReview(1)
        );

        step("Отправка запроса на создание отзыва с ошибкой сервера", () ->
                api.review.givenInternalServerErrorReview(accessToken, request)
        );
    }

    @Test
    @DisplayName("Успешное создание отзыва и проверка в UI")
    void createNewReviewTest() {
        String accessToken = step(
                "Получение токена авторизации",
                this::getAccessToken
        );

        ClubRequestModel clubRequest = step(
                "Подготовка данных нового клуба",
                clubTestData::newClubBody
        );

        ClubResponseModel createdClub = step(
                "Создание нового клуба через API",
                () -> api.clubs.postCreateClub(clubRequest, accessToken)
        );

        CreateReviewRequestModel reviewRequest = step(
                "Подготовка данных нового отзыва",
                () -> reviewTestData.createNewReview(createdClub.id())
        );

        ReviewResponseModel createdReview = step(
                "Создание отзыва через API",
                () -> api.review.createNewReview(accessToken, reviewRequest)
        );

        step("Проверка данных созданного отзыва", () -> {
            assertThat(createdReview.id()).isNotNull();
            assertThat(createdReview.club()).isEqualTo(reviewRequest.club());
            assertThat(createdReview.review()).isEqualTo(reviewRequest.review());
            assertThat(createdReview.assessment()).isEqualTo(reviewRequest.assessment());
            assertThat(createdReview.readPages()).isEqualTo(reviewRequest.readPages());
        });

        step("Проверка отображения отзыва в UI", () ->
                bookPage.openBookPageByClubId(createdClub.id())
                        .checkCountPageInReview(reviewRequest.readPages())
                        .checkReviewContent(reviewRequest.review())
        );
    }

    @Test
    @DisplayName("Успешное получение отзыва по ID")
    void getReviewByIdTest() {
        String accessToken = step(
                "Получение токена авторизации",
                this::getAccessToken
        );

        ReviewResponseModel response = step(
                "Получение отзыва по ID",
                () -> api.review.getReviewById(VALID_REVIEW_ID, accessToken)
        );

        step("Проверка полученного отзыва", () -> {
            assertThat(response.id()).isEqualTo(VALID_REVIEW_ID);
            assertThat(response.club()).isNotNull();
            assertThat(response.user()).isNotNull();
            assertThat(response.review()).isNotBlank();
            assertThat(response.assessment()).isBetween(1, 5);
            assertThat(response.readPages()).isNotNull();
            assertThat(response.created()).isNotNull();
        });
    }

    @Test
    @DisplayName("404 — Получение отзыва по несуществующему ID")
    void get404ResponseReviewByIdTest() {
        String accessToken = step(
                "Получение токена авторизации",
                this::getAccessToken
        );

        ReviewNotFoundResponseModel response = step(
                "Получение отзыва по несуществующему ID",
                () -> api.review.getReviewByIdNotFound(NOT_FOUND_REVIEW_ID, accessToken)
        );

        step("Проверка ответа об отсутствии отзыва", () ->
                assertThat(response.detail())
                        .isEqualTo("No BookReview matches the given query.")
        );
    }

    @Test
    @DisplayName("Успешное полное обновление отзыва по ID")
    void fullUpdateReviewByIdTest() {
        String accessToken = step(
                "Получение токена авторизации",
                this::getAccessToken
        );

        CreateReviewRequestModel request = step(
                "Подготовка данных для обновления отзыва",
                () -> reviewTestData.createNewReview(CLUBS_ID)
        );

        ReviewResponseModel response = step(
                "Полное обновление отзыва через API",
                () -> api.review.fullUpdateReviewById(
                        VALID_REVIEW_ID,
                        request,
                        accessToken
                )
        );

        step("Проверка обновлённого отзыва в API", () -> {
            assertThat(response.id()).isEqualTo(VALID_REVIEW_ID);
            assertThat(response.club()).isEqualTo(request.club());
            assertThat(response.review()).isEqualTo(request.review());
            assertThat(response.assessment()).isEqualTo(request.assessment());
            assertThat(response.readPages()).isEqualTo(request.readPages());
            assertThat(response.user()).isNotNull();
            assertThat(response.created()).isNotNull();
        });

        step("Проверка обновлённого отзыва в UI", () ->
                bookPage.openBookPageByClubId(request.club())
                        .checkCountPageInReview(request.readPages())
                        .checkReviewContent(request.review())
        );
    }

    @Test
    @DisplayName("403 — Запрет полного обновления чужого отзыва")
    void fullUpdateReviewByIdForbiddenTest() {
        String accessToken = step(
                "Получение токена авторизации",
                this::getAccessToken
        );

        CreateReviewRequestModel request = step(
                "Подготовка данных для обновления чужого отзыва",
                () -> reviewTestData.createNewReview(FORBIDDEN_CLUB_ID)
        );

        step("Попытка обновления чужого отзыва", () ->
                api.review.fullUpdateReviewByIdForbidden(
                        FORBIDDEN_REVIEW_ID,
                        request,
                        accessToken
                )
        );
    }

    @Test
    @DisplayName("Успешное частичное обновление отзыва по ID")
    void patchUpdateReviewByIdTest() {
        String accessToken = step(
                "Получение токена авторизации",
                this::getAccessToken
        );

        ReviewPatchRequestModel request = step(
                "Подготовка данных для частичного обновления отзыва",
                () -> reviewTestData.patchUpdateReview(CLUBS_ID)
        );

        ReviewResponseModel response = step(
                "Частичное обновление отзыва через API",
                () -> api.review.patchUpdateReviewById(
                        VALID_REVIEW_ID,
                        request,
                        accessToken
                )
        );

        step("Проверка частично обновлённого отзыва в API", () -> {
            assertThat(response.id()).isEqualTo(VALID_REVIEW_ID);
            assertThat(response.club()).isEqualTo(request.clubId());
            assertThat(response.review()).isEqualTo(request.review());
            assertThat(response.readPages()).isEqualTo(request.readPages());
            assertThat(response.user()).isNotNull();
            assertThat(response.created()).isNotNull();
        });

        step("Проверка обновлённого отзыва в UI", () ->
                bookPage.openBookPageByClubId(request.clubId())
                        .checkCountPageInReview(request.readPages())
                        .checkReviewContent(request.review())
        );
    }

    @Test
    @DisplayName("Успешное удаление отзыва и проверка в UI")
    void successfulDeleteReviewById() {
        SuccessfulLoginResponseModel auth = step(
                "Получение токенов авторизации",
                this::getAccessAndRefreshToken
        );

        ClubRequestModel clubRequest = step(
                "Подготовка данных нового клуба",
                clubTestData::newClubBody
        );

        ClubResponseModel createdClub = step(
                "Создание нового клуба через API",
                () -> api.clubs.postCreateClub(clubRequest, auth.access())
        );

        CreateReviewRequestModel reviewRequest = step(
                "Подготовка данных нового отзыва",
                () -> reviewTestData.createNewReview(createdClub.id())
        );

        ReviewResponseModel createdReview = step(
                "Создание отзыва через API",
                () -> api.review.createNewReview(auth.access(), reviewRequest)
        );

        BrowserAuthModel browserAuth = step(
                "Подготовка данных авторизации для браузера",
                () -> BrowserAuthData.createBrowserAuth(
                        auth.access(),
                        auth.refresh()
                )
        );

        step("Открытие favicon для инициализации браузерной сессии", () ->
                open("https://book-club.qa.guru/favicon.ico")
        );

        step("Сохранение авторизации в Local Storage", () ->
                browserAuthHelper.setBrowserAuth(browserAuth)
        );

        step("Проверка отображения отзыва в UI", () ->
                bookPage.openBookPageByClubId(createdClub.id())
                        .checkCountPageInReview(createdReview.readPages())
                        .checkReviewContent(createdReview.review())
        );

        step("Удаление отзыва через API", () ->
                api.review.deleteReviewById(createdReview.id(), auth.access())
        );

        step("Проверка отсутствия отзыва в UI после удаления", () ->
                bookPage.openBookPageByClubId(createdClub.id())
                        .checkNoReviewMessage(NO_REVIEW_MESSAGE)
        );
    }

    @Test
    @DisplayName("Авторизация в браузере через API")
    void authBrowser() {
        SuccessfulLoginResponseModel auth = step(
                "Получение токенов авторизации через API",
                this::getAccessAndRefreshToken
        );

        BrowserAuthModel browserAuth = step(
                "Подготовка данных авторизации для браузера",
                () -> BrowserAuthData.createBrowserAuth(
                        auth.access(),
                        auth.refresh()
                )
        );

        step("Открытие favicon для инициализации браузерной сессии", () ->
                open("https://book-club.qa.guru/favicon.ico")
        );

        step("Сохранение авторизации в Local Storage", () ->
                browserAuthHelper.setBrowserAuth(browserAuth)
        );

        step("Открытие сайта с авторизацией", () ->
                open("https://book-club.qa.guru")
        );
    }
}