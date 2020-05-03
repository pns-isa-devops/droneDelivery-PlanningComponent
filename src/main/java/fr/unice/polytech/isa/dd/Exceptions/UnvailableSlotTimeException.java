package fr.unice.polytech.isa.dd.Exceptions;


import javax.xml.ws.WebFault;
import java.io.Serializable;

@WebFault(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/dd/planningService")
public class UnvailableSlotTimeException extends Exception implements Serializable {

    private String dateUnvailable;
    private String hourUnvailable;

    public UnvailableSlotTimeException(String date, String hour){
        super("Le slot de : "+date+" - "+hour+" est indisponible" );
        this.dateUnvailable = date;
        this.hourUnvailable = hour;
    }

    public String getDateUnvailable() {
        return dateUnvailable;
    }

    public String getHourUnvailable() {
        return hourUnvailable;
    }

    public void setDateUnvailable(String dateUnvailable) {
        this.dateUnvailable = dateUnvailable;
    }

    public void setHourUnvailable(String hourUnvailable) {
        this.hourUnvailable = hourUnvailable;
    }
}
