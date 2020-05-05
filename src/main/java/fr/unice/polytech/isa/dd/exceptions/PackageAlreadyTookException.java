package fr.unice.polytech.isa.dd.exceptions;

import javax.xml.ws.WebFault;
import java.io.Serializable;
@WebFault(targetNamespace = "http://www.polytech.unice.fr/si/4a/isa/dd/planningService")
public class PackageAlreadyTookException extends Exception implements Serializable {
    private String number;

    public PackageAlreadyTookException(String number){
        super("This package : "+number+ " is already took");
        this.number = number;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }
}
