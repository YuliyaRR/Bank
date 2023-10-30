package org.example.util;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class UtilValidatorTest {
    private static final String UUID_STRING = "12605bea-4688-4a8a-b64f-d29e24eb6d81";
    private static final UUID ID = UUID.fromString(UUID_STRING);
    private static final String EXCEPTION_STRING_DATA_NOT_ENTERED = "Data not entered";
    private static final String EXCEPTION_ACCOUNT_NOT_ENTERED = "Account not entered";
    private static final String EXCEPTION_UUID_NOT_VALID = "UUID not valid";
    private static final String EXCEPTION_DATE_FORMAT_NOT_VALID = "Invalid date format";
    @Test
    public void checkUUIDAccountBodyWhenUUIDNotNullThenExceptionDoesNotThrow() {
        assertDoesNotThrow(() -> UtilValidator.checkUUIDAccountBody(ID));
    }

    @Test
    public void checkUUIDAccountBodyWhenUUIDNullThenExceptionThrows() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> UtilValidator.checkUUIDAccountBody(null));
        assertEquals(EXCEPTION_ACCOUNT_NOT_ENTERED, exception.getMessage());
    }

    @Test
    public void checkUUIDParameterWhenUUIDIsValidThenExceptionDoesNotThrow() {
        UUID uuid = assertDoesNotThrow(() -> UtilValidator.checkUUIDParameter(UUID_STRING));
        assertEquals(ID, uuid);
    }

    @Test
    public void checkUUIDParameterWhenUUIDMissSomeNumsThenExceptionThrows() {
        String invalidUUID = "bea-4688-a8a-64f-b6d81";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> UtilValidator.checkUUIDParameter(invalidUUID));
        assertEquals(EXCEPTION_UUID_NOT_VALID, exception.getMessage());
    }

    @Test
    public void checkUUIDParameterWhenUUIDFormatNotValidThenExceptionThrows() {
        String invalidUUID = "wesrdtfyguhijk";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> UtilValidator.checkUUIDParameter(invalidUUID));
        assertEquals(EXCEPTION_UUID_NOT_VALID, exception.getMessage());
    }

    @Test
    public void checkUUIDParameterWhenUUIDNullThenExceptionThrows() {
        String invalidUUID = null;
        RuntimeException exception = assertThrows(RuntimeException.class, () -> UtilValidator.checkUUIDParameter(invalidUUID));
        assertEquals(EXCEPTION_UUID_NOT_VALID, exception.getMessage());
    }

    @Test
    public void checkStringWhenStringValidThenExceptionDoesNotThrow() {
        assertDoesNotThrow(() -> UtilValidator.checkString("Client"));
    }

    @Test
    public void checkStringWhenStringIsEmptyThenExceptionThrows() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> UtilValidator.checkString(""));
        assertEquals(EXCEPTION_STRING_DATA_NOT_ENTERED, exception.getMessage());
    }

    @Test
    public void checkStringWhenStringIsBlankThenExceptionThrows() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> UtilValidator.checkString("    "));
        assertEquals(EXCEPTION_STRING_DATA_NOT_ENTERED, exception.getMessage());
    }

    @Test
    public void checkStringWhenStringIsNullThenExceptionThrows() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> UtilValidator.checkString(null));
        assertEquals(EXCEPTION_STRING_DATA_NOT_ENTERED, exception.getMessage());
    }

    @Test
    public void parseDateWhenDateIsValidThenExceptionDoesNotThrow() {
        String date = "2023-10-13";
        assertDoesNotThrow(() -> UtilValidator.parseDate(date));
    }

    @Test
    public void parseDateWhenDateNotValidThenExceptionThrows() {
        String date = "13-10-2023";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> UtilValidator.parseDate(date));
        assertEquals(EXCEPTION_DATE_FORMAT_NOT_VALID, exception.getMessage());
    }
    @Test
    public void parseDateWhenDateIsEmptyThenExceptionThrows() {
        String date = "";
        RuntimeException exception = assertThrows(RuntimeException.class, () -> UtilValidator.parseDate(date));
        assertEquals(EXCEPTION_DATE_FORMAT_NOT_VALID, exception.getMessage());
    }
}