package kodlama.io.rentacar.common.constants;

public class Messages {
    public static class Car {
        public static final String NotExists = "CAR_NOT_EXISTS";
        public static final String Exists = "CAR_ALREADY_EXISTS";
        public static final String NotAvailable = "CAR_NOT_AVAILABLE";
        public static final String NotRented = "CAR_IS_NOT_RENTED";
    }

    public static class Model {
        public static final String NotExists = "MODEL_NOT_EXISTS";
        public static final String Exists = "MODEL_ALREADY_EXISTS";
    }

    public static class Brand {
        public static final String NotExists = "BRAND_NOT_EXISTS";
        public static final String Exists = "BRAND_ALREADY_EXISTS";
    }

    public static class Maintenance {
        public static final String NotExists = "MAINTENANCE_NOT_EXISTS";
        public static final String CarExists = "CAR_IS_CURRENTLY_UNDER_MAINTENANCE";
        public static final String CarNotExist = "CAR_NOT_REGISTERED_FOR_MAINTENANCE";
        public static final String CarIsRented = "CAR_IS_CURRENTLY_RENTED_AND_CANNOT_BE_SERVICED_OR_MAINTENANCE";
    }

    public static class Rental {
        public static final String NotExists = "RENTAL_NOT_EXISTS";
    }

    public static class Payment {
        public static final String NotFound = "PAYMENT_NOT_EXISTS";
        public static final String CardNumberAlreadyExist = "CARD_NUMBER_ALREADY_EXIST";
        public static final String NotEnoughMoney = "NOT_ENOUGH_MONEY";
        public static final String NotAValidPayment = "NOT_A_VALID_PAYMENT";
        public static final String Failed = "PAYMENT_FAILED";

    }

    public static class Invoice {
        public static final String NotExists = "INVOICE_NOT_EXISTS";
    }
}
