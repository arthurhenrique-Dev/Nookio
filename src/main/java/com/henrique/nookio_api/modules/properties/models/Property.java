package com.henrique.nookio_api.modules.properties.models;

import com.henrique.nookio_api.modules.files.models.Photos;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@Entity
@Table(name = "properties", schema = "property")
public class Property {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "information_id")
    private PropertyInformation informationId;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "owner_id")
//    private User owner;
//
//    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
//    @JoinTable(
//            schema = "files",
//            name = "photos_property",
//            joinColumns = @JoinColumn(name = "property_id"),
//            inverseJoinColumns = @JoinColumn(name = "photo_id")
//    )
//    private List<Photos> photos;
//
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, optional = false)
//    @JoinTable(
//            schema = "geo",
//            name = "address_property",
//            joinColumns = @JoinColumn(name = "property_id"),
//            inverseJoinColumns = @JoinColumn(name = "address_id")
//    )
//    private Address address;
//
//    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JoinTable(
//            name = "property_avaliations",
//            joinColumns = @JoinColumn(name = "property_id"),
//            inverseJoinColumns = @JoinColumn(name = "avaliation_is")
//    )
//    private Avaliation avaliation;
}
