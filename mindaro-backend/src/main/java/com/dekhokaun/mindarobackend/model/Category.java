package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "d3_category")
@Getter
@Setter
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)", unique = true, nullable = false)
    private UUID id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String fbid;

    @Column(nullable = false)
    private Integer code;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer prede;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String icon;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String image;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String text;

    @Column(length = 5, nullable = false)
    private String language;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String action;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String link;

    @Column(nullable = false)
    private Integer orderx;

    @Column(nullable = false)
    private Integer status;

    @ManyToMany(mappedBy = "categories")
    private List<Mentor> mentors;

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
