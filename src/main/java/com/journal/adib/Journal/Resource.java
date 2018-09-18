package com.journal.adib.Journal;

import javax.persistence.Column;
import javax.persistence.Table;

@Table(name="resources")
public class Resource {

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
