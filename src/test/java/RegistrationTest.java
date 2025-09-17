import com.example.FACT.controller.RegistrationController;
import com.example.FACT.model.*;
import org.junit.jupiter.api.*;


/// done by summer n11187450
import static org.junit.jupiter.api.Assertions.*;
public class RegistrationTest {
    public String regex = "^\\b[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}\\b$";
    @Test
    public void testUsingCorrectEmail() {
        String emailAddress = "username@domain.com";
        assertTrue(RegistrationController.patternMatches(emailAddress, regex));
    }
    @Test
    public void testUsingIncorrectEmailSymbol(){
        String emailAddress = "username#domain.com";
        assertFalse(RegistrationController.patternMatches(emailAddress, regex));
    }
    @Test
    public void testUsingIncorrectEmailFormat(){
        String emailAddress = "user-name@domain.com.";
        assertFalse(RegistrationController.patternMatches(emailAddress, regex));
    }
}
