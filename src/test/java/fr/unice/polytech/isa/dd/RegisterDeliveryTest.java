package fr.unice.polytech.isa.dd;

import arquillian.AbstractPlanningTest;
import fr.unice.polytech.isa.dd.Exceptions.PackageAlreadyTookException;
import fr.unice.polytech.isa.dd.Exceptions.UnvailableSlotTimeException;
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

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.*;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
@Transactional(TransactionMode.COMMIT)
public class RegisterDeliveryTest extends AbstractPlanningTest {

    @EJB(name = "planning-stateless")  DeliveryRegistration deliveryRegistration;
    @EJB(name = "planning-stateless")  AvailableSlotTime availableSlotTime;
    @EJB(name="delivery-stateless") DeliverySchedule deliverySchedule;
    @PersistenceContext
    private EntityManager entityManager;
    @Inject
    private UserTransaction utx;

    private Customer customer = new Customer("Paul","where does he live");
    private Provider provider = new Provider();
    private Package aPackage1 = new Package();
    private Package aPackage2 = new Package();
    String adate;
    String anhour;

    @Before
    public void setUp(){
        adate = "31/12/2020";
        anhour = "18h30";
        entityManager.persist(customer);

        provider.setName("Aug");
        entityManager.persist(provider);

        aPackage1.setWeight(10.0);
        aPackage1.setSecret_number("2005");
        aPackage1.setProvider(provider);
        entityManager.persist(aPackage1);
        provider.add(aPackage1);

        aPackage2.setWeight(10.0);
        aPackage2.setSecret_number("2020");
        aPackage2.setProvider(provider);
        entityManager.persist(aPackage2);
        provider.add(aPackage2);
    }

    @After
    public void cleanUp() throws SystemException, NotSupportedException, HeuristicRollbackException, HeuristicMixedException, RollbackException {
        utx.begin();
        Delivery delivery = deliverySchedule.get_deliveries().get(0);
        entityManager.merge(delivery);
        entityManager.remove(delivery);

        aPackage1 = entityManager.merge(aPackage1);
        entityManager.remove(aPackage1);
        aPackage2 = entityManager.merge(aPackage2);
        entityManager.remove(aPackage2);

        provider = entityManager.merge(provider);
        entityManager.remove(provider);

        customer = entityManager.merge(customer);
        entityManager.remove(customer);

        utx.commit();
    }


    @Test
    public void registertest() throws Exception {
        availableSlotTime.valid_slot_time(adate,anhour);
        deliveryRegistration.register_delivery("Paul","2005",adate,anhour);
        assertEquals(1,customer.getCustomer_deliveries().size());
    }

    @Test(expected = PackageAlreadyTookException.class)
    public void registerExceptionTimeTest() throws Exception {
        availableSlotTime.valid_slot_time(adate,anhour);
        deliveryRegistration.register_delivery("Paul","2005",adate,anhour);
        deliveryRegistration.register_delivery("Paul","2005",adate,anhour);
    }

    @Test(expected = UnvailableSlotTimeException.class)
    public void registerExceptionPackageTest() throws Exception {
        availableSlotTime.valid_slot_time(adate,anhour);
        deliveryRegistration.register_delivery("Paul","2005",adate,anhour);
        availableSlotTime.valid_slot_time(adate,anhour);
        deliveryRegistration.register_delivery("Paul","2020",adate,anhour);
    }
}
