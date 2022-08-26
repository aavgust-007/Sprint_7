public class CourierGenerator {

    public static Courier getCourierCreate() {
        return new Courier("dsdsdsd", "password", "Vadfksjd");
    }

    public static Courier getDefault() {
        return new Courier("qaCourfd1", "password", "Ivanov");
    }

    public static Courier getDuplicateLogin() {
        return new Courier("qaCourfd1", "password1", "Ivanov");
    }

    public static Courier CourierNonAuthorization() {
        return new Courier("qCou1", "password1", "Petrov");
    }

}