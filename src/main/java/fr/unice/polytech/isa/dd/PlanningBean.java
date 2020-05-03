package fr.unice.polytech.isa.dd;

import fr.unice.polytech.isa.dd.Exceptions.UnvailableSlotTimeException;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless(name="planning-stateless")
public class PlanningBean implements DeliveryRegistration, AvailableSlotTime {

    @PersistenceContext
    private EntityManager entityManager;

    @EJB(name = "delivery-stateless") private DeliverySchedule deliverySchedule;
    @EJB(name = "package-stateless") private PackageFinder packageFinder;
    @EJB(name = "customer-stateless") private CustomerFinder customerFinder;

    private boolean validslot = false;

    @Override
    public String register_delivery(String name_client, String number_secret, String delivery_date, String hour_delivery) throws Exception {
        Customer customer = customerFinder.findCustomerByName(name_client);
        Package aPackage = packageFinder.findPackageBySecretNumber(number_secret);
        MyDate dt = new MyDate(delivery_date,hour_delivery);
        Delivery delivery = new Delivery(customer,aPackage,delivery_date,dt.getDate_seconds());
        if(validslot){
            customer.add_a_customer_delivery(delivery);
            entityManager.persist(delivery);
        }else throw  new UnvailableSlotTimeException(delivery_date,hour_delivery);
        return "Livraison Programmé";
    }

    @Override
    public String repogramming_delivery(String old_date, String old_hour, String delivery_date, String hour_delivery) throws Exception {
        if(validslot){
            Delivery delivery = deliverySchedule.findDeliveryByDateAndHour(old_date, old_hour);
            MyDate myDate = new MyDate(delivery_date,hour_delivery);
            Delivery delivery1 =  entityManager.find(Delivery.class,delivery.getId());
            delivery1.setDeliveryDate(delivery_date);
            delivery1.setDeliveryBeginTimeInSeconds(myDate.getDate_seconds());
            entityManager.persist(delivery1);
        }else throw  new UnvailableSlotTimeException(delivery_date,hour_delivery);
        return "La livraison a été reprogrammé !";
    }

    @Override
    public Boolean valid_slot_time(String delivery_date, String hour_delivery) throws Exception {
        List<Delivery> sorted_filtered_list = deliverySchedule.all_deliveries_of_theDate(delivery_date);
        int adateseconds = new MyDate(delivery_date,hour_delivery).getDate_seconds();
        int min_slot = 45 * 60;

        if(sorted_filtered_list != null && !sorted_filtered_list.isEmpty()) {
            int size = sorted_filtered_list.size();
            boolean is_the_smallest = adateseconds < sorted_filtered_list.get(0).getDeliveryBeginTimeInSeconds();
            boolean is_the_biggest = adateseconds > sorted_filtered_list.get(size-1).getDeliveryBeginTimeInSeconds();

            if (is_the_smallest) {
                int temp = sorted_filtered_list.get(0).getDeliveryBeginTimeInSeconds();
                int end = adateseconds + min_slot;
                return (end - temp) >= min_slot;
            } else if (is_the_biggest) {
                int temp = sorted_filtered_list.get(size-1).getDeliveryEndTimeInSeconds();
                return (adateseconds - temp) >= min_slot;
            } else {
                return searchFreeSlotTime(sorted_filtered_list,adateseconds,min_slot);
            }
        }else{
            validslot = true;
            return true;
        }
    }

    private boolean searchFreeSlotTime(List<Delivery> deliveries, int timeinseconds, int min_slot){
        int index_min = 0; int mine = 0;

        while (index_min < deliveries.size()) {
            int temp = deliveries.get(index_min).getDeliveryBeginTimeInSeconds();
            if (temp < timeinseconds) {
                mine = deliveries.get(index_min).getDeliveryEndTimeInSeconds();
            } else break;
            index_min++;
        }
        int max = deliveries.get(index_min).getDeliveryBeginTimeInSeconds();

        int diff1 = timeinseconds - mine;
        int diff2 = max - (timeinseconds + min_slot);
        if (diff1 >= min_slot && diff2 >= min_slot) {
            validslot = true;
            return true;
        }
        return false;
    }
}
