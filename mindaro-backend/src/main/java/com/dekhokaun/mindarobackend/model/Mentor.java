package com.dekhokaun.mindarobackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a1_mentor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Mentor {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)", unique = true, nullable = false)
    private UUID id;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String mentorfbid;

    private Integer umid;
    private Integer gcode;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false, unique = true)
    private Long mobile;

    @Column(length = 5)
    private String country;

    @Column(length = 11, nullable = false)
    private String pwd;

    @Enumerated(EnumType.STRING)
    @Column(length = 1, nullable = false)
    private MentorStatus status;

    private Integer ulevel;

    @Enumerated(EnumType.STRING)
    @Column(length = 1, nullable = false)
    private MentorType utype;

    private Integer parentid;

    @Column(columnDefinition = "TEXT")
    private String firstname;

    @Column(columnDefinition = "TEXT")
    private String lastname;

    @Column(columnDefinition = "TEXT")
    private String nationality;

    @Column(columnDefinition = "TEXT")
    private String photo;

    @Column(length = 2)
    private String gender;

    private LocalDate dob;

    @Column(columnDefinition = "TEXT")
    private String mainlanguage;

    @Column(columnDefinition = "TEXT")
    private String address;

    @Column(columnDefinition = "TEXT")
    private String city;

    @Column(columnDefinition = "TEXT")
    private String state;

    private Integer pincode;

    private Integer totalexpyrs;
    private Integer timeweeklyhrs;

    @Column(columnDefinition = "TEXT")
    private String platforminfo;

    @Column(columnDefinition = "TEXT")
    private String qualification;

    @Column(columnDefinition = "TEXT")
    private String mainincome;

    @Column(columnDefinition = "TEXT")
    private String tw;

    @Column(columnDefinition = "TEXT")
    private String linkedin;

    @Column(columnDefinition = "TEXT")
    private String yt;

    @Column(columnDefinition = "TEXT")
    private String fb;

    @Column(nullable = false)
    private Double rating = 0.0;

    @Column(nullable = false)
    private Integer ratingCount = 0;

    private Integer rate;

    @OneToMany(mappedBy = "mentor", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings;

        @ManyToMany
        @JoinTable(
                name = "mentor_category",
                joinColumns = @JoinColumn(name = "mentor_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id")
        )
        private Set<Category> categories;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();

        if (utype == null) {
            utype = MentorType.BASIC;
        }

        if (status == null) {
            status = MentorStatus.PENDING_APPROVAL;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}