package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.User;
import com.dekhokaun.mindarobackend.model.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends BaseRepository<User> {

//    Page<User> findAll(Pageable pageable);

    List<User> findByStatus(String status);

    @Query("SELECT u FROM User u WHERE u.name LIKE %:name%")
    List<User> searchByName(@Param("name") String name);

    @Query("SELECT u FROM User u WHERE u.id IN :ids")
    List<User> findByIds(@Param("ids") List<Integer> ids);

    @Query("""
            SELECT u FROM User u
            WHERE
              (:q IS NULL OR :q = '' OR
                 lower(u.name) LIKE lower(concat('%', :q, '%')) OR
                 lower(u.email) LIKE lower(concat('%', :q, '%')) OR
                 cast(u.mobile as string) LIKE concat('%', :q, '%')
              )
              AND (:utype IS NULL OR u.utype = :utype)
              AND (:statusUpper IS NULL OR upper(coalesce(u.status, '')) = :statusUpper)
            """)
    Page<User> adminSearchUsers(
            @Param("q") String q,
            @Param("utype") UserType utype,
            @Param("statusUpper") String statusUpper,
            Pageable pageable
    );

    boolean existsByEmail(String email);

    Optional<User> findByEmail(String email);
}
