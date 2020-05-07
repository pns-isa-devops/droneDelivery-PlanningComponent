package fr.unice.polytech.isa.dd.interceptors;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class DateVerifierForValidSlot extends DateVerifier {
    @AroundInvoke
    public Object intercept (InvocationContext context) throws Exception {
        String date = context.getParameters()[0].toString();
        if(date.trim().equals("")) throw new Exception("The date cannot be undefined");
        if(! validate_date(date)) throw new Exception("The date is the date is neither in format dd/MM/yyyy nor valid");
            return context.proceed();
    }
}
