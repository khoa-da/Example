package com.example.kalban_greenbag.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Nationalized;
import org.springframework.data.redis.core.RedisHash;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "Categories")
public class Category extends BaseEntity{


    @Size(max = 50)
    @NotNull
    @Nationalized
    @Column(name = "CategoryName", nullable = false, length = 50)
    private String categoryName;

    @Nationalized
    @Lob
    @Column(name = "Description")
    private String description;

    @OneToMany(mappedBy = "category", fetch= FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<BaseModel> baseModels = new LinkedHashSet<>();

}