package geacommerce.common;

public class Constants {

    public static final String NOT_NULL_REGISTER_NAME_MESSAGE = "Името трябва да се попълни с подходяща стойност.";
    public static final String NOT_EMPTY_REGISTER_NAME_MESSAGE = "Името не трябва да е празно.";

    public static final String NOT_NULL_REGISTER_LASTNAME_MESSAGE = "Фамилията трябва да се попълни с подходяща стойност.";
    public static final String NOT_EMPTY_REGISTER_LASTNAME_MESSAGE = "Фамилията не трябва да е празна.";

    public static final String NOT_NULL_LOGIN_REGISTER_EMAIL_MESSAGE = "Имейлът трябва да се попълни с подходяща стойност.";
    public static final String NOT_EMPTY_LOGIN_REGISTER_EMAIL_MESSAGE = "Имейлът не трябва да е празен.";

    public static final String USER_REGISTER_PHONE_PATTERN = "\\d{9}";
    public static final String NOT_NULL_REGISTER_PHONE_MESSAGE = "Телефонният номер трябва да се попълни с подходяща стойност.";
    public static final String NOT_EMPTY_REGISTER_PHONE_MESSAGE = "Телефонният номер не трябва да е празен.";

    public static final String NOT_NULL_REGISTER_ADDRESS_MESSAGE = "Адресът трябва да се попълни с подходяща стойност.";
    public static final String NOT_EMPTY_REGISTER_ADDRESS_MESSAGE = "Адресът не трябва да е празен.";

    public static final String NOT_NULL_REGISTER_TOWN_MESSAGE = "Градът трябва да се попълни с подходяща стойност.";
    public static final String NOT_EMPTY_REGISTER_TOWN_MESSAGE = "Градът не трябва да е празен.";

    public static final String USER_REGISTER_NAME_SIZE_MESSAGE = "Името трябва съдържа от 2 до 20 символа.";
    public static final String USER_REGISTER_LASTNAME_SIZE_MESSAGE = "Фамилията трябва съдържа от 2 до 20 символа.";
    public static final String USER_REGISTER_PHONE_SIZE_MESSAGE = "Телефонният номер трябва съдържа точно 9 цифри.";

    public static final String USER_REGISTER_LOGIN_EMAIL_MESSAGE = "Имейлът трябва да е валиден.";
    public static final String USER_LOGIN_REGISTER_PASSWORD_INVALID_MESSAGE = "Паролата трябва съдържа миниммум 3 символа.";
    public static final int USER_LOGIN_REGISTER_PASSWORD_MINIMUM_SIZE = 3;

    public static final int USER_REGISTER_LOGIN_NAME_MINIMUM_SIZE = 2;
    public static final int USER_REGISTER_LOGIN_NAME_MAXIMUM_SIZE = 20;

    public static final String NOT_NULL_INQUIRY_MESSAGE = "Съобщението трябва да се попълни с подходяща стойност.";
    public static final String NOT_EMPTY_INQUIRY_MESSAGE = "Съобщението не трябва да е празно.";
    public static final int INQUIRY_MESSAGE_MAXIMUM_SIZE = 500;

    public static final String INQUIRY_MESSAGE_SIZE = "Съобщението трябва да е максимум 500 символа.";
    public static final int INQUIRY_MESSAGE_USERNAME_MINIMUM_SIZE = 2;
    public static final int INQUIRY_MESSAGE_USERNAME_MAXIMUM_SIZE = 20;

    public static final String ADD_TO_CART_MINIMUM_QUANTITY = "Моля попълнете полето с валиден брой.";

    public static final String PRODUCT_SEARCH_PATTERN = "^[A-z0-9А-я\\s]+$";
    public static final String SEARCH_PATTERN_MESSAGE = "Специални символи не са позволени.";

    public static final String ORDER_STATUS_CONFIRMED = "Приета";

    public static final String BEARING_TYPE = "Лагер";
    public static final String BELT_TYPE = "Ремък";
    public static final String SEAL_TYPE = "Семеринг";
    public static final String OTHER_TYPE = "Друг";
    public static final String ALL_TYPE = "Всички";

    public static final int PRODUCT_NAME_MINIMUM_SIZE = 2;
    public static final int PRODUCT_NAME_MAXIMUM_SIZE = 20;
    public static final String PRODUCT_PRICE_MINIMUM_VALUE = "0.01";
    public static final int PRODUCT_AMOUNT_MINIMUM_VALUE = 1;
}
