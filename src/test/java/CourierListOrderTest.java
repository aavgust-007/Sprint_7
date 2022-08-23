import io.restassured.response.ValidatableResponse;
import org.junit.Test;

import java.util.List;

import static org.apache.http.HttpStatus.SC_OK;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CourierListOrderTest {
    private OrderListClient orderListClient;

    @Test
    public void createOrderTest() {

        orderListClient = new OrderListClient();
        ValidatableResponse orderListResponce = orderListClient.getList();
        int statusCode = orderListResponce.extract().statusCode();
        assertEquals("Status code is incorrecr", SC_OK, statusCode);
        List<Integer> listOrder = orderListResponce.extract().path("orders");
        assertNotNull("List order is null", listOrder);

    }
}
