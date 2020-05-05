package fr.unice.polytech.isa.dd;

import fr.unice.polytech.isa.dd.exceptions.PackageAlreadyTookException;
import fr.unice.polytech.isa.dd.exceptions.UnvailableSlotTimeException;
import fr.unice.polytech.isa.dd.exceptions.UnknownCustomerException;
import fr.unice.polytech.isa.dd.exceptions.UnknownPackageException;

import javax.ejb.Local;
import java.text.ParseException;

@Local
public interface DeliveryRegistration {

    String register_delivery(String name_client, String package_number, String delivery_date, String hour_delivery) throws PackageAlreadyTookException, UnvailableSlotTimeException, UnknownCustomerException, UnknownPackageException, ParseException;

    String repogramming_delivery( String old_date, String old_hour,String delivery_date, String hour_delivery) throws UnvailableSlotTimeException, ParseException;
}
