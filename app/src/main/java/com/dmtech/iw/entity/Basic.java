/**
  * Copyright 2022 bejson.com 
  */
package com.dmtech.iw.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Auto-generated: 2022-09-24 16:53:17
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Entity
public class Basic {

    @Id
    private Long id;    //数据表中记录的ID

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String cid;
    private String location;
    private String parent_city;
    private String admin_area;
    private String cnty;
    private String lat;
    private String lon;
    private String tz;

    @Generated(hash = 486223695)
    public Basic(Long id, String cid, String location, String parent_city,
            String admin_area, String cnty, String lat, String lon, String tz) {
        this.id = id;
        this.cid = cid;
        this.location = location;
        this.parent_city = parent_city;
        this.admin_area = admin_area;
        this.cnty = cnty;
        this.lat = lat;
        this.lon = lon;
        this.tz = tz;
    }

    @Generated(hash = 414549035)
    public Basic() {
    }

    public void setCid(String cid) {
         this.cid = cid;
     }
     public String getCid() {
         return cid;
     }

    public void setLocation(String location) {
         this.location = location;
     }
     public String getLocation() {
         return location;
     }

    public void setParent_city(String parent_city) {
         this.parent_city = parent_city;
     }
     public String getParent_city() {
         return parent_city;
     }

    public void setAdmin_area(String admin_area) {
         this.admin_area = admin_area;
     }
     public String getAdmin_area() {
         return admin_area;
     }

    public void setCnty(String cnty) {
         this.cnty = cnty;
     }
     public String getCnty() {
         return cnty;
     }

    public void setLat(String lat) {
         this.lat = lat;
     }
     public String getLat() {
         return lat;
     }

    public void setLon(String lon) {
         this.lon = lon;
     }
     public String getLon() {
         return lon;
     }

    public void setTz(String tz) {
         this.tz = tz;
     }
     public String getTz() {
         return tz;
     }

}