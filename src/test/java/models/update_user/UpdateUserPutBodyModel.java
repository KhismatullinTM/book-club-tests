package models.update_user;

public record UpdateUserPutBodyModel(
        String username,
        String firstName,
        String lastName,
        String email
) {}