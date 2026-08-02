package com.henrique.nookio_api.modules.properties.services.create_chain;

import com.henrique.nookio_api.infraestructure.bucket.ports.BucketPort;
import com.henrique.nookio_api.modules.files.models.File;
import com.henrique.nookio_api.modules.location.models.Location;
import com.henrique.nookio_api.modules.location.repository.LocationRepository;
import com.henrique.nookio_api.modules.properties.dto.RegisterPropertyDto;
import com.henrique.nookio_api.modules.properties.dto.SnapshotProperty;
import com.henrique.nookio_api.modules.properties.interfaces.SnapshotMapper;
import com.henrique.nookio_api.modules.properties.models.Property;
import com.henrique.nookio_api.modules.properties.models.PropertyInformation;
import com.henrique.nookio_api.modules.properties.models.PropertyPhoto;
import com.henrique.nookio_api.modules.properties.repository.PropertiesRepository;
import com.henrique.nookio_api.modules.properties.repository.PropertyInformationRepository;
import com.henrique.nookio_api.modules.properties.repository.PropertyPhotoRepository;
import lombok.experimental.SuperBuilder;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(4)
@SuperBuilder
public class SetupNewPropertyDataStep extends CreatePropertyChain {

    private final LocationRepository locationRepository;
    private final PropertyInformationRepository propertyInformationRepository;
    private final PropertiesRepository propertiesRepository;
    private final PropertyPhotoRepository propertyPhotoRepository;
    private final SnapshotMapper snapshotMapper;
    private final BucketPort bucketPort;

    @Override
    public void step(RegisterPropertyDto dto, SnapshotProperty snapshot) {
        try {
            Location location = locationRepository.save(snapshotMapper.toLocation(snapshot));
            PropertyInformation information = propertyInformationRepository.save(snapshotMapper.toInformation(snapshot));

            Property property = snapshotMapper.toProperty(snapshot, information.getId(), location.getId());
            property = propertiesRepository.save(property);

            if (snapshot.getUploadedFiles() != null && !snapshot.getUploadedFiles().isEmpty()) {
                for (int i = 0; i < snapshot.getUploadedFiles().size(); i++) {
                    File file = snapshot.getUploadedFiles().get(i);
                    PropertyPhoto photo = PropertyPhoto.builder()
                            .propertyId(property.getId().longValue())
                            .file(file)
                            .photoOrder(i + 1)
                            .build();
                    propertyPhotoRepository.save(photo);
                }
            }

            information.setProperty(property.getId());
            propertyInformationRepository.save(information);

            snapshot.setSavedProperty(property);

        } catch (Exception e) {
            if (snapshot.getUploadedFiles() != null) {
                snapshot.getUploadedFiles().forEach(file -> {
                    try {
                        bucketPort.delete(file.getUrl());
                    } catch (Exception ignored) {}
                });
            }
            throw e;
        }
    }
}
