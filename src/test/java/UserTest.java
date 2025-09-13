import com.example.FACT.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class UserTest {
    private static final String FIRST_NAME = "John";
    private static final String FIRST_NAME_TWO = "Jane";
    private static final String LAST_NAME = "Doe";
    private static final String LAST_NAME_TWO = "Doe";
    private static final String EMAIL = "john@gmail.com";
    private static final String EMAIL_TWO = "jane@gmail.com";
    private static final String PASSWORD = "TEST";
    private static final String PASSWORD_TWO = "TESTER";

    private User user;
    private User userTwo;

    @BeforeEach
    public void setup(){
        user = new User(FIRST_NAME, LAST_NAME, EMAIL, PASSWORD);
        userTwo = new User(FIRST_NAME_TWO, LAST_NAME_TWO, EMAIL_TWO, PASSWORD);
    }


    @Test
    public void testGetFirstName() {
        assertEquals(FIRST_NAME, user.getFirstName());
    }
    @Test
    public void testSetFirstName() {
        user.setFirstName(FIRST_NAME_TWO);
        assertEquals(FIRST_NAME_TWO, user.getFirstName());
    }
    @Test
    public void testGetLastName() {
        assertEquals(LAST_NAME, user.getLastName());
    }
    @Test
    public void testSetLastName() {
        user.setLastName(LAST_NAME_TWO);
        assertEquals(LAST_NAME_TWO, user.getLastName());
    }
    @Test
    public void testGetEmail() {
        assertEquals(EMAIL, user.getEmail());
    }
    @Test
    public void testSetEmail() {
        user.setEmail(EMAIL_TWO);
        assertEquals(EMAIL_TWO, user.getEmail());
    }
    @Test
    public void testGetPassword() {
        assertEquals(PASSWORD, user.getPassword());
    }
    @Test
    public void testSetPassword() {
        user.setPassword(PASSWORD_TWO);
        assertEquals( PASSWORD_TWO, user.getPassword());
    }

}
