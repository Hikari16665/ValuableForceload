package me.eventually.valuableforceload.utils;

public class TypeUtil {
    public static Boolean isAllNull(Object... objects) {
        for (Object object : objects) {
            if (object != null) {
                return false;
            }
        }
        return true;
    }
}
