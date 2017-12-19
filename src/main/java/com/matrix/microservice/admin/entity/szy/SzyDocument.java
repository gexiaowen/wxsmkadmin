package com.matrix.microservice.admin.entity.szy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matrix.microservice.admin.entity.BaseEntity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "szy_document")
public class SzyDocument extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;

    private String child;

    private String ver;

    private String size;

    private String cruser;

    private String crtime;

    @Transient
    @JsonIgnore
    private String stime;

    @Transient
    @JsonIgnore
    private String etime;

    @Transient
    @JsonIgnore
    private String sort = "";

    @Transient
    @JsonIgnore
    private String order = "";

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return child
     */
    public String getChild() {
        return child;
    }

    /**
     * @param child
     */
    public void setChild(String child) {
        this.child = child;
    }

    /**
     * @return ver
     */
    public String getVer() {
        return ver;
    }

    /**
     * @param ver
     */
    public void setVer(String ver) {
        this.ver = ver;
    }

    /**
     * @return size
     */
    public String getSize() {
        return size;
    }

    /**
     * @param size
     */
    public void setSize(String size) {
        this.size = size;
    }

    /**
     * @return cruser
     */
    public String getCruser() {
        return cruser;
    }

    /**
     * @param cruser
     */
    public void setCruser(String cruser) {
        this.cruser = cruser;
    }

    /**
     * @return crtime
     */
    public String getCrtime() {
        return crtime;
    }

    /**
     * @param crtime
     */
    public void setCrtime(String crtime) {
        this.crtime = crtime.replace(".0","");
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }

    public String getOrder() {
        return order;
    }

    public void setOrder(String order) {
        this.order = order;
    }

    public String getStime() {
        return stime;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public String getEtime() {
        return etime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    @Override
    public String toString() {
        return "SzyDocument{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", child='" + child + '\'' +
                ", ver='" + ver + '\'' +
                ", size='" + size + '\'' +
                ", cruser='" + cruser + '\'' +
                ", crtime='" + crtime + '\'' +
                '}';
    }

}