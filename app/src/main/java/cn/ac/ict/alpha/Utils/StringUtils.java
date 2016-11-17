package cn.ac.ict.alpha.Utils;

/**
 * Author: saukymo
 * Date: 11/14/16
 */

public class StringUtils {
    private static final String USER_NAME_REGEX = "^[a-zA-Z]\\w{2,19}$";
    private static final String PASSWORD_REGEX = "^[a-zA-Z]\\w{3,14}$";
//    private static final String EMAIL_REGEX = "^([\\w\\-\\.]+)@([\\w-]+\\.)+[\\w-]{2,4}$";
    private static final String PHONE_NUMBER_REGEX = "^[0-9]{11}$";

    public static boolean checkUserName(String userName) {
        return userName.matches(USER_NAME_REGEX);
    }

    public static boolean checkPassword(String pwd) {
        return pwd.matches(PASSWORD_REGEX);
    }

    public static boolean checkEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email)
                .matches();
    }

    public static boolean checkPhoneNumber(String phoneNumber) {
//        return Patterns.PHONE.matcher(phoneNumber).matches();
        return phoneNumber.matches(PHONE_NUMBER_REGEX);
    }
}
