import com.example.FACT.controller.RegistrationController;
import com.example.FACT.model.*;
import org.junit.jupiter.api.*;

import java.sql.Connection;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;
public class RegistrationTest {
    @Test
    public void testUsingCorrectEmail() {
        String emailAddress = "username@domain.com";
        assertTrue(RegistrationController.patternMatches(emailAddress));
    }
    @Test
    public void testUsingIncorrectEmailSymbol(){
        String emailAddress = "username#domain.com";
        assertFalse(RegistrationController.patternMatches(emailAddress));
    }
    @Test
    public void testUsingIncorrectEmailFormat(){
        String emailAddress = "user-name@domain.com.";
        assertFalse(RegistrationController.patternMatches(emailAddress));
    }
}
