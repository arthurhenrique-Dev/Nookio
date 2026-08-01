package com.henrique.nookio_api.modules.properties.repository;

import com.henrique.nookio_api.modules.properties.models.Property;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PropertiesRepository extends JpaRepository<Property, Integer> {
}
