package com.henrique.nookio_api.modules.properties.services.chains;

import com.henrique.nookio_api.modules.properties.dto.RegisterPropertyDto;
import com.henrique.nookio_api.modules.properties.dto.SnapshotProperty;
import com.henrique.nookio_api.shared.chains.BaseChain2;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@NoArgsConstructor
@SuperBuilder
public abstract class CreatePropertyChain extends BaseChain2<RegisterPropertyDto, SnapshotProperty> {
}
