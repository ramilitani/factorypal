package com.factorypal.speedmetrics.utils;

import com.factorypal.speedmetrics.exceptions.FactoryPalException;
import org.apache.logging.log4j.util.Strings;
import org.springframework.http.HttpStatus;
import org.springframework.web.multipart.MultipartFile;

public class CsvFileValidator {

    private static final String CSV_EXTENSION = "csv";
    private static final String COMMA = ",";
    private static final String SEMICOLON = ";";

    public static void validate(MultipartFile uploadFile) throws FactoryPalException {

        String fileName = uploadFile.getOriginalFilename();
        String extension = fileName.substring(fileName.lastIndexOf(".")+1);

        if (!extension.equals(CSV_EXTENSION)) {
            throw new FactoryPalException("Invalid file format", HttpStatus.BAD_REQUEST);
        }
    }

    public static String[] validate(String line, int length) {

        boolean valid = true;
        String[] keyValue = line.split(COMMA + "|" + SEMICOLON);
        if (keyValue.length == length) {
            for (int i = 0; i < length; i++) {
                if (Strings.isBlank(keyValue[i])) {
                    valid = false;
                    break;
                }
            }

            if (valid) return keyValue;
        }

        throw new FactoryPalException(String.format("Invalid file format in line: %s", line), HttpStatus.BAD_REQUEST);
    }
}
