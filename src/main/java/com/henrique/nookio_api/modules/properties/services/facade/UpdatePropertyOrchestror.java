package com.henrique.nookio_api.modules.properties.services.facade;

import com.henrique.nookio_api.modules.properties.dto.UpdatePropertyDto;
import com.henrique.nookio_api.modules.properties.models.Property;
import com.henrique.nookio_api.modules.properties.repository.PropertiesRepository;
import com.henrique.nookio_api.modules.properties.services.strategies.UpdatePropertyStrategy;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdatePropertyOrchestror {

    private final PropertiesRepository propertiesRepository;
    private final List<UpdatePropertyStrategy> updateStrategies;

    @Transactional
    public void execute(UpdatePropertyDto dto) {
        Property property = propertiesRepository.findById(dto.propertyId())
                .orElseThrow(() -> new IllegalArgumentException("Imóvel não encontrado."));

        if (dto.title() != null && !dto.title().isBlank()) {
            property.setTitle(dto.title());
            propertiesRepository.save(property);
        }
        for (UpdatePropertyStrategy strategy : updateStrategies) {
            strategy.update(property, dto);
        }
    }
}

