package ru.yandex.intershop.util;

import ru.yandex.intershop.error.exception.IncorrectRequestException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class CustomFormatter {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");


    public static LocalDate stringToDate(String dateString) {

        try {
            if (dateString != null) {
                return LocalDate.parse(dateString, DATE_FORMATTER);
            } else {
                return null;
            }
        } catch (DateTimeParseException e) {
            throw new IncorrectRequestException("- Wrong date format");
        }
    }

    public static String dateToString(LocalDate date) {

        if (date != null) {
            return date.format(DATE_FORMATTER);
        } else {
            return null;
        }
    }

    public static LocalDateTime stringToDateTime(String dateString) {

        try {
            if (dateString != null) {
                return LocalDateTime.parse(dateString, DATE_TIME_FORMATTER);
            } else {
                return null;
            }
        } catch (DateTimeParseException e) {
            throw new IncorrectRequestException("- Wrong date and time format");
        }
    }

    public static String dateTimeToString(LocalDateTime dateTime) {

        return dateTime.format(DATE_TIME_FORMATTER);
    }
}
