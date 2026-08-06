package com.henrique.nookio_api.modules.properties.services.strategies;

import com.henrique.nookio_api.modules.properties.dto.UpdatePropertyDto;
import com.henrique.nookio_api.modules.properties.models.Property;

public interface UpdatePropertyStrategy {

    void update(Property property, UpdatePropertyDto dto);
}
