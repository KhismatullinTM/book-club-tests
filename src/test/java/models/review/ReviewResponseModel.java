package models.review;

public record ReviewResponseModel(Integer id,
                                  Integer club,
                                  ReviewUser user,
                                  String review,
                                  Integer assessment,
                                  Integer readPages,
                                  String created,
                                  String modified) {
}