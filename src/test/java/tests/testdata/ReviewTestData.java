package tests.testdata;

import models.review.CreateReviewRequestModel;
import models.review.ReviewPatchRequestModel;
import net.datafaker.Faker;

public class ReviewTestData {

    private final Faker faker = new Faker();

    public CreateReviewRequestModel createNewReview(Integer clubId) {
        return new CreateReviewRequestModel(
                clubId,
                faker.lorem().sentence(),
                faker.number().numberBetween(1, 6),
                faker.number().numberBetween(0, 201)
        );
    }

    public ReviewPatchRequestModel patchUpdateReview(Integer clubId) {
        return new ReviewPatchRequestModel(
                clubId,
                faker.lorem().sentence(),
                faker.number().numberBetween(0, 201)
        );
    }
}
