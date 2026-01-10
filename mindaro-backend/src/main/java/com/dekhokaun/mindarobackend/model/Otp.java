package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "x2_otp")
@Getter
@Setter
public class Otp {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)", unique = true, nullable = false)
    private UUID id;

    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID userid;

    @Column(length = 11, nullable = false)
    private String otpType;

    @Column(length = 20, nullable = false)
    private String countryCodeMobile;

    @Column(length = 20, nullable = false)
    private String mobile;

    @Column(length = 20, nullable = false)
    private String otp;

    @Column(length = 100, nullable = false)
    private String ip;

    @Column(length = 50, nullable = false)
    private String smsCounter;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String smsResponse;

    @Column(length = 11, nullable = false)
    private String smsapi;

    @Column(length = 20, nullable = false)
    private String status;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
