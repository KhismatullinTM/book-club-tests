package tests.testdata;

import io.qameta.allure.Step;
import models.clubs.ClubPatchRequestModel;
import models.clubs.ClubRequestModel;
import net.datafaker.Faker;

import static tests.testdata.TestData.*;


public class ClubTestData {

    private final Faker faker = new Faker();

    @Step("Создание тела запроса для полного обновления данных клуба")
    public ClubRequestModel newClubBody() {
        return new ClubRequestModel(
                "Club " + faker.book().title() + " " + faker.number().digits(6),
                faker.book().author(),
                faker.number().numberBetween(2000, 2026),
                faker.lorem().paragraph(),
                CLUB_TELEGRAM_LINK
        );
    }

    @Step("Создание тела запроса для частичного обновления данных клуба")
    public ClubPatchRequestModel patchUpdateClubBody() {
        return new ClubPatchRequestModel(
                faker.lorem().paragraph()
        );
    }
}
