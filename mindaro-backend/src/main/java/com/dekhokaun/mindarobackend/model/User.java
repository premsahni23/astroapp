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

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "a1_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.BINARY)
    @Column(columnDefinition = "BINARY(16)", unique = true, nullable = false)
    private UUID id;

    private Integer umid;

    private Integer gcode;

    @Column(columnDefinition = "TEXT")
    private String userfbid;

    @Column(columnDefinition = "TEXT")
    private String mentorfbid;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String name;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private Long mobile;

    private String country;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean isProfileCompleted = false;

    private String pwd;

    @Column(length = 11)
    private String status;

    private Integer ulevel;

    @Enumerated(EnumType.STRING)
    @Column(length = 1, nullable = false)
    private UserType utype;

    private Integer parentid;

    @Column(columnDefinition = "TEXT")
    private String token;

    @Column(precision = 5, scale = 2)
    private BigDecimal rating;

    private Integer ratingcount;

    @Column(columnDefinition = "TEXT")
    private String howtoknow;

    @ManyToOne
    @JoinColumn(name = "mentor_id")
    private Mentor mentor;

    @OneToMany(mappedBy = "user")
    private List<Rating> ratings;

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;

        if (utype == null) {
            utype = UserType.CUSTOMER;
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
