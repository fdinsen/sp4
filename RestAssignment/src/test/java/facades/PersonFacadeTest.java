package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import utils.EMF_Creator;
import entities.Person;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;

    private Person p1, p2, p3;
    private Address a1, a2, a3;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getPersonFacade(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.createNamedQuery("Address.deleteAllRows").executeUpdate();
            p1 = new Person("Lars", "Larsen", "12345678");
            p2 = new Person("Peter", "Petersen", "98765432");
            p3 = new Person("Fie", "Fiesen", "91827364");
            a1 = new Address("Petersvej", "2340", "Petsburg");
            a2 = new Address("Banjogade", "7777", "Guitarino");
            a3 = new Address("Mozart Alle", "1100", "Klaverby");
            p1.setAddress(a1);
            p2.setAddress(a2);
            p3.setAddress(a3);

            em.persist(p1);
            em.persist(p2);
            em.persist(p3);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }
    
    @Test
    public void testGetAllPersonsByCount() {
        int expectedCount = 3;

        int actual = facade.getAllPersons().size();

        assertEquals(expectedCount, actual);
    }

    @Test
    public void testGetPersonById() throws PersonNotFoundException {
        int idToCheck = p1.getId();
        String expectedName = p1.getfName();
        String expectedStreet = p1.getAddress().getStreet();

        PersonDTO actual = facade.getPerson(idToCheck);
        String actualName = actual.getfName();
        String actualStreet = actual.getStreet();

        assertEquals(expectedName, actualName);
        assertEquals(expectedStreet, actualStreet);
    }

    @Test
    public void testAddPersonOnDTO() throws MissingInputException {
        Person personToAdd = new Person("Neue", "Personen", "55554444");
        Address addressToAdd = new Address("Villavej", "1213", "Lilleby");

        PersonDTO addedPerson
                = facade.addPerson(personToAdd.getfName(), personToAdd.getlName(), personToAdd.getPhone(),
                        addressToAdd.getStreet(), addressToAdd.getZip(), addressToAdd.getCity());

        assertEquals(personToAdd.getfName(), addedPerson.getfName());
        assertEquals(addressToAdd.getZip(), addedPerson.getZip());
    }

    @Test
    public void testAddPersonOnDB() throws PersonNotFoundException, MissingInputException {
        Person personToAdd = new Person("Neue", "Personen", "55554444");
        Address addressToAdd = new Address("Villavej", "1213", "Lilleby");

        PersonDTO added
                = facade.addPerson(personToAdd.getfName(), personToAdd.getlName(), personToAdd.getPhone(),
                        addressToAdd.getStreet(), addressToAdd.getZip(), addressToAdd.getCity());

        PersonDTO addedPerson = facade.getPerson(added.getId());

        assertEquals(personToAdd.getfName(), addedPerson.getfName());
        assertEquals(addressToAdd.getCity(), addedPerson.getCity());
    }
    
    @Test
    public void testAddPersonWithExistingAddress() throws PersonNotFoundException, MissingInputException {
        Person personToAdd = new Person("Neue", "Personen", "55554444");
        Address addressToAdd = new Address("Petersvej", "2340", "Petsburg");

        PersonDTO added
                = facade.addPerson(personToAdd.getfName(), personToAdd.getlName(), personToAdd.getPhone(),
                        addressToAdd.getStreet(), addressToAdd.getZip(), addressToAdd.getCity());

        PersonDTO addedPerson = facade.getPerson(added.getId());

        assertEquals(personToAdd.getfName(), addedPerson.getfName());
        assertEquals(addressToAdd.getCity(), addedPerson.getCity());
    }

    @Test
    public void testEditPersonNoAddress() throws PersonNotFoundException {
        //Arrange
        // create person object with edited lastname
        Person editedPerson = new Person(p1.getfName(), "changeson", p1.getPhone());

        // set the id to the same as p1, so p1 will be edited
        editedPerson.setId(p1.getId());
        // create a dto from person
        PersonDTO edit = new PersonDTO(editedPerson);
        // edit
        facade.editPerson(edit);

        PersonDTO actual = facade.getPerson(p1.getId());

        assertEquals(edit.getlName(), actual.getlName());
        assertEquals(p1.getAddress().getStreet(), actual.getStreet());
    }

    @Test
    public void testEditPersonWithAddress() throws PersonNotFoundException {
        //Arrange
        // create person object with edited lastname
        Person editedPerson = new Person(p1.getfName(), "changeson", p1.getPhone());
        Address editedAddres = new Address("Petersvej", "1122", "Kasperby");
        editedPerson.setAddress(a1);
        // set the id to the same as p1, so p1 will be edited
        editedPerson.setId(p1.getId());
        // create a dto from person
        PersonDTO edit = new PersonDTO(editedPerson);
        // edit
        facade.editPerson(edit);

        PersonDTO actual = facade.getPerson(p1.getId());

        assertEquals(edit.getlName(), actual.getlName());
        assertEquals(edit.getStreet(), actual.getStreet());
    }

    @Test
    public void testDeletePersonOnCount() throws PersonNotFoundException {
        int expectedCount = 2;

        facade.deletePerson(p1.getId());
        PersonsDTO all = facade.getAllPersons();
        int actualCount = all.size();

        assertEquals(expectedCount, actualCount);
    }
    
    @Test
    public void testDeletePersonSameAddress() throws PersonNotFoundException, MissingInputException {
        Person personToAdd = new Person("Neue", "Personen", "55554444");
        Address addressToAdd = new Address("Petersvej", "2340", "Petsburg");

        PersonDTO added
                = facade.addPerson(personToAdd.getfName(), personToAdd.getlName(), personToAdd.getPhone(),
                        addressToAdd.getStreet(), addressToAdd.getZip(), addressToAdd.getCity());
        int expectedCount = 3;

        facade.deletePerson(p1.getId());
        PersonsDTO all = facade.getAllPersons();
        int actualCount = all.size();

        assertEquals(expectedCount, actualCount);
    }

    @Test
    public void testAddPersonNofName() {
        MissingInputException thrown = assertThrows(MissingInputException.class,
                () -> {
                    Person personToAdd = new Person(null, "Personen", "55554444");
                    Address addressToAdd = new Address("Villavej", "1213", "Lilleby");

                    PersonDTO addedPerson
                    = facade.addPerson(personToAdd.getfName(), personToAdd.getlName(), personToAdd.getPhone(),
                            addressToAdd.getStreet(), addressToAdd.getZip(), addressToAdd.getCity());
                });
        assertNotNull(thrown);
    }

    @Test
    public void testDeleteNonExistentPerson() {
        PersonNotFoundException thrown = assertThrows(PersonNotFoundException.class,
                () -> {
                    facade.deletePerson(123214);
                });
        assertNotNull(thrown);
    }

    @Test
    public void testEditNonExistentPerson() {
        PersonNotFoundException thrown = assertThrows(PersonNotFoundException.class,
                () -> {
                    //Arrange
                    // create person object with edited lastname
                    Person editedPerson = new Person(p1.getfName(), "changeson", p1.getPhone());
                    // set the id to the same as p1, so p1 will be edited
                    editedPerson.setId(125315);
                    // create a dto from person
                    PersonDTO edit = new PersonDTO(editedPerson);
                    // edit
                    facade.editPerson(edit);
                });
        assertNotNull(thrown);
    }

    @Test
    public void testEditPersonLNameOnly() throws PersonNotFoundException {
        //Arrange
        // create person object with edited lastname
        Person editedPerson = new Person(null, "changeson", null);
        // set the id to the same as p1, so p1 will be edited
        editedPerson.setId(p1.getId());
        // create a dto from person
        PersonDTO edit = new PersonDTO(editedPerson);
        // edit
        facade.editPerson(edit);

        PersonDTO actual = facade.getPerson(p1.getId());

        assertEquals(p1.getfName(), actual.getfName());
        assertEquals(edit.getlName(), actual.getlName());
        assertEquals(p1.getPhone(), actual.getPhone());
    }

    @Test 
    public void testEditPersonMultiplePeopleOnAddress() throws PersonNotFoundException {
        //Arrange
        //Create person with an existing address
        Person personToAdd = new Person("Neue", "Personen", "55554444");
        Address addressToAdd = new Address("Petersvej", "2340", "Petsburg");
        // create person object with edited lastname
        Person editedPerson = new Person(p1.getfName(), "changeson", p1.getPhone());
        Address editedAddres = new Address("Petersvej", "1122", "Kasperby");
        editedPerson.setAddress(a1);
        // set the id to the same as p1, so p1 will be edited
        editedPerson.setId(p1.getId());
        // create a dto from person
        PersonDTO edit = new PersonDTO(editedPerson);
        // edit
        facade.editPerson(edit);

        PersonDTO actual = facade.getPerson(p1.getId());

        assertEquals(edit.getlName(), actual.getlName());
        assertEquals(edit.getStreet(), actual.getStreet());
    }
}
