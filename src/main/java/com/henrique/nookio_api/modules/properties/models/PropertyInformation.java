package com.henrique.nookio_api.modules.properties.models;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Entity
@Table(name = "properties", schema = "property")
public class PropertyInformation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "property_id")
    private Property property;

    @Embedded
    private Info info;
}
