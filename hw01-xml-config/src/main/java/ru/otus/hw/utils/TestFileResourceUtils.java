package ru.otus.hw.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

public class TestFileResourceUtils {
    public static Reader getResourceFileReader(Class<?> loadedClass, String filePath) {
        ClassLoader loader = loadedClass.getClassLoader();
        InputStream inputStream = loader.getResourceAsStream(filePath);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found:" + filePath);
        }

        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);

        return new BufferedReader(inputStreamReader);
    }
}
