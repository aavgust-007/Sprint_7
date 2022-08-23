import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class CourierLoginTest {

    private Courier courier;

    private Courier courierWrongPassword;
    private CourierClient courierClient;
    private Courier CourierNonAutorization;
    private int courierId;

    @Before
    public void setUp() {
        courier = CourierGenerator.getCourierCreate();
        courierWrongPassword = CourierGenerator.getDuplicateLogin();
        CourierNonAutorization = CourierGenerator.CourierNonAutorization();
        courierClient = new CourierClient();

    }

    @After
    public void tearDown() {
        courierClient.delete(courierId);
    }

    @Test
    public void courierAuthorizationTest() {
        ValidatableResponse response = courierClient.create(courier);
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrecr", SC_CREATED, statusCode);
        ValidatableResponse loginResponce = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponce.extract().path("id");
        assertNotNull("Id is null", courierId);
    }

    @Test
    public void authorizationOfCourierWithAnIncorrectPasswordTest() {
        ValidatableResponse loginResponce = courierClient.login(CourierCredentials.from(courierWrongPassword));
        int statusCode = loginResponce.extract().statusCode();
        assertEquals("Status code is incorrecr", SC_NOT_FOUND, statusCode);

    }

    @Test
    public void loginCourierNotWithAllRequiredFieldsTest() {

        ValidatableResponse response = courierClient.loginNotWithPassword(CourierLogin.from(courier));
        int statusCode = response.extract().statusCode();
        assertEquals("You can create two identical couriers", SC_BAD_REQUEST, statusCode);
        String message = response.extract().path("message");
        assertEquals("Insufficient login data",
                "Недостаточно данных для входа", message);
    }

    @Test
    public void AuthorizationErrorUnderNonexistentUserTest() {

        ValidatableResponse response = courierClient.login(CourierCredentials.from(CourierNonAutorization));
        int statusCode = response.extract().statusCode();
        assertEquals("Incorrect response code when logging in under an existing user", SC_NOT_FOUND, statusCode);
        String message = response.extract().path("message");
        assertEquals("It is mistakenly possible to log in under an existing user",
                "Учетная запись не найдена", message);
    }
}
