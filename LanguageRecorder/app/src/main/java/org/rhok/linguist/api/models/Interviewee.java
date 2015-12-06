package org.rhok.linguist.api.models;

import org.rhok.linguist.code.entity.Person;

/**
 * Created by bramleyt on 5/12/2015.
 */
public class Interviewee {
    private int id;

    private String name;
    private String mobile;
    private String email;
    private Integer age;
    private String gender;
    private String occupation;
    private String education_level;
    private String firstLanguage;
    private String secondLanguage;
    private String thirdLanguage;
    private String fourthLanguage;
    private String livesInMunicipality;
    private String livesInDistrict;
    private String livesInVillage;
    private Boolean livedWholeLife;
    private Integer livedInYears;
    private String bornMunicipality;
    private String bornDistrict;
    private String bornVillage;

    public Interviewee(){

    }
    public Interviewee(Person person){
        name=person.name;
        mobile=null;
        email=null;
        age=person.age;
        gender=person.gender;
        occupation=person.occupation;
        education_level=person.education;
        firstLanguage=person.firstLanguage;
        secondLanguage=person.secondLanguage;
        thirdLanguage=person.thirdLanguage;
        fourthLanguage=person.fourthLanguage;
        livesInMunicipality=person.livesInMunicipality;
        livesInDistrict=person.livesInDistrict;
        livesInVillage=person.livesInVillage;
        livedWholeLife=person.livedWholeLife;
        livedInYears=person.livedInYears;
        bornMunicipality=person.bornMunicipality;
        bornDistrict=person.bornDistrict;
        bornVillage=person.bornVillage;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getEducation_level() {
        return education_level;
    }

    public void setEducation_level(String education_level) {
        this.education_level = education_level;
    }

    public String getFirstLanguage() {
        return firstLanguage;
    }

    public void setFirstLanguage(String firstLanguage) {
        this.firstLanguage = firstLanguage;
    }

    public String getSecondLanguage() {
        return secondLanguage;
    }

    public void setSecondLanguage(String secondLanguage) {
        this.secondLanguage = secondLanguage;
    }

    public String getThirdLanguage() {
        return thirdLanguage;
    }

    public void setThirdLanguage(String thirdLanguage) {
        this.thirdLanguage = thirdLanguage;
    }

    public String getFourthLanguage() {
        return fourthLanguage;
    }

    public void setFourthLanguage(String fourthLanguage) {
        this.fourthLanguage = fourthLanguage;
    }

    public String getLivesInMunicipality() {
        return livesInMunicipality;
    }

    public void setLivesInMunicipality(String livesInMunicipality) {
        this.livesInMunicipality = livesInMunicipality;
    }

    public String getLivesInDistrict() {
        return livesInDistrict;
    }

    public void setLivesInDistrict(String livesInDistrict) {
        this.livesInDistrict = livesInDistrict;
    }

    public String getLivesInVillage() {
        return livesInVillage;
    }

    public void setLivesInVillage(String livesInVillage) {
        this.livesInVillage = livesInVillage;
    }

    public Boolean getLivedWholeLife() {
        return livedWholeLife;
    }

    public void setLivedWholeLife(Boolean livedWholeLife) {
        this.livedWholeLife = livedWholeLife;
    }

    public Integer getLivedInYears() {
        return livedInYears;
    }

    public void setLivedInYears(Integer livedInYears) {
        this.livedInYears = livedInYears;
    }

    public String getBornMunicipality() {
        return bornMunicipality;
    }

    public void setBornMunicipality(String bornMunicipality) {
        this.bornMunicipality = bornMunicipality;
    }

    public String getBornDistrict() {
        return bornDistrict;
    }

    public void setBornDistrict(String bornDistrict) {
        this.bornDistrict = bornDistrict;
    }

    public String getBornVillage() {
        return bornVillage;
    }

    public void setBornVillage(String bornVillage) {
        this.bornVillage = bornVillage;
    }
}
