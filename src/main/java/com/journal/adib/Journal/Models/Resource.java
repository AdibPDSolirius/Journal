package com.journal.adib.Journal.Models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.crypto.Data;
import java.awt.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="resources")
public class Resource {

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(Set<Language> languages) {
        this.languages = languages;
    }

    public Set<Framework> getFrameworks() {
        return frameworks;
    }

    public void setFrameworks(Set<Framework> frameworks) {
        this.frameworks = frameworks;
    }

    public Set<Database> getDatabases() {
        return databases;
    }

    public void setDatabases(Set<Database> databases) {
        this.databases = databases;
    }

    public Set<Library> getLibraries() {
        return libraries;
    }

    public void setLibraries(Set<Library> libraries) {
        this.libraries = libraries;
    }

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "resource_id")
    private Long id;

    @NotBlank
    @Size(max = 30)
    @Column(name="resource_name")
    private String name;

    @Column(name="resource_url")
    private String url;


    @ManyToMany()
    @JoinTable(name = "resource_language",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "language_id")
    )
    private Set<Language> languages = new HashSet<>();


    @ManyToMany()
    @JoinTable(name = "resource_framework",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "framework_id")
    )
    private Set<Framework> frameworks = new HashSet<>();

    @ManyToMany()
    @JoinTable(name = "resource_database",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "database_id")
    )
    private Set<Database> databases = new HashSet<>();

    @ManyToMany()
    @JoinTable(name = "library_framework",
            joinColumns = @JoinColumn(name = "resource_id"),
            inverseJoinColumns = @JoinColumn(name = "library_id")
    )
    private Set<Library> libraries = new HashSet<>();








}
