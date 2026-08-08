package com.henrique.nookio_api.modules.properties.services.orchestror;

import com.henrique.nookio_api.modules.properties.dto.UpdatePropertyDto;
import com.henrique.nookio_api.modules.properties.models.Property;
import com.henrique.nookio_api.modules.properties.repository.PropertiesRepository;
import com.henrique.nookio_api.modules.properties.services.update.UpdatePropertyPhotosStrategy;
import com.henrique.nookio_api.modules.properties.services.update.UpdatePropertyInformationStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UpdatePropertyOrchestror {

    private final PropertiesRepository propertiesRepository;
    private final UpdatePropertyInformationStrategy informationStrategy;
    private final UpdatePropertyPhotosStrategy photosStrategy;

    @Transactional
    public void execute(UpdatePropertyDto dto) {
        Property property = propertiesRepository.findById(dto.propertyId())
                .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado."));

        if (dto.title() != null && !dto.title().isBlank()) {
            property.setTitle(dto.title());
            propertiesRepository.save(property);
        }
        if (dto.informationUpdate() != null) informationStrategy.update(property, dto.informationUpdate());
    }
}


