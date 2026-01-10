package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Category;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends BaseRepository<Category> {

    List<Category> findByStatus(Integer status);

//    Page<Category> findAll(Pageable pageable);

    Optional<Category> findByName(String name);

    void deleteByName(String name);
}
