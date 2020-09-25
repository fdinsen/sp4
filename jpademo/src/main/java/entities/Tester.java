/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import dto.PersonStyleDTO;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author gamma
 */
public class Tester {
    public static void main(String[] args) {
        //Initialise
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");
        EntityManager em = emf.createEntityManager();
        
        Person p1 = new Person("Peter", 1995);
        Person p2 = new Person("Lasse", 1990);
        
        Address a1 = new Address("Store torv 1 ", 2323, "Nedesnup");
        Address a2 = new Address("Langgade 32", 1234, "Opperrup");
        
        Fee f1 = new Fee(120);
        Fee f2 = new Fee(230);
        Fee f3 = new Fee(150);
        
        SwimStyle s1 = new SwimStyle("Crawl");
        SwimStyle s2 = new SwimStyle("Butterfly");
        SwimStyle s3 = new SwimStyle("Breast stroke");
        
        
        //Act
        p1.setAddress(a1);
        p2.setAddress(a2);
        
        p1.addFee(f1);
        p1.addFee(f2);
        p2.addFee(f3);
        
        p1.addSwimStyle(s1);
        p1.addSwimStyle(s3);
        p2.addSwimStyle(s2);
        
        em.getTransaction().begin();
        em.persist(p1);
        em.persist(p2);
        em.getTransaction().commit();
        
        //p1 is still managed by EntityManager, so any changes made within a transaction will be persisted
        em.getTransaction().begin();
        p1.removeSwimStyle(s3);
        em.getTransaction().commit();
        
        //View
        System.out.println("p1: " + p1.getP_id() + ", " + p1.getName());
        System.out.println("p2: " + p2.getP_id() + ", " + p2.getName());
        
        System.out.println("Peter's gade: " + p1.getAddress().getStreet());
        System.out.println("Adresse 2's person: " + a2.getPerson().getName());
        
        System.out.println("Hvem har betalt f2?: " + f2.getPerson().getName());
        
        TypedQuery q1 = em.createQuery("SELECT f FROM Fee f", Fee.class);
        List<Fee> fees = q1.getResultList();
        
        for(Fee f : fees) {
            System.out.println(f.getPerson().getName() + ": " + f.getAmount() + ", " + f.getPayDate());
        }
        
        TypedQuery<Person> q2 = em.createQuery("SELECT p FROM Person p", Person.class);
        List<Person> persons = q2.getResultList();
        for(Person p : persons) {
            System.out.println("Navn: " + p.getName());
            System.out.println("--Fees:");
            for(Fee f : p.getFees()) {
                System.out.println("---- Beløb: " + f.getAmount() + ", " + f.getPayDate().toString());
            }
            System.out.println("--Styles:");
            for(SwimStyle ss: p.getStyles()) {
                System.out.println("---- Style: " + ss.getStyleName());
            }
        }
        
        System.out.println("**** Eksperimenter med JPQL");
        
        Query q3 = em.createQuery("SELECT new dto.PersonStyleDTO(p.name, p.year, s.styleName) FROM Person p JOIN p.styles s");
        List<PersonStyleDTO> personList = q3.getResultList();
        
        for(PersonStyleDTO p : personList) {
            System.out.println(p.getName() + ", " + p.getYear() + ", " + p.getSwimStyle());
        }
        
        
    }
}
