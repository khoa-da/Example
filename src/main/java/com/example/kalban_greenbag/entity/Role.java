package com.example.kalban_greenbag.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Roles")
public class Role extends BaseEntity{


    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "RoleName", nullable = false, length = 50)
    private String roleName;

    @Nationalized
    @Lob
    @Column(name = "Description")
    private String description;

    @JsonIgnore
    @OneToMany(mappedBy = "role")
    private Set<User> users = new LinkedHashSet<>();

}