package tests;

import api.ApiClient;
import io.restassured.RestAssured;
import models.login.LoginBodyModel;
import org.junit.jupiter.api.BeforeAll;

import static tests.testdata.TestData.LOGIN_PASSWORD;
import static tests.testdata.TestData.LOGIN_USERNAME;

public class TestBase {

    protected static final ApiClient api = new ApiClient();

    protected String getAccessToken() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        return api.auth.loginAndGetAccessToken(loginData);
    }

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://book-club.qa.guru";
    }
}
