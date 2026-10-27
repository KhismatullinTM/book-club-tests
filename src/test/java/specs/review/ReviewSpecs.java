package specs.review;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.notNullValue;
import static specs.BaseSpec.baseRequestSpec;
import static org.hamcrest.Matchers.*;

public class ReviewSpecs {
    public static RequestSpecification reviewSpec = baseRequestSpec;

    public static ResponseSpecification responseGetAllReviewsSpec= new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/review/get_all_reviews_by_qwery_search_response_schema.json"))
            .expectBody("count", notNullValue())
            .expectBody("results", notNullValue())
            .expectBody("results.id", everyItem(notNullValue()))
            .expectBody("results.club", everyItem(notNullValue()))
            .expectBody("results.user", everyItem(notNullValue()))
            .expectBody("results.user.id", everyItem(notNullValue()))
            .expectBody("results.user.username", everyItem(notNullValue()))
            .expectBody("results.review", everyItem(notNullValue()))
            .expectBody("results.assessment", everyItem(notNullValue()))
            .expectBody("results.readPages", everyItem(notNullValue()))
            .expectBody("results.created", everyItem(notNullValue()))
            .build();

    public static ResponseSpecification responseCreateReviewSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/review/create_review_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("club", notNullValue())
            .expectBody("user.id", notNullValue())
            .expectBody("user.username", notNullValue())
            .expectBody("review", notNullValue())
            .expectBody("assessment", notNullValue())
            .expectBody("readPages", notNullValue())
            .expectBody("created", notNullValue())
            .build();

    public static ResponseSpecification responseGetReviewSpec = new ResponseSpecBuilder()
                    .log(ALL)
                    .expectStatusCode(200)
                    .expectBody(matchesJsonSchemaInClasspath(
                            "schemas/review/get_review_by_id_response_schema.json"))
                    .expectBody("id", notNullValue())
                    .expectBody("club", notNullValue())
                    .expectBody("user.id", notNullValue())
                    .expectBody("user.username", notNullValue())
                    .expectBody("review", notNullValue())
                    .expectBody("assessment", notNullValue())
                    .expectBody("readPages", notNullValue())
                    .expectBody("created", notNullValue())
                    .build();

    public static ResponseSpecification responseGetReviewByIdNotFoundSpec = new ResponseSpecBuilder()
                    .log(ALL)
                    .expectStatusCode(404)
                    .expectBody(matchesJsonSchemaInClasspath(
                            "schemas/review/get_review_by_id_not_found_response_schema.json"))
                    .expectBody("detail", notNullValue())
                    .build();

    public static ResponseSpecification responseGetReviewByIdForbiddenSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(403)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/review/update_review_forbidden_response_schema.json"))
            .expectBody("detail", equalTo("You do not have permission to perform this action."))
            .build();

    public static final ResponseSpecification responseServerErrorSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(500)
            .expectContentType(ContentType.HTML)
            .expectBody(matchesPattern(
                    "(?s).*<title>Server Error \\(500\\)</title>.*"
                            + "<h1>Server Error \\(500\\)</h1>.*"))
            .build();
}
