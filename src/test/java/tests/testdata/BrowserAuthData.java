package tests.testdata;

import models.browser.BrowserAuthModel;

public class BrowserAuthData {

    public static BrowserAuthModel createBrowserAuth(String accessToken, String refreshToken) {
        BrowserAuthModel.UserAuthModel user = new BrowserAuthModel.UserAuthModel(
                        9037,
                        "user_1791600701072",
                        "",
                        "",
                        "",
                        "136.169.152.204");
        return new BrowserAuthModel(
                user,
                accessToken,
                refreshToken,
                true
        );
    }
}
