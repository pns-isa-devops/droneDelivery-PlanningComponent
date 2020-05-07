package fr.unice.polytech.isa.dd.interceptors.hourinterceptors;

import javax.interceptor.AroundInvoke;
import javax.interceptor.InvocationContext;

public class HourVerifierForRegister extends HourVerifier{

    @AroundInvoke
    public Object intercept (InvocationContext context) throws Exception {
        String hour = context.getParameters()[3].toString();
        if(hour.trim().equals("")) throw new Exception("An hour cannot be undefined");
        if(! validate_hour(hour)) throw new Exception("Hour is neither in format HHhMM nor valid");
        return context.proceed();
    }
}
