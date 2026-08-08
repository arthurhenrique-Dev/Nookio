package com.henrique.nookio_api.modules.avaliations.repository;

import com.henrique.nookio_api.modules.avaliations.models.Avaliation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AvaliationRepository extends JpaRepository<Avaliation, Long> {
    List<Avaliation> findAllByPropertyId(Long propertyId);
}
