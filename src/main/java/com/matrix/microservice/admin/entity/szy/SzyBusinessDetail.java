package com.matrix.microservice.admin.entity.szy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matrix.microservice.admin.entity.BaseEntity;

import javax.persistence.*;

@Table(name = "szy_business_detail")
public class SzyBusinessDetail extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;
    private String link_id;

    private String user;

    private String time;

    private String status;

    private String deal;

    private String bak;
    
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
    public String getLink_id() {
        return link_id;
    }

    /**
     * @param id
     */
    public void setLink_id(String link_id) {
        this.link_id = link_id;
    }

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
     * @return user
     */
    public String getUser() {
        return user;
    }

    /**
     * @param user
     */
    public void setUser(String user) {
        this.user = user;
    }

    /**
     * @return time
     */
    public String getTime() {
        return time;
    }

    /**
     * @param time
     */
    public void setTime(String time) {
        this.time = time.replace(".0","");;
    }

    /**
     * @return status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
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
     * @return bak
     */
    public String getBak() {
        return bak;
    }

    /**
     * @param bak
     */
    public void setBak(String bak) {
        this.bak = bak;
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
        return "SzyBusinessDetail{" +
                "id='" + id + '\'' +
                ", user='" + user + '\'' +
                ", time='" + time + '\'' +
                ", deal='" + deal + '\'' +
                ", status='" + status + '\'' +
                ", bak='" + bak + '\'' +
                '}';
    }
}