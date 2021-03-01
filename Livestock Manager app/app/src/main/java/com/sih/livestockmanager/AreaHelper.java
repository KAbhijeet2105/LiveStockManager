package com.sih.livestockmanager;

public class AreaHelper {

    String Animal_count,regionId;

    public AreaHelper() {

    }

    public AreaHelper(String animal_count, String regionId) {
        Animal_count = animal_count;
        this.regionId = regionId;
    }

    public String getAnimal_count() {
        return Animal_count;
    }

    public void setAnimal_count(String animal_count) {
        Animal_count = animal_count;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }
}
