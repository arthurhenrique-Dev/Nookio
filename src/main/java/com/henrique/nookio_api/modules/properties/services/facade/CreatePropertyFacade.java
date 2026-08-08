package com.henrique.nookio_api.modules.properties.services.facade;

import com.henrique.nookio_api.modules.properties.dto.RegisterPropertyDto;
import com.henrique.nookio_api.modules.properties.dto.SnapshotProperty;
import com.henrique.nookio_api.modules.properties.models.Property;
import com.henrique.nookio_api.modules.properties.services.chains.CreatePropertyChain;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CreatePropertyFacade {

    private final List<CreatePropertyChain> chains;

    @Transactional
    public Property execute(RegisterPropertyDto dto) {
        SnapshotProperty snapshot = new SnapshotProperty();
        for (CreatePropertyChain chain : chains) {
            chain.handle(dto, snapshot);
        }
        return snapshot.getSavedProperty();
    }
}
