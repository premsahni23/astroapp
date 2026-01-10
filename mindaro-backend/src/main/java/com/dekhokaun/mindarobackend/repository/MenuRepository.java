package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuRepository extends BaseRepository<Menu> {

    List<Menu> findByStatus(Integer status);

//    Page<Menu> findAll(Pageable pageable);

    Optional<Menu> findByText(String text);

    void deleteByText(String text);
}
