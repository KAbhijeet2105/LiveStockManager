package com.sih.livestockmanager;

public class UserHelper {

    String uid,username,email,phoneno,isAdmin,area,aho_id,aadhar_no,gender;

    public UserHelper() {
    }

    //Constructor for Admin
//    public UserHelper(String username, String email, String phoneno, String isAdmin,String area,String aho_id)
//    {
//        this.username = username;
//        this.email = email;
//        this.phoneno = phoneno;
//        this.isAdmin = isAdmin;
//        this.area = area;
//        this.aho_id =aho_id;
//    }

    // Constructor for User

    public UserHelper(String uid,String username, String email, String phoneno, String isAdmin, String area, String aho_id, String aadhar_no, String gender) {
        this.uid = uid;
        this.username = username;
        this.email = email;
        this.phoneno = phoneno;
        this.isAdmin = isAdmin;
        this.area = area;
        this.aho_id = aho_id;
        this.aadhar_no = aadhar_no;
        this.gender = gender;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getAho_id() {
        return aho_id;
    }

    public void setAho_id(String aho_id) {
        this.aho_id = aho_id;
    }

    public String getAadhar_no() {
        return aadhar_no;
    }

    public void setAadhar_no(String aadhar_no) {
        this.aadhar_no = aadhar_no;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(String isAdmin) {
        this.isAdmin = isAdmin;
    }
}
