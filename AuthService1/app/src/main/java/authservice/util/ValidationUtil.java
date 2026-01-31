package authservice.util;

import authservice.model.UserInfoDto;

public class ValidationUtil {
    private static final String EMAIL_REGEX = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
    private static final String PASSWORD_REGEX =
            "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";


    public static Boolean validateUser(UserInfoDto user) {

        if (user == null) return Boolean.FALSE;

        String email = user.getEmail();
        String password = user.getPassword();

        if (email == null || !email.matches(EMAIL_REGEX)) {
                return Boolean.FALSE;
        }

        if (password == null || !password.matches(PASSWORD_REGEX)) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

}
