package models.review;

public record ReviewRequestModel(Integer club,
                                 String review,
                                 Integer assessment,
                                 Integer readPages) {
}