package tests.testdata;

public class TestData {


    public static final String LOGIN_USERNAME = "user_1791600701072";
    public static final String LOGIN_PASSWORD = "pass_1791600701073";
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

    public static final String UPDATE_USERNAME = "updatedUser123";
    public static final String UPDATE_FIRST_NAME = "Test";
    public static final String UPDATE_LAST_NAME = "User";
    public static final String UPDATE_EMAIL = "updated.user@example.com";
    public static final String CLUB_TELEGRAM_LINK = "https://t.me/clean_code_club";

    public static final int CLUBS_ID = 6763;
    public static final int CLUB_ID_FORBIDDEN_TO_EDIT = 40;
    public static final String FORBIDDEN_ERROR_TEXT = "You do not have permission to perform this action";
    public static final Integer NOT_FOUND_REVIEW_ID = 999;
    public static final Integer VALID_REVIEW_ID = 1939;
    public static final Integer FORBIDDEN_CLUB_ID = 2;
    public static final Integer FORBIDDEN_REVIEW_ID = 1397;
}

