package com.henrique.nookio_api.modules.users.repositories;

import com.henrique.nookio_api.modules.users.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserRepository
        extends JpaRepository<User, Integer>, JpaSpecificationExecutor<User> {
}
