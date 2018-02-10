package me.rayzr522.flash.utils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArrayUtils {
    public static String join(List<?> objects, String joiner) {
        return objects.stream().map(Objects::toString).collect(Collectors.joining(joiner));
    }
}
