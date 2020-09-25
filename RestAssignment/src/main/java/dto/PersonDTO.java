/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Person;

/**
 *
 * @author gamma
 */
public class PersonDTO {

    private String fName;
    private String lName;
    private String phone;
    private int id;
    private String street;
    private String zip;
    private String city;

    public PersonDTO(Person person) {
        this.fName = person.getfName();
        this.lName = person.getlName();
        this.phone = person.getPhone();
        this.id = person.getId();
        if (person.getAddress() != null) {
            this.street = person.getAddress().getStreet();
            this.zip = person.getAddress().getZip();
            this.city = person.getAddress().getCity();
        }
    }

    public PersonDTO(String fName, String lName, String phone, String street, String zip, String city) {
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
        this.street = street;
        this.zip = zip;
        this.city = city;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

}
