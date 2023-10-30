package org.example.util;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.UUID;

public class UtilValidator {
    public static void checkUUIDAccountBody(UUID account) {
        if(Objects.isNull(account)) {
            throw new RuntimeException("Account not entered");
        }
    }

    public static UUID checkUUIDParameter(String strUUID) {
        try {
            UUID uuid = UUID.fromString(strUUID);

            if(!uuid.toString().equals(strUUID)){
                throw new IllegalArgumentException();
            }

            return uuid;
        } catch (RuntimeException e) {
            throw new RuntimeException("UUID not valid");
        }
    }

    public static void checkString(String str) {
        if(Objects.isNull(str) || str.isEmpty() || str.isBlank()) {
            throw new RuntimeException("Data not entered");
        }
    }

    public static LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            throw new RuntimeException("Invalid date format");
        }
    }


}
