package com.journal.adib.Journal;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name="resources")
public class Resource {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "resource_id")
    private Long id;

    @NotBlank
    @Size(max = 30)
    @Column(name="resource_name")
    private String resourceName;

    @Column(name="resource_url")
    private String resourceURL;


    public String getResourceName() {
        return resourceName;
    }

    public String getResourceURL() {
        return resourceURL;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public void setResourceURL(String resourceURL) {
        this.resourceURL = resourceURL;
    }

}
