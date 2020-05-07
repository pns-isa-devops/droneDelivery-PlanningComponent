package fr.unice.polytech.isa.dd.interceptors;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateVerifier {

    public boolean validate_date(String strDate) {
        SimpleDateFormat sdfrmt = new SimpleDateFormat("dd/MM/yyyy");
        sdfrmt.setLenient(false);

        try
        {
            sdfrmt.parse(strDate);
        }
        catch (ParseException e)
        {
            return false;
        }
        return true;
    }
}
