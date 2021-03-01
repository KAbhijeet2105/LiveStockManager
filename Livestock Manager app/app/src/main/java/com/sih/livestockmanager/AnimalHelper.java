package com.sih.livestockmanager;

public class AnimalHelper {

    String ownerid,ownernm,ownerphn,animalId,category,breed,gender,weight,dob,age,use,regionId,isAlive,req_approve;

    public AnimalHelper() {


    }

    public AnimalHelper(String ownerid, String ownernm, String ownerphn, String animalId, String category, String breed, String gender, String weight, String dob, String age, String use, String regionId, String isAlive, String req_approve) {
        this.ownerid = ownerid;
        this.ownernm = ownernm;
        this.ownerphn = ownerphn;
        this.animalId = animalId;
        this.category = category;
        this.breed = breed;
        this.gender = gender;
        this.weight = weight;
        this.dob = dob;
        this.age = age;
        this.use = use;
        this.regionId = regionId;
        this.isAlive = isAlive;
        this.req_approve = req_approve;
    }

    public String getOwnerid() {
        return ownerid;
    }

    public void setOwnerid(String ownerid) {
        this.ownerid = ownerid;
    }

    public String getOwnernm() {
        return ownernm;
    }

    public void setOwnernm(String ownernm) {
        this.ownernm = ownernm;
    }

    public String getOwnerphn() {
        return ownerphn;
    }

    public void setOwnerphn(String ownerphn) {
        this.ownerphn = ownerphn;
    }

    public String getAnimalId() {
        return animalId;
    }

    public void setAnimalId(String animalId) {
        this.animalId = animalId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUse() {
        return use;
    }

    public void setUse(String use) {
        this.use = use;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getIsAlive() {
        return isAlive;
    }

    public void setIsAlive(String isAlive) {
        this.isAlive = isAlive;
    }

    public String getReq_approve() {
        return req_approve;
    }

    public void setReq_approve(String req_approve) {
        this.req_approve = req_approve;
    }
}

