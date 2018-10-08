package com.journal.adib.Journal.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="databases")
public class Database extends Technology {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "database_id")
    private Long id;

    @NotBlank
    @Size(max = 30)
    @Column(name="database_name")
    private String name;

    @ManyToMany(mappedBy = "databases")
    @JsonIgnore
    private Set<Resource> resources = new HashSet<>();

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String languageName) {
        this.name = languageName;
    }

    public String toString(){
        return this.getName();
    }
}