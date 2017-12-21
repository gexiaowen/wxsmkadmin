package com.matrix.microservice.admin.entity.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matrix.microservice.admin.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "szy_question")
public class SzyQuestion  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String question;

    private String service;

    private String child;

    private String deal;

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
     * @return question
     */
    public String getQuestion() {
        return question;
    }

    /**
     * @param question
     */
    public void setQuestion(String question) {
        this.question = question;
    }

    /**
     * @return service
     */
    public String getService() {
        return service;
    }

    /**
     * @param service
     */
    public void setService(String service) {
        this.service = service;
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
     * @return deal
     */
    public String getDeal() {
        return deal;
    }

    /**
     * @param deal
     */
    public void setDeal(String deal) {
        this.deal = deal;
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
        return "SzyQuestion{" +
                "id='" + id + '\'' +
                ", question='" + question + '\'' +
                ", service='" + service + '\'' +
                ", child='" + child + '\'' +
                ", deal='" + deal + '\'' +
                ", cruser='" + cruser + '\'' +
                ", crtime='" + crtime + '\'' +
                '}';
    }
}