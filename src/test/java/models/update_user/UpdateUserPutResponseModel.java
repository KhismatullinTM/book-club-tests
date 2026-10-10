package models.update_user;

public record UpdateUserPutResponseModel(
          int id,
          String username,
          String firstName,
          String lastName,
          String email,
          String remoteAddr
) {}