package com.example.authservice.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@SuperBuilder
@Entity(name="role")
@Getter
@Setter
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(name="name")
    private String name;
    @Column(name = "description")
    private String description;

    @ManyToMany(mappedBy = "roles")
    List<User> userList;

    public String getName() {
        return name;
    }

    public String getDescription(){
        return description;
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
}
