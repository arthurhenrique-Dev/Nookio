package com.henrique.nookio_api.modules.files.annotations.chain;

import lombok.RequiredArgsConstructor;
import org.apache.tika.Tika;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

@RequiredArgsConstructor
public class FileProcess extends ValidateProcess{

    private static final Tika tika = new Tika();
    private final String[] allowedTypes;
    public static final long MAX_FILE_SIZE = 30 * 1024 * 1024;

    @Override
    protected boolean validate(MultipartFile file) {
        if (file == null && file.getSize() > MAX_FILE_SIZE) return false;
        try {
            InputStream inputStream = file.getInputStream();
            String type = tika.detect(inputStream);
            if (!Arrays.stream(allowedTypes).anyMatch(allowed -> allowed.equalsIgnoreCase(type))) return false;
            return true;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
