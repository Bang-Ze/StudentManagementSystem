package utils;

import java.util.regex.Pattern;

public class ValidationUtil {

    public static boolean isValidAge(int age) {
        return age > 0;
    }

    public static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$"
    );

    public static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }

    public static boolean isValidGrade(String grade) {
        if (grade == null) return false;
        return grade.matches("(?i)^[ABCDF]$");
    }

    public static boolean isValidScore(double score) {
        return score >= 0 && score <= 100;
    }
}