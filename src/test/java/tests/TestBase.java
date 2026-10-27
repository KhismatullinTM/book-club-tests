package tests;

import api.ApiClient;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.WebDriverRunner;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.restassured.RestAssured;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import tests.helpers.Attach;
import io.qameta.allure.selenide.AllureSelenide;

import java.util.Map;

import static com.codeborne.selenide.Selenide.*;
import static tests.testdata.TestData.LOGIN_PASSWORD;
import static tests.testdata.TestData.LOGIN_USERNAME;

public class TestBase {

    protected static final ApiClient api = new ApiClient();

    protected String getAccessToken() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        return api.auth.loginAndGetAccessToken(loginData);
    }

    protected SuccessfulLoginResponseModel getAccessAndRefreshToken() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        return api.auth.login(loginData);
    }

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://book-club.qa.guru";
        Configuration.baseUrl = "https://book-club.qa.guru";

        DesiredCapabilities capabilities = new DesiredCapabilities();
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments(
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--host-rules=MAP personalization-web.g.mindbox.ru 0.0.0.0, " +
                        "MAP personalization-speedtest.g.mindbox.ru 0.0.0.0, " +
                        "MAP web-static.mindbox.ru 0.0.0.0, " +
                        "MAP personalization-web.mindbox.ru 0.0.0.0"
        );
        capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);

        capabilities.setCapability("selenoid:options", Map.<String, Object>of(
                "enableVNC", true,
                "enableVideo", true
        ));

        Configuration.browserCapabilities = capabilities;

        String remoteUrl = System.getProperty("remoteUrl");
        Configuration.remote = remoteUrl;
    }

    @BeforeEach
    void setUpPage() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void tearDown() {
        if (WebDriverRunner.hasWebDriverStarted()) {
            Attach.screenshotAs("Last screenshot");
            Attach.pageSource();
            Attach.browserConsoleLogs();
            Attach.addVideo();
            closeWebDriver();
        }
    }
}