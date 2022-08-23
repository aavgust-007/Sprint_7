import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.*;

public class CourierCreateTest {

    private Courier courier;
    private Courier courierDublicate;
    private int courierIdDublicate;
    private CourierClient courierClient;
    private int courierId;


    @Before
    public void setUp(){
        courier = CourierGenerator.getDefault();
        courierDublicate = CourierGenerator.getDuplicateLogin();
        courierClient = new CourierClient();

    }

    @After
    public void tearDown(){
       courierClient.delete(courierId);
        courierClient.delete(courierIdDublicate);
    }

    @Test
    public void  courierCanBeCreatedTest(){
        ValidatableResponse response;
        response = courierClient.create(courier);
        int statusCode = response.extract().statusCode();
        assertEquals("Status code is incorrecr", SC_CREATED , statusCode);
        boolean isCreated = response.extract().path("ok");
        assertTrue("Courier is not created", isCreated);
        ValidatableResponse loginResponce = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponce.extract().path("id");
        assertNotNull("Id is null",  courierId);
        assert(courierId > 0);
    }

    @Test
    public void  errorWhenCreatingDublicateCourierTest(){

        ValidatableResponse   response = courierClient.create(courier);
        ValidatableResponse   response2 = courierClient.create(courier);
        int statusCode2 = response2.extract().statusCode();
        assertEquals("You can create two identical couriers", SC_CONFLICT , statusCode2);
        ValidatableResponse loginResponce = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponce.extract().path("id");
    }

    @Test
    public void  сreatingCourierNotWithAllRequiredFields(){

        ValidatableResponse   response = courierClient.createLogin(CourierLogin.from(courier));
        int statusCode2 = response.extract().statusCode();
        assertEquals("You can create two identical couriers", SC_BAD_REQUEST , statusCode2);
        String message = response.extract().path("message");
        assertEquals("Incorrect message when creating a client with incomplete data",
                "Недостаточно данных для создания учетной записи" , message);
    }

    @Test
    public void  creatingCourierWithDuplicateLogin(){

        ValidatableResponse   response = courierClient.create(courier);
        int statusCode1 = response.extract().statusCode();
        ValidatableResponse   response2 = courierClient.create(courierDublicate);
        int statusCode2 = response.extract().statusCode();
        String message = response.extract().path("message");
        assertEquals("Incorrect code when creating a client with a duplicate login", SC_CONFLICT , statusCode2);
        assertEquals("Incorrect message when creating a client with a duplicate login",
                "Этот логин уже используется. Попробуйте другой." , message);
        ValidatableResponse loginResponce = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponce.extract().path("id");
        ValidatableResponse loginResponceDublicate = courierClient.login(CourierCredentials.from(courierDublicate));
        courierIdDublicate = loginResponceDublicate.extract().path("id");

    }

}
