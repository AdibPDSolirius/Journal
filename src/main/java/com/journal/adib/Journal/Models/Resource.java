package com.journal.adib.Journal.Models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="resources")
public class Resource {

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "resource_id")
    private Long id;

    @NotBlank
    @Size(max = 30)
    @Column(name="resource_name")
    private String resourceName;

    @Column(name="resource_url")
    private String resourceURL;


    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public Set<Language> getLanguages() {
        return languages;
    }

    @ManyToMany()
    @JoinTable(name = "resource_language",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<Language> languages = new HashSet<>();

    public Long getId() {
        return id;
    }

    public String getResourceName() {
        return resourceName;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public Long getResourceId() { return id; }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }

    public void setResourceId(Long id) { this.id = id; }

    public void setId(Long id) {
        this.id = id;
    }

    public String toString(){
        return this.getResourceName();
    }





}
