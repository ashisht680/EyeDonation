package com.javinindia.ansheyedonate.apiparsing.blooddonatelistparsing;

/**
 * Created by Ashish on 12-04-2017.
 */

public class DonateListDetail {

    private String donnerId;
    private String uid;
    private String name;
    private String dob;
    private String gender;
    private String email;
    private String contactNumber;
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getDonnerId() {
        return donnerId;
    }

    public void setDonnerId(String donnerId) {
        this.donnerId = donnerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
