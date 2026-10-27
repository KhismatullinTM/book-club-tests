package models.review;

public record ReviewPatchRequestModel(Integer clubId,
                                      String review,
                                      Integer readPages) {
}
