package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Banner;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BannerRepository extends BaseRepository<Banner> {
    List<Banner> findByIsActiveTrueOrderByCreatedAtDesc();
}
