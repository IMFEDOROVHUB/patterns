import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Selectors;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;

public class CardDeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    public void main() {
        var userInfo = DataGenerator.Registration.generateUser("ru");

        var dayToAddFirstMeeting = 5;
        var firstMeetingDate = DataGenerator.generateDate(dayToAddFirstMeeting);
        var dayToAddSecondMeeting = 6;
        var secondMeetingDate = DataGenerator.generateDate(dayToAddSecondMeeting);


        $("[data-test-id='city'] input").setValue(userInfo.getCity());
        $("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(firstMeetingDate);
        $("[data-test-id='name'] input").setValue(userInfo.getName());
        $("[data-test-id='phone'] input").setValue(userInfo.getPhone());
        $("[data-test-id='agreement']").click();
        $$("button").findBy(Condition.exactText("Запланировать")).click();

        $(Selectors.byText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + firstMeetingDate));

        $("[data-test-id='date'] input").press(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(secondMeetingDate);
        $$("button").findBy(Condition.exactText("Запланировать")).click();

        $("[data-test-id='replan-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("У вас уже запланирована встреча на другую дату. Перепланировать?"));

        $("[data-test-id='replan-notification'] button").click();

        $("[data-test-id='success-notification'] .notification__content")
                .shouldBe(Condition.visible)
                .shouldHave(Condition.text("Встреча успешно запланирована на " + secondMeetingDate));
    }
}

