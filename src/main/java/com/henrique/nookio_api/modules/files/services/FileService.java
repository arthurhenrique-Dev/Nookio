package com.henrique.nookio_api.modules.files.services;

import com.henrique.nookio_api.infraestructure.bucket.ports.BucketPort;
import com.henrique.nookio_api.modules.files.models.File;
import com.henrique.nookio_api.modules.files.repository.FileRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository repository;
    private final BucketPort bucketPort;

    @Transactional
    public List<File> upload(List<MultipartFile> files){
        List<File> result = new ArrayList<>(files.size());
        List<String> uploadedFiles = new ArrayList<>(files.size());
        try {
            for (MultipartFile file : files) {
                File uploaded = bucketPort.upload(file)
                        .orElseThrow(RuntimeException::new);
                uploadedFiles.add(uploaded.getUrl());
                File saved = repository.saveAndFlush(uploaded);
                result.add(saved);
            }
            return result;
        } catch (Exception e) {
            uploadedFiles.forEach(bucketPort::delete);
            throw new RuntimeException(e);
        }
    }
}
