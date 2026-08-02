package com.henrique.nookio_api.modules.properties.services;

import com.henrique.nookio_api.modules.properties.dto.CatalogationParameters;
import com.henrique.nookio_api.modules.properties.dto.InputCatalog;
import com.henrique.nookio_api.modules.properties.interfaces.CatalogMapper;
import com.henrique.nookio_api.modules.properties.models.VwPropertiesCatalog;
import com.henrique.nookio_api.modules.properties.repository.VwPropertiesCatalogRepository;
import com.henrique.nookio_api.modules.properties.repository.VwPropertiesCatalogSpecs;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PropertiesService {

    private final VwPropertiesCatalogRepository repository;
    private final CatalogMapper catalogMapper;

    public Slice<VwPropertiesCatalog> getCatalog(InputCatalog input) {
        if (input == null) {
            return repository.findAllBy(null, VwPropertiesCatalogSpecs.pageable(null));
        }

        CatalogationParameters parameters = catalogMapper.toParameters(input);

        Specification<VwPropertiesCatalog> spec = VwPropertiesCatalogSpecs.filteredCatalog(parameters);

        Pageable pageable = (parameters.getInputPreSet() != null)
                ? parameters.getInputPreSet().pageable(
                        Sort.Order.desc("avaliation"),
                        Sort.Order.desc("totalSchedules")
                  )
                : VwPropertiesCatalogSpecs.pageable(null);

        return repository.findAllBy(spec, pageable);
    }

    public void createProperty(){}

    public void proceedProperty(){}
}
