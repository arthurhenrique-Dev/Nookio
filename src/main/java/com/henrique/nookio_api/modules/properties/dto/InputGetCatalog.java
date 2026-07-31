package com.henrique.nookio_api.modules.properties.dto;

import com.henrique.nookio_api.modules.location.dto.LocationInput;
import com.henrique.nookio_api.modules.properties.models.Info;
import com.henrique.nookio_api.shared.input.InputPreSet;

public record InputGetCatalog(
        InputPreSet inputPreSet,
        String search,
        Info info,
        LocationInput locationInput
) {
}
