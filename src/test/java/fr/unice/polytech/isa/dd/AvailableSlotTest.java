package fr.unice.polytech.isa.dd;

import arquillian.AbstractPlanningTest;
import fr.unice.polytech.isa.dd.entities.Customer;
import fr.unice.polytech.isa.dd.entities.Delivery;
import fr.unice.polytech.isa.dd.entities.Package;
import fr.unice.polytech.isa.dd.entities.Provider;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.transaction.api.annotation.TransactionMode;
import org.jboss.arquillian.transaction.api.annotation.Transactional;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import utils.MyDate;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class AvailableSlotTest extends AbstractPlanningTest {

    @EJB(name = "planning-stateless")
    DeliveryRegistration deliveryRegistration;
    @EJB(name = "planning-stateless")
    AvailableSlotTime availableSlotTime;
    @EJB(name = "delivery-stateless") DeliverySchedule deliverySchedule;
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private UserTransaction utx;

    private Customer customer = new Customer("Paul","where does he live");
    private Provider provider = new Provider();
    private Package aPackage = new Package();
    private Package aPackageDemanded = new Package();
    private Delivery delivery1 = new Delivery();
    private Delivery delivery2 = new Delivery();
    String adate;
    String anhour;

    @Before
    public void setUp() throws Exception {
        adate = "31/12/2020";
        anhour = "10h45";
        entityManager.persist(customer);

        provider.setName("Aug");
        entityManager.persist(provider);

        aPackage.setWeight(10.0);
        aPackage.setSecret_number("2000");
        aPackage.setProvider(provider);
        entityManager.persist(aPackage);
        provider.add(aPackage);

        MyDate dt = new MyDate(adate,"9h00");
        delivery1.setPackageDelivered(aPackage);
        delivery1.setDeliveryDate(adate);
        delivery1.setDeliveryBeginTimeInSeconds(dt.getDate_seconds());
        delivery1.setDeliveryEndTimeInSeconds(dt.getDate_seconds()+ 2700);
        customer.add_a_customer_delivery(delivery1);
        entityManager.persist(delivery1);

        MyDate dt2 = new MyDate(adate,"12h15");
        delivery2.setPackageDelivered(aPackage);
        delivery2.setDeliveryDate(adate);
        delivery2.setDeliveryBeginTimeInSeconds(dt2.getDate_seconds());
        delivery2.setDeliveryEndTimeInSeconds(dt2.getDate_seconds()+ 2700);
        customer.add_a_customer_delivery(delivery2);
        entityManager.persist(delivery2);


        aPackageDemanded.setSecret_number("2020");
        aPackageDemanded.setProvider(provider);
        aPackageDemanded.setWeight(30.0);
        provider.add(aPackageDemanded);
        entityManager.persist(aPackageDemanded);
    }

    @After
    public void cleanUp() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        utx.begin();
        for( int i = 0; i < 3; i++){
            Delivery delivery = deliverySchedule.get_deliveries().get(0);
            entityManager.merge(delivery);
            entityManager.remove(delivery);
        }

        aPackage = entityManager.merge(aPackage);
        entityManager.remove(aPackage);

        aPackageDemanded = entityManager.merge(aPackageDemanded);
        entityManager.remove(aPackageDemanded);

        provider = entityManager.merge(provider);
        entityManager.remove(provider);

        customer = entityManager.merge(customer);
        entityManager.remove(customer);

        utx.commit();
    }

    @Test
    public void validslottest() throws Exception {
        boolean valid = availableSlotTime.valid_slot_time(adate,anhour);
        assertTrue(valid);
        deliveryRegistration.register_delivery("Paul","2020",adate,anhour);
        assertEquals(3,customer.getCustomer_deliveries().size());
        assertEquals(3,deliverySchedule.get_deliveries().size());
    }
}
