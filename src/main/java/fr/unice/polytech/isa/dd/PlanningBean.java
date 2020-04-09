package fr.unice.polytech.isa.dd;

import fr.unice.polytech.isa.dd.entities.Customer;
import fr.unice.polytech.isa.dd.entities.Delivery;
import fr.unice.polytech.isa.dd.entities.Package;
import utils.MyDate;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless(name="planning-stateless")
public class PlanningBean implements DeliveryRegistration, AvailableSlotTime {

    @PersistenceContext
    private EntityManager entityManager;

    @EJB(name = "delivery-stateless") private DeliverySchedule deliverySchedule;

    private boolean validslot = false;

    @Override
    public void register_delivery(String name_client, String number_secret, String delivery_date, String hour_delivery) throws Exception {

        int number = Integer.parseInt(number_secret);
        Customer customer = findCustomerByName(name_client);
        MyDate dt = new MyDate(delivery_date,hour_delivery);
        Delivery delivery = new Delivery();
        Package aPackage = findPackageByNumber(number);
        assert aPackage != null;
        delivery.setPackageDelivered(aPackage);
        delivery.setDeliveryDate(delivery_date);
        delivery.setDeliveryBeginTimeInSeconds(dt.getDate_seconds());
        assert customer != null;
        if(validslot){
            customer.add_a_customer_delivery(delivery);
            entityManager.persist(delivery);
        }else throw  new Exception("Slot nont disponible");

    }

    private Package findPackageByNumber(int number) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Package> criteria = builder.createQuery(Package.class);
        Root<Package> root =  criteria.from(Package.class);
        criteria.select(root).where(builder.equal(root.get("secretNumber"), number));
        TypedQuery<Package> query = entityManager.createQuery(criteria);
        try {
           return Optional.of(query.getSingleResult()).get();
        } catch (NoResultException nre){
            return null;
        }
    }

    private Customer findCustomerByName(String name) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Customer> criteria = builder.createQuery(Customer.class);
        Root<Customer> root =  criteria.from(Customer.class);
        criteria.select(root).where(builder.equal(root.get("name"), name));
        TypedQuery<Customer> query = entityManager.createQuery(criteria);
        try {
            return Optional.of(query.getSingleResult()).get();
        } catch (NoResultException nre){
            return null;
        }
    }

    private List<Delivery> all_deliveries(String delivery_date) throws Exception {
        if(!deliverySchedule.get_deliveries().isEmpty()){
            List<Delivery> deliveriesList = deliverySchedule.get_deliveries();
            MyDate myDate = new MyDate(delivery_date,"00h00");
            deliveriesList = deliveriesList.stream().filter(d->d.getDeliveryDate().equals(delivery_date)).collect(Collectors.toList());
            deliveriesList.sort(Comparator.comparingInt(Delivery::getDeliveryBeginTimeInSeconds));
            return deliveriesList;
        }
        return null;
    }

    @Override
    public boolean valid_slot_time(String delivery_date, String hour_delivery) throws Exception {
        List<Delivery> sorted_filtered_list = all_deliveries(delivery_date);
        MyDate adate = new MyDate(delivery_date,hour_delivery);
        int min_slot = 45 * 60;
        int adateseconds = adate.getDate_seconds();
        int minb = 0; int index_min = 0; int mine = 0;
        int max = 0; int index_max;
        //int min = sorted_filtered_list.get(0).getDeliveryBeginTimeInSeconds();
        //assert sorted_filtered_list != null;
        if(sorted_filtered_list != null && !sorted_filtered_list.isEmpty()){
            while(index_min < sorted_filtered_list.size() ) {
                int temp = sorted_filtered_list.get(index_min).getDeliveryBeginTimeInSeconds();
                if (temp < adateseconds) {
                    mine = sorted_filtered_list.get(index_min).getDeliveryEndTimeInSeconds();
                }else break;
                index_min++;
            }
            max = sorted_filtered_list.get(index_min).getDeliveryBeginTimeInSeconds();

            int diff1 =  adateseconds - mine;
            int diff2 =  max - (adateseconds + min_slot );
       /* System.out.println("/*********************************\n"+diff1+"******************\n");
        System.out.println("/*********************************\n"+diff2+"******************\n");*/
            if(diff1 >= min_slot && diff2 >= min_slot){
                validslot = true;
                return true;
            }
        }else{
            validslot = true;
            return true;
        }
        return false;
        }

    }
