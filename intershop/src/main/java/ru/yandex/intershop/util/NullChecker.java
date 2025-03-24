package ru.yandex.intershop.util;

import java.util.function.Consumer;

public class NullChecker {

    public static <T> void setIfNotNull(final Consumer<T> targetConsumer, final T value) {

        if (value != null) {
            targetConsumer.accept(value);
        }
    }
}
