package com.henrique.nookio_api.modules.properties.services.create_chain;

import com.henrique.nookio_api.modules.properties.dto.RegisterPropertyDto;
import com.henrique.nookio_api.modules.properties.dto.SnapshotProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public abstract class CreatePropertyChain {

    protected CreatePropertyChain next;

    public void handle(RegisterPropertyDto dto, SnapshotProperty snapshot){
        step(dto, snapshot);
        if (getNext() != null) next.handle(dto, snapshot);
    }
    public abstract void step(RegisterPropertyDto dto, SnapshotProperty snapshot);

}
