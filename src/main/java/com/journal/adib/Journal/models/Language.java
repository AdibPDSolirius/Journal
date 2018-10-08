package com.journal.adib.Journal.Models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.journal.adib.Journal.Models.Resource;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="languages")
public class Language extends Technology{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "language_id")
    private Long id;

    @NotBlank
    @Size(max = 30)
    @Column(name="language_name")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Resource> getResources() {
        return resources;
    }

    public void setResources(Set<Resource> resources) {
        this.resources = resources;
    }

    @ManyToMany(mappedBy = "languages")
    @JsonIgnore
    private Set<Resource> resources = new HashSet<>();

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
