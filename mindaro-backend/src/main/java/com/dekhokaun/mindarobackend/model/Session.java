package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "x1_session")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)", unique = true, nullable = false)
    private UUID id;

    @Column(columnDefinition = "TEXT")
    private String fbid;

    @Column(columnDefinition = "BINARY(16)", nullable = false)
    private UUID userid;

    @Column(length = 100)
    private String ip;

    @Column(length = 200)
    private String browser;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime datetime;

    @Column(length = 250, nullable = false)
    private String gps;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime logintime;

    @Column
    private LocalDateTime logouttime;

    @Column(length = 25)
    private String logintype;

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
