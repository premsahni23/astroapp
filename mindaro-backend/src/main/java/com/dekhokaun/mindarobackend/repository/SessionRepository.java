package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Session;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SessionRepository extends BaseRepository<Session> {

    List<Session> findByUserid(UUID userid);

//    Page<Session> findAll(Pageable pageable);
}