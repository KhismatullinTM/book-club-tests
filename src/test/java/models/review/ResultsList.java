package models.review;

public record ResultsList(Integer id,
                          Integer club,
                          ReviewUser user,
                          String review,
                          Integer assessment,
                          Integer readPages,
                          String created,
                          String modified
){}