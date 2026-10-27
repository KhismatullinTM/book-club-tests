package api;

import io.qameta.allure.Step;
import models.review.*;

import static io.restassured.RestAssured.given;
import static specs.review.ReviewSpecs.*;

public class ReviewApiClient {

    @Step("Поиск всех отзывов по клуба, со значением id: {club}")
    public ReviewListResponseModel getAllReview(String accessToken,
                                                Integer club,
                                                Integer page,
                                                Integer pageSize) {
        return given(reviewSpec)
                .header("Authorization", "Bearer " + accessToken)
                .queryParam("club", club)
                .queryParam("page", page)
                .queryParam("page_size", pageSize)
                .when()
                .get("/clubs/reviews/")
                .then()
                .spec(responseGetAllReviewsSpec)
                .extract()
                .as(ReviewListResponseModel.class);
    }

    @Step("Создание нового отзыва")
    public ReviewResponseModel createNewReview(String accessToken,
                                               CreateReviewRequestModel requestModel) {
        return given(reviewSpec)
                .header("Authorization", "Bearer " + accessToken)
                .when()
                .body(requestModel)
                .post("/clubs/reviews/")
                .then()
                .spec(responseCreateReviewSpec)
                .extract()
                .as(ReviewResponseModel.class);
    }

    @Step("500 - Получение ошибки при создании нового отзыва")
    public void givenInternalServerErrorReview(String accessToken, CreateReviewRequestModel requestModel) {
        given(reviewSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(requestModel)
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(responseServerErrorSpec);
    }

    @Step("Поиск отзывов по id: {id}")
    public ReviewResponseModel getReviewById(Integer id, String accessToken) {
        return given(reviewSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .when()
                .get("/clubs/reviews/{id}/")
                .then()
                .spec(responseGetReviewSpec)
                .extract()
                .as(ReviewResponseModel.class);
    }

    @Step("Поиск несуществующего отзыва по id: {id}")
    public ReviewNotFoundResponseModel getReviewByIdNotFound(Integer id, String accessToken) {
        return given(reviewSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .when()
                .get("/clubs/reviews/{id}/")
                .then()
                .spec(responseGetReviewByIdNotFoundSpec)
                .extract()
                .as(ReviewNotFoundResponseModel.class);
    }

    @Step("Полное изменение отзыва по id: {id}")
    public ReviewResponseModel fullUpdateReviewById(Integer reviewId, CreateReviewRequestModel requestModel, String accessToken) {
        return given(reviewSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", reviewId)
                .body(requestModel)
                .when()
                .put("/clubs/reviews/{id}/")
                .then()
                .spec(responseGetReviewSpec)
                .extract()
                .as(ReviewResponseModel.class);
    }

    @Step("Изменение чужого отзыва по id: {id}")
    public ReviewForbiddenResponseModel fullUpdateReviewByIdForbidden(Integer id, CreateReviewRequestModel requestModel, String accessToken) {
        return given(reviewSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .body(requestModel)
                .when()
                .put("/clubs/reviews/{id}/")
                .then()
                .spec(responseGetReviewByIdForbiddenSpec)
                .extract()
                .as(ReviewForbiddenResponseModel.class);
    }

    @Step("Частичное изменение отзыва по id: {id}")
    public ReviewResponseModel patchUpdateReviewById(Integer reviewId, ReviewPatchRequestModel requestModel, String accessToken) {
        return given(reviewSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", reviewId)
                .body(requestModel)
                .when()
                .patch("/clubs/reviews/{id}/")
                .then()
                .spec(responseGetReviewSpec)
                .extract()
                .as(ReviewResponseModel.class);
    }

    @Step("Поиск отзывов по id: {id}")
    public void deleteReviewById(Integer id, String accessToken) {
        given(reviewSpec)
                .header("Authorization", "Bearer " + accessToken)
                .pathParam("id", id)
                .when()
                .delete("/clubs/reviews/{id}/")
                .then()
                .statusCode(204);
    }
}