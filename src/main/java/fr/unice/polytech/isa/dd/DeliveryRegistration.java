package fr.unice.polytech.isa.dd;

import javax.ejb.Local;

@Local
public interface DeliveryRegistration {

    void register_delivery(String name_client, String package_number, String delivery_date, String hour_delivery) throws Exception;

    void repogramming_delivery( String old_date, String old_hour,String delivery_date, String hour_delivery) throws Exception;
}
