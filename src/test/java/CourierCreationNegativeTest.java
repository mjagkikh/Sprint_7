import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreationNegativeTest {
    private CourierClient courierClient;

    @Before
    public void setUp() {
        courierClient = new CourierClient();

    }

    @Test
    @DisplayName("Courier creation with null login")
    @Description("Если одного из полей нет, запрос возвращает ошибку")
    public void courierCreationWithNullLogin() {
        String password = RandomStringUtils.randomAlphanumeric(10);
        ValidatableResponse createResponse = courierClient.createCourier(new Courier(null, password, null));

        int statusCode = createResponse.extract().statusCode();
        assertThat(statusCode, equalTo(HttpURLConnection.HTTP_BAD_REQUEST));

        String message = createResponse.extract().path("message");
        assertThat(message, equalTo("Недостаточно данных для создания учетной записи"));

    }

    @Test
    @DisplayName("Courier creation with null password")
    @Description("Если одного из полей нет, запрос возвращает ошибку")
    public void courierCreationWithNullPassword() {
        String login = RandomStringUtils.randomAlphanumeric(10);
        ValidatableResponse createResponse = courierClient.createCourier(new Courier(login, null, null));

        int statusCode = createResponse.extract().statusCode();
        assertThat(statusCode, equalTo(HttpURLConnection.HTTP_BAD_REQUEST));

        String message = createResponse.extract().path("message");
        assertThat(message, equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test

    @DisplayName("Courier creation with null fields")
    @Description("Если ни одного из полей нет, запрос возвращает ошибку")
    public void courierCreationWithAllNullFields() {
        ValidatableResponse courierResponse = courierClient.createCourier(new Courier(null, null, null));

        int statusCode = courierResponse.extract().statusCode();
        assertThat(statusCode, equalTo(HttpURLConnection.HTTP_BAD_REQUEST));
        String message = courierResponse.extract().path("message");
        assertThat(message, equalTo("Недостаточно данных для создания учетной записи"));

    }
}
