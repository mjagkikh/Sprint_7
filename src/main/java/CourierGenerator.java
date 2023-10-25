import org.apache.commons.lang.RandomStringUtils;

import java.time.LocalDateTime;

public class CourierGenerator {

    public static Courier random() {
        return new Courier("Test" + RandomStringUtils.randomAlphanumeric(5), "123", "BossOfTheGYM");
    }
}
