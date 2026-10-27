package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class BookPage {

    private final SelenideElement countPageInReview = $(".reviews-list .read-pages");
    private final SelenideElement reviewContent = $(".reviews-list .review-content");
    private final SelenideElement noReviewsMessage = $(".no-reviews p");

    @Step("Открытие страницы клуба с id: {id}")
    public BookPage openBookPageByClubId(Integer id) {
        open("/clubs/" + id);
        return this;
    }

    @Step("Проверка количества прочитанных страниц в отзыве")
    public BookPage checkCountPageInReview(Integer pages) {
        countPageInReview.shouldBe(visible).shouldHave(text(pages + " стр"));
        return this;
    }

    @Step("Проверка содержания отзыва")
    public BookPage checkReviewContent(String content) {
        reviewContent.shouldBe(visible).shouldHave(text(content));
        return this;
    }

    @Step("Проверка отсутствия отзыва")
    public BookPage checkNoReviewMessage(String message) {
        noReviewsMessage.shouldBe(visible).shouldHave(text(message));
        return this;
    }
}
