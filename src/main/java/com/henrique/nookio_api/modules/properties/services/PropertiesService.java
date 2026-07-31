package com.henrique.nookio_api.modules.properties.services;

import com.henrique.nookio_api.modules.properties.dto.InputGetCatalog;
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

    public Slice<VwPropertiesCatalog> getCatalog(InputGetCatalog input){
        Specification<VwPropertiesCatalog> spec = VwPropertiesCatalogSpecs
                .filteredCatalog(input);
        Pageable pageable = input
                .inputPreSet()
                .pageable(
                        Sort.Order.desc("avaliation"),
                        Sort.Order.desc("total_schedules")
                );
        return repository.findAllBy(
                spec,
                pageable
        );
    }
}
