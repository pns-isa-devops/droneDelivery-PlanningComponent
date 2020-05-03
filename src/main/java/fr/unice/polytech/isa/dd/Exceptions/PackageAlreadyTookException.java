package fr.unice.polytech.isa.dd.Exceptions;

import java.io.Serializable;

public class PackageAlreadyTookException extends Exception implements Serializable {

    public PackageAlreadyTookException(String number){
        super("This package : "+number+ " is already took");
    }
}
