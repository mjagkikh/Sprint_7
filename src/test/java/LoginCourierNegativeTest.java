import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.commons.lang.RandomStringUtils;
import org.junit.Before;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;


public class LoginCourierNegativeTest {

    private CourierClient courierClient;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
    }

    @Test
    @DisplayName("Courier can't login without field login in body")
    @Description("Без поля логина")
    public void courierCantLoginWithoutLoginInBody400() {
        String password = RandomStringUtils.randomAlphanumeric(10);
        Credentials creds = new Credentials(password);

        ValidatableResponse loginResponse = courierClient.login(creds);
        int statusCode = loginResponse.extract().statusCode();
        String massage = loginResponse.extract().path("message");

        assertThat(statusCode, equalTo(HttpURLConnection.HTTP_BAD_REQUEST));
        assertThat(massage, equalTo("Недостаточно данных для входа"));

    }

    @Test
    @DisplayName("Courier can't login with null login")
    @Description("Передаём в боди login:null")
    public void courierCantLoginWithNullLogin400() {
        Courier courier = CourierGenerator.random();
        ValidatableResponse loginResponse = courierClient.login(new Credentials(null, courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");

        assertThat(statusCode, equalTo(HttpURLConnection.HTTP_BAD_REQUEST));
        assertThat(message, equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Courier can't login with incorrect login")
    @Description("Система вернёт ошибку, если неправильно указать логин")
    public void courierCantLoginWithIncorrectLogin404() {
        Courier courier = CourierGenerator.random();
        courierClient.createCourier(courier);

        Credentials creds = Credentials.from(courier);
        ValidatableResponse loginResponse = courierClient.login(creds);
        courierId = loginResponse.extract().path("id");

        loginResponse = courierClient.login(new Credentials("         " + courier.getLogin(), courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        courierClient.delete(courierId);

        assertThat(statusCode, equalTo(HttpURLConnection.HTTP_NOT_FOUND));
        assertThat(message, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Courier can't login with incorrect password")
    @Description("Система вернёт ошибку, если неправильно указать пароль")
    public  void courierCantLoginWithIncorrectPassword404() {
        Courier courier = CourierGenerator.random();
        courierClient.createCourier(courier);

        Credentials creds = Credentials.from(courier);
        ValidatableResponse loginResponse = courierClient.login(creds);
        courierId = loginResponse.extract().path("id");

        loginResponse =  courierClient.login(new Credentials(courier.getLogin(), "        " + courier.getPassword()));
        int statusCode = loginResponse.extract().statusCode();
        String message = loginResponse.extract().path("message");
        courierClient.delete(courierId);

        assertThat(statusCode, equalTo(HttpURLConnection.HTTP_NOT_FOUND));
        assertThat(message, equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Courier can't login without created data")
    @Description("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку")
    public void courierCantLoginWithoutCreatedData404() {
        ValidatableResponse loginResponse = courierClient.login(new Credentials("loginWithoutCreation", "sOmepssw"));
        int statusCode = loginResponse.extract().statusCode();
        String message =  loginResponse.extract().path("message");

       assertThat(statusCode, equalTo(HttpURLConnection.HTTP_NOT_FOUND));
       assertThat(message, equalTo("Учетная запись не найдена"));
    }
}