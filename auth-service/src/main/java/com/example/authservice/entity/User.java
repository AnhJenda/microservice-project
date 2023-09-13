package com.example.authservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user")
@NoArgsConstructor
@Getter
@Setter
@SuperBuilder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String username;
    private String password;
    private String email;
    private String tel;
    private boolean isVerified = false;
//    private String[] roles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(  name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    public User(String username, String email, String hashedPassword) {
        this.username = username;
        this.email = email;
        this.password = hashedPassword;
    }


    @Column(name = "inserted_time", nullable = true)
    private Date insertedTime;
    @Column(name = "updated_time", nullable = true)
    private Date updatedTime;
    @Column(name = "inserted_by", nullable = true)
    private String insertedBy;
    @Column(name = "updated_by", nullable = true)
    private String updatedBy;

    @PrePersist
    private  void beforeInsert() {
        this.insertedTime = new Date();
    }

    @PreUpdate
    private  void beforeUpdate() {
        this.updatedTime = new Date();
    }

    public void setEmailVerified(boolean b) {
        this.isVerified = b;
    }

    public boolean isEmailVerified() {
        return isVerified;
    }
}
