package com.henrique.nookio_api.modules.properties.services;

import com.henrique.nookio_api.modules.properties.dto.CatalogationParameters;
import com.henrique.nookio_api.modules.properties.dto.InputCatalog;
import com.henrique.nookio_api.modules.properties.dto.RegisterPropertyDto;
import com.henrique.nookio_api.modules.properties.interfaces.CatalogMapper;
import com.henrique.nookio_api.modules.properties.models.Property;
import com.henrique.nookio_api.modules.properties.models.VwPropertiesCatalog;
import com.henrique.nookio_api.modules.properties.repository.PropertiesRepository;
import com.henrique.nookio_api.modules.properties.repository.VwPropertiesCatalogRepository;
import com.henrique.nookio_api.modules.properties.repository.VwPropertiesCatalogSpecs;
import com.henrique.nookio_api.modules.properties.dto.UpdatePropertyDto;
import com.henrique.nookio_api.modules.properties.services.facade.CreatePropertyFacade;
import com.henrique.nookio_api.modules.properties.services.facade.UpdatePropertyOrchestror;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertiesService {

    private final VwPropertiesCatalogRepository vwPropertiesCatalogRepository;
    private final PropertiesRepository propertiesRepository;
    private final CreatePropertyFacade createFacade;
    private final UpdatePropertyOrchestror updateFacade;
    private final CatalogMapper catalogMapper;

    public Slice<VwPropertiesCatalog> getCatalog(InputCatalog input) {
        if (input == null) {
            return vwPropertiesCatalogRepository.findAllBy(null, VwPropertiesCatalogSpecs.pageable(null));
        }

        CatalogationParameters parameters = catalogMapper.toParameters(input);

        Specification<VwPropertiesCatalog> spec = VwPropertiesCatalogSpecs.filteredCatalog(parameters);

        Pageable pageable = (parameters.getInputPreSet() != null)
                ? parameters.getInputPreSet().pageable(
                        Sort.Order.desc("avaliation"),
                        Sort.Order.desc("totalSchedules")
                  )
                : VwPropertiesCatalogSpecs.pageable(null);

        return vwPropertiesCatalogRepository.findAllBy(spec, pageable);
    }

    public void createProperty(RegisterPropertyDto dto){
        createFacade.execute(dto);
    }

    public void updateProperty(UpdatePropertyDto dto){
        updateFacade.execute(dto);
    }

    @Transactional
    public void deleteProperty(Integer propertyId, Integer ownerId){
        Property property = propertiesRepository.findById(propertyId)
                .orElseThrow(() -> new IllegalArgumentException("Property not found."));

        if (!property.getOwnerId().equals(ownerId)) {
            throw new IllegalArgumentException("Only the property owner can do it!");
        }

        property.setActive(false);
        propertiesRepository.save(property);
    }
}
