package fr.unice.polytech.isa.dd.interceptors;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class DateVerifierForRegister extends DateVerifier{

    @AroundInvoke
    public Object intercept (InvocationContext context) throws Exception {
        String date = context.getParameters()[2].toString();
        if(date.trim().equals("")) throw new Exception("The date cannot be undefined");
        if(! validate_date(date)) throw new Exception("The date is the date is neither in format dd/MM/yyyy nor valid");
        return context.proceed();
    }
}
