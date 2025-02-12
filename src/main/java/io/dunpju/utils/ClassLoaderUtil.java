package io.dunpju.utils;

import java.io.File;

public class ClassLoaderUtil {
    public static Class load(String clazz) throws ClassNotFoundException {
        return Class.forName(clazz);
    }
}
