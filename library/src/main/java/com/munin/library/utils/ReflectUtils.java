package com.munin.library.utils;

import java.lang.reflect.Field;

public class ReflectUtils {

    public static Field getField(Object object, String name) {
        Field field = null;
        try {
            field = object.getClass().getDeclaredField(name);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        return field;
    }
}
