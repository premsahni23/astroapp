package com.dekhokaun.mindarobackend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;
import java.util.UUID;

@NoRepositoryBean
public interface BaseRepository<T> extends JpaRepository<T, UUID> {
    Optional<T> findById(UUID id);
}
