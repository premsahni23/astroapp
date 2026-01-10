package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Category;
import com.dekhokaun.mindarobackend.model.Mentor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;

@Repository
public interface MentorRepository extends BaseRepository<Mentor> {
    Optional<Mentor> findByName(String name);
    Optional<Mentor> findByUmid(Integer umid);
//    Page<Mentor> findByCategoriesContains(Category category, Pageable pageable);
    Page<Mentor> findByCategoriesContainingAndMainlanguage(Set<Category> categories, String mainlanguage, Pageable pageable);
}
