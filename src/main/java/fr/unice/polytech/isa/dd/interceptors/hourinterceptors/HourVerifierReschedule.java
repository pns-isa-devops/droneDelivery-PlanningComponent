package fr.unice.polytech.isa.dd.interceptors.hourinterceptors;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class HourVerifierReschedule extends HourVerifier{

    @AroundInvoke
    public Object intercept (InvocationContext context) throws Exception {
        String old_hour= context.getParameters()[1].toString();
        String new_hour= context.getParameters()[3].toString();
        if(! verifyhours(old_hour,new_hour)) throw new Exception("An hour is neither in format HHhMM nor valid");
        return context.proceed();
    }

    private boolean verifyhours(String hour1, String hour2){
        boolean validhour1 = !(hour1.trim().equals("")) && validate_hour(hour1);
        boolean validhour2 = !(hour2.trim().equals("")) && validate_hour(hour2);

        return validhour1 && validhour2;
    }

}
