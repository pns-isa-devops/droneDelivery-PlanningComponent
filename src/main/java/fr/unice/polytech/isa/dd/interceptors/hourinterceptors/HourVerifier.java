package fr.unice.polytech.isa.dd.interceptors.hourinterceptors;

public class HourVerifier {

    protected boolean validate_hour(String strhour) {
        if(strhour.indexOf('h') == -1) return false;
        String [] strhoursplitted = strhour.split("h");
        int hour = Integer.parseInt(strhoursplitted[0]);
        int minutes = Integer.parseInt(strhoursplitted[1]);
        if(!(hour <= 23 && hour >= 0)) return false;
        return minutes <= 59 && minutes >= 0;
    }
}
