package models.review;

import java.util.List;

public record ReviewListResponseModel(Integer count,
                                      String next,
                                      String previous,
                                      List<ResultsList> results) {
}