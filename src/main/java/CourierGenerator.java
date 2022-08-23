public class CourierGenerator {

    public static Courier getCourierCreate() {
        return new Courier("Courier_new", "password", "Vadfksjd");
    }

    public static Courier getDefault() {
        return new Courier("aCourfd", "password", "Ivanov");
    }

    public static Courier getDuplicateLogin() {
        return new Courier("aCourfd", "password1", "Ivanov");
    }

    public static Courier CourierNonAutorization() {
        return new Courier("Cou", "password1", "Petrov");
    }

}
