package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.Person;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getPersonFacade(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public PersonDTO addPerson(String fName, String lName, String phone, String street, String zip, String city) throws MissingInputException {
        if(fName == null || lName == null || phone == null){
            throw new MissingInputException("fName, lName or phone is missing.");
        }else if(street == null || zip == null || city == null) {
            throw new MissingInputException("street, zip or city is missing");
        }
        EntityManager em = getEntityManager();
        try {
            Person person = new Person(fName, lName, phone);
            person.setAddress(new Address(street, zip, city));
            em.getTransaction().begin();
            em.persist(person);
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO deletePerson(int id) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        try {
            Person personToBeDeleted = em.find(Person.class, id);
            if (personToBeDeleted != null) {
                em.getTransaction().begin();
                em.remove(personToBeDeleted);
                em.getTransaction().commit();
                if(personToBeDeleted.getAddress() != null) {
                    em.getTransaction().begin();
                    em.remove(personToBeDeleted.getAddress());
                    em.getTransaction().commit();
                }
                
                return new PersonDTO(personToBeDeleted);
            }else {
                throw new PersonNotFoundException("Could not delete, provided id does not exist");
            }
                
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO getPerson(int id) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        try {
            Person person = em.find(Person.class, id);
            if (person != null) {
                return new PersonDTO(person);
            } else {
                throw new PersonNotFoundException("No person with provided id found");
            }
//            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p WHERE p.id = :id", Person.class);
//            q.setParameter("id", id);
//            List<Person> per = q.getResultList();
//            if(per.size() > 0) {
//                PersonDTO person = new PersonDTO(per.get(0));
//                return person;
//            }
        } finally {
            em.close();
        }
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Person> q = em.createQuery("SELECT p FROM Person p LEFT JOIN Address a ON p.id = a.id", Person.class);
            PersonsDTO all = new PersonsDTO(q.getResultList());
            return all;
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Person editedPerson = em.find(Person.class, p.getId());
            if(editedPerson != null) {
                if(p.getfName() != null) {
                    editedPerson.setfName(p.getfName());
                }
                if(p.getlName() != null) {
                    editedPerson.setlName(p.getlName());
                }
                if(p.getPhone() != null) {
                    editedPerson.setPhone(p.getPhone());    
                }
                if (p.getStreet() != null) {
                    editedPerson.getAddress().setStreet(p.getStreet()); 
                }
                if (p.getZip() != null) {
                    editedPerson.getAddress().setZip(p.getZip());
                }
                if (p.getCity() != null) {
                    editedPerson.getAddress().setCity(p.getCity());
                }
                em.getTransaction().commit();
                return new PersonDTO(editedPerson);
            }else {
                throw new PersonNotFoundException("Could not edit, provided id does not exist");
            }
        } finally {
            em.close();
        }
    }

}
