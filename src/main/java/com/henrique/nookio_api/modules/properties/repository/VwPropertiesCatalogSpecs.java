package com.henrique.nookio_api.modules.properties.repository;

import com.henrique.nookio_api.modules.properties.dto.InputGetCatalog;
import com.henrique.nookio_api.modules.properties.models.Info;
import com.henrique.nookio_api.modules.properties.models.VwPropertiesCatalog;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class VwPropertiesCatalogSpecs {

    public static Specification<VwPropertiesCatalog> filteredCatalog(InputGetCatalog filter) {
        return (root, query, builder) -> {
            if (filter == null) {
                return builder.conjunction();
            }

            List<Predicate> predicates = new ArrayList<>();

            // 1. Global Multi-Field Search (LIKE %search%)
            if (filter.search() != null && !filter.search().isBlank()) {
                String pattern = "%" + filter.search().trim().toLowerCase() + "%";
                Predicate searchPredicate = builder.or(
                        builder.like(builder.lower(root.get("ownerName")), pattern),
                        builder.like(builder.lower(root.get("location").get("city")), pattern),
                        builder.like(builder.lower(root.get("location").get("neighborhood")), pattern),
                        builder.like(builder.lower(root.get("location").get("state")), pattern)
                );
                predicates.add(searchPredicate);
            }

            // 2. Info Filters
            Info info = filter.info();
            if (info != null) {
                if (info.getPropertyType() != null) {
                    predicates.add(builder.equal(root.get("information").get("info").get("propertyType"), info.getPropertyType()));
                }
                if (info.getBedrooms() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("information").get("info").get("bedrooms"), info.getBedrooms()));
                }
                if (info.getBathrooms() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("information").get("info").get("bathrooms"), info.getBathrooms()));
                }
                if (info.getBeds() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("information").get("info").get("beds"), info.getBeds()));
                }
                if (info.getMaxGuests() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("information").get("info").get("maxGuests"), info.getMaxGuests()));
                }
                if (info.getParkingSpaces() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("information").get("info").get("parkingSpaces"), info.getParkingSpaces()));
                }
                if (info.getAreaSqm() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("information").get("info").get("areaSqm"), info.getAreaSqm()));
                }
                if (info.getPools() != null) {
                    predicates.add(builder.greaterThanOrEqualTo(root.get("information").get("info").get("pools"), info.getPools()));
                }
                if (info.isNextToBeach()) {
                    predicates.add(builder.isTrue(root.get("information").get("info").get("nextToBeach")));
                }
                if (info.isPetFriendly()) {
                    predicates.add(builder.isTrue(root.get("information").get("info").get("petFriendly")));
                }
                if (info.isHasWifi()) {
                    predicates.add(builder.isTrue(root.get("information").get("info").get("hasWifi")));
                }
                if (info.isHasAirConditioning()) {
                    predicates.add(builder.isTrue(root.get("information").get("info").get("hasAirConditioning")));
                }
                if (info.getFavorableSeason() != null) {
                    predicates.add(builder.equal(root.get("information").get("info").get("favorableSeason"), info.getFavorableSeason()));
                }
                if (info.getPrice() != null) {
                    predicates.add(builder.lessThanOrEqualTo(root.get("information").get("info").get("price"), info.getPrice()));
                }
                if (info.getCleaningFee() != null) {
                    predicates.add(builder.lessThanOrEqualTo(root.get("information").get("info").get("cleaningFee"), info.getCleaningFee()));
                }
            }

            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
