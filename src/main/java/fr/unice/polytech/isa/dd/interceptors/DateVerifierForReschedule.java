package fr.unice.polytech.isa.dd.interceptors;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class DateVerifierForReschedule extends DateVerifier {

    @AroundInvoke
    public Object intercept (InvocationContext context) throws Exception {
        String old_date = context.getParameters()[0].toString();
        String new_date = context.getParameters()[2].toString();
        if(! verifydates(old_date,new_date)) throw new Exception("The date is the date is neither in format dd/MM/yyyy nor valid");
        return context.proceed();
    }

    private boolean verifydates(String date1, String date2){
        boolean validdate1 = !(date1.trim().equals("")) && validate_date(date1);
        boolean validdate2 = !(date2.trim().equals("")) && validate_date(date2);

        return validdate1 && validdate2;
    }
}
