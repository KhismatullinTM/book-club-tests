package tests;

public class TestData {

    public static final String LOGIN_USERNAME = "user_1791599234220";
    public static final String LOGIN_PASSWORD = "pass_1791599234220";
    public static final String LOGIN_WRONG_PASSWORD = "qaguru1234";

    public static final String LOGIN_TOKEN_PREFIX = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public static final String LOGIN_WRONG_CREDENTIALS_ERROR = "Invalid username or password.";

    public static final String REGISTRATION_EXISTING_USER_ERROR =
            "A user with that username already exists.";

    public static final String REGISTRATION_IP_REGEXP =
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}"
                    + "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$";

    public static final String LOGIN_NONEXISTENT_USERNAME = "nonexistent_user_12345";
    public static final String LOGIN_EMPTY_USERNAME = "";
    public static final String LOGIN_EMPTY_PASSWORD = "";

    public static final String EXPECTED_EMPTY_CREDENTIAL_ERROR = "This field may not be blank.";

    public static final String REGISTRATION_EMPTY_USERNAME = "";
    public static final String REGISTRATION_EMPTY_PASSWORD = "";

    public static final String UPDATE_USERNAME = "updatedUser123";
    public static final String UPDATE_FIRST_NAME = "Test";
    public static final String UPDATE_LAST_NAME = "User";
    public static final String UPDATE_EMAIL = "updated.user@example.com";
}

