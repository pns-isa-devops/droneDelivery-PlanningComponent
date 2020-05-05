package fr.unice.polytech.isa.dd;
import javax.ejb.Local;
import java.text.ParseException;

@Local
public interface AvailableSlotTime {

    Boolean valid_slot_time(String delivery_date, String hour_delivery) throws  ParseException;
}
