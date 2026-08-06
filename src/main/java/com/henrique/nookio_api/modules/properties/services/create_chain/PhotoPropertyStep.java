package com.henrique.nookio_api.modules.properties.services.create_chain;

import com.henrique.nookio_api.modules.files.services.FileService;
import com.henrique.nookio_api.modules.properties.dto.RegisterPropertyDto;
import com.henrique.nookio_api.modules.properties.dto.SnapshotProperty;

import lombok.experimental.SuperBuilder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(3)
@SuperBuilder
public class PhotoPropertyStep extends CreatePropertyChain {

    private final FileService fileService;

    @Override
    public void step(RegisterPropertyDto dto, SnapshotProperty snapshot) {
        if (dto.photos() != null && !dto.photos().isEmpty()) {
            var uploadedFiles = fileService.upload(dto.photos());
            snapshot.setUploadedFiles(uploadedFiles);
        }
        snapshot.setPhotos(dto.photos());
    }
}
