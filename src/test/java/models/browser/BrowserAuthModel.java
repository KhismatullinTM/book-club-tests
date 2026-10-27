package models.browser;

public record BrowserAuthModel(
        UserAuthModel user,
        String accessToken,
        String refreshToken,
        boolean isAuthenticated
) {
    public record UserAuthModel(
            int id,
            String username,
            String firstName,
            String lastName,
            String email,
            String remoteAddr
    ) {
    }
}