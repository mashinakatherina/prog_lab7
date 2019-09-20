package ifmo.programming.lab7;


import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

public class Utilities {



    public static boolean isValidEmailAddress(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddr = new InternetAddress(email);
            emailAddr.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}
