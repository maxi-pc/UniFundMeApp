package com.example.maxi.unifundme;

/**
 * Created by Maxi on 2018-03-21.
 */

public class User {

    private Integer id;

    private String username;
    private String emailAddress;
    private String password;
    // profile stuff
    private Integer profileSet;
    private String study;
    private String locality;
    private String aboriginality;
    private String province;
    private String school;
    private Double gpa;

    public User(Integer id, String username, String emailAddress, String password, Integer profileSet, String study, String locality, String aboriginality, String province, String school, Double gpa) {
        this.id = id;
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
        this.profileSet = profileSet;
        this.study = study;
        this.locality = locality;
        this.aboriginality = aboriginality;
        this.province = province;
        this.school = school;
        this.gpa = gpa;
    }

    /*
    public User(Integer id, String username, String emailAddress, String password, Integer profileSet, String study, String locality, String aboriginality, String provinceInt, String schoolInt, Double gpa) {
        this.id = id;
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
        this.profileSet = profileSet;
        this.study = study;
        this.locality = locality;
        this.aboriginality = aboriginality;
        this.province = provinceInt;
        this.school = schoolInt;
        this.gpa = gpa;
    }
    */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User(String username, String emailAddress, String password) {
        this.username = username;
        this.emailAddress = emailAddress;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getProfileSet() {
        return profileSet;
    }

    public void setProfileSet(Integer profileSet) {
        this.profileSet = profileSet;
    }

    public String getStudy() {
        return study;
    }

    public void setStudy(String study) {
        this.study = study;
    }

    public String getLocality() {
        return locality;
    }

    public void setLocality(String locality) {
        this.locality = locality;
    }

    public String getAboriginality() {
        return aboriginality;
    }

    public void setAboriginality(String aboriginality) {
        this.aboriginality = aboriginality;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public Double getGpa() {
        return gpa;
    }

    public void setGpa(Double gpa) {
        this.gpa = gpa;
    }

}
