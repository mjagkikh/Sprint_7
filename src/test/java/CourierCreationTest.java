import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class CourierCreationTest {

    private final CourierClient client = new CourierClient();
    private final CourierAssertions check = new CourierAssertions();
    protected int courierId;

    @After
    public void deleteCourier() {
        ValidatableResponse delete = client.delete(courierId);
        check.deletedSuccessfully(delete);
    }

    @Test
    @DisplayName("Courier creation with login then")
    public void courierCreationPositiveTest() {
        Courier courier = CourierGenerator.random();
        ValidatableResponse response = client.createCourier(courier);
        check.createdSuccessfully(response);

        Credentials credentials = Credentials.from(courier);
        ValidatableResponse loginResponse = client.login(credentials);
        courierId = check.loggedInSuccessfully(loginResponse);

        assert courierId != 0;
    }

    @Test
    @DisplayName("Courier creation with the same credentials")
    public void courierCreationWithSameCredentials() {
        Courier courier = CourierGenerator.random();
        ValidatableResponse response = client.createCourier(courier);
        check.createdSuccessfully(response);

        ValidatableResponse response1 = client.createCourier(courier);
        check.createdUnsuccessfully409(response1);

        Credentials credentials = Credentials.from(courier);
        ValidatableResponse loginResponse = client.login(credentials);
        courierId = check.loggedInSuccessfully(loginResponse);

        assert courierId != 0;
    }
}
