package com.dekhokaun.mindarobackend.repository;

import com.dekhokaun.mindarobackend.model.Otp;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OtpRepository extends BaseRepository<Otp> {

    // mobile == email
    Optional<Otp> findByUseridAndMobileAndOtpTypeAndStatus(UUID userid, String mobile, String otpType, String status);

    void deleteByUseridAndMobileAndOtpType(UUID userid, String mobile, String otpType);
}
    