package com.matrix.microservice.admin.entity.szy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.matrix.microservice.admin.entity.BaseEntity;

import java.util.Date;
import javax.persistence.*;

@Table(name = "szy_business")
public class SzyBusiness  extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String name;

    private String child;

    private String ver;

    private String branch;
    private String teller;

    private String cruser;

    private String crtime;

    private String zruser;
    private String zrtime;

    private String touser;

    private String totime;

    private String checkuser;

    private String checktime;

    private String status;

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
     * @return branch
     */
    public String getBranch() {
        return branch;
    }

    /**
     * @param branch
     */
    public void setBranch(String branch) {
        this.branch = branch;
    }
    /**
     * @return teller
     */
    public String getTeller() {
        return teller;
    }

    /**
     * @param teller
     */
    public void setTeller(String teller) {
        this.teller = teller;
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

    /**
     * @return zruser
     */
    public String getZruser() {
        return zruser;
    }

    /**
     * @param zruser
     */
    public void setZruser(String zruser) {
        this.zruser = zruser;
    }

    /**
     * @return zrtime
     */
    public String getZrtime() {
        return zrtime;
    }

    /**
     * @param zrtime
     */
    public void setZrtime(String zrtime) {
        this.zrtime = zrtime.replace(".0","");
    }

    /**
     * @return touser
     */
    public String getTouser() {
        return touser;
    }

    /**
     * @param touser
     */
    public void setTouser(String touser) {
        this.touser = touser;
    }

    /**
     * @return totime
     */
    public String getTotime() {
        return totime;
    }

    /**
     * @param totime
     */
    public void setTotime(String totime) {
        this.totime = totime.replace(".0","");
    }

    /**
     * @return checkuser
     */
    public String getCheckuser() {
        return checkuser;
    }

    /**
     * @param checkuser
     */
    public void setCheckuser(String checkuser) {
        this.checkuser = checkuser;
    }

    /**
     * @return checktime
     */
    public String getChecktime() {
        return checktime;
    }

    /**
     * @param checktime
     */
    public void setChecktime(String checktime) {
        this.checktime = checktime.replace(".0","");
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
        return "SzyBusiness{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", child='" + child + '\'' +
                ", ver='" + ver + '\'' +
                ", branch='" + branch + '\'' +
                ", teller='" + teller + '\'' +
                ", cruser='" + cruser + '\'' +
                ", crtime='" + crtime + '\'' +
                ", zruser='" + zruser + '\'' +
                ", zrtime='" + zrtime + '\'' +
                ", touser='" + touser + '\'' +
                ", totime='" + totime + '\'' +
                ", checkuser='" + checkuser + '\'' +
                ", checktime='" + checktime + '\'' +
                ", status='" + status + '\'' +
                ", bak='" + bak + '\'' +
                '}';
    }
}