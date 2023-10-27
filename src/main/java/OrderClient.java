import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;

public class OrderClient extends Client {

    static final String ORDER_PATH = "/orders";

    @Step("Send POST request to create order")
    public ValidatableResponse createOrder (Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    @Step("Send PUT request to cancel order /orders")
    public ValidatableResponse cancelOrder (int track) {
        String json = String.format("{\"track\": %d}", track);
        return spec()
                .body(json)
                .when()
                .put(ORDER_PATH)
                .then().log().all();
    }

    @Step("Send GET request to get order list /orders")
    public ValidatableResponse getlOrderList () {
          return spec()
                .get(ORDER_PATH)
                .then().log().all();
    }
}
