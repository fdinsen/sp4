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
    
    public PersonDTO(Person person) {
        this.fName = person.getfName();
        this.lName = person.getlName();
        this.phone = person.getPhone();
        this.id = person.getId();
    }
    
    public PersonDTO(String fName, String lName, String phone) {
        this.fName = fName;
        this.lName = lName;
        this.phone = phone;
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
    
    
}
