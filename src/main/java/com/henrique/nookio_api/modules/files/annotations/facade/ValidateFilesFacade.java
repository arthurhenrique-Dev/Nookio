package com.henrique.nookio_api.modules.files.annotations.facade;

import com.henrique.nookio_api.modules.files.annotations.chain.FileProcess;
import com.henrique.nookio_api.modules.files.annotations.chain.ImageProcess;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class ValidateFilesFacade {

    private static final String JPEG = "image/jpeg";
    private static final String PNG = "image/png";
    private static final String PDF = "application/pdf";

    public boolean facade(MultipartFile file, String[] allowed_types){
        FileProcess begin = new FileProcess(allowed_types);
        if (file.getContentType().equals(JPEG) || file.getContentType().equals(PNG)) begin.setNext(new ImageProcess());
        if (file.getContentType().equals(PDF)) begin.setNext(null);
        return begin.handle(file);
    }
}
