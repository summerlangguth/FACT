import com.example.FACT.model.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.Instant;
import java.time.*;
import java.util.concurrent.TimeUnit;
/// done by summer n11187450
import static org.junit.jupiter.api.Assertions.*;
/// use to create mock data -> safer than testing using an actual database.
import static org.mockito.Mockito.*;
public class SQLiteUserTest {

    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private SqliteUserDAO userTest;
    private User mockUser;
    private UserManager mockUserManager;

    @BeforeEach
    public void setUp(){
        /// initialise all mock information
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);
        mockUser = mock(User.class);
        mockUserManager = mock(UserManager.class);

        userTest = new SqliteUserDAO();
        userTest.setConnection(mockConnection);
        mockUserManager.setInstance(mockUserManager);
        when(mockUserManager.getLoggedInUser()).thenReturn(mockUser);
    }
    @Test
    public void testLoginSuccessful() throws SQLException {
        /// ensure running the test uses the mock information
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true); /// want to pass
        when(mockResultSet.getTimestamp("lastActive")).thenReturn(Timestamp.from(Instant.now()));
        when(mockResultSet.getInt("streak")).thenReturn(0); /// mock the set activity
        when(mockStatement.executeUpdate()).thenReturn(0); /// allow the update to happen
        boolean testLogin = userTest.validateLogin("test@example.com","Correct");
        assertTrue(testLogin);
    }

    @Test
    public void testLoginNoRecord() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); /// want to fail
        boolean testLogin = userTest.validateLogin("test@example.com","Incorrect");
        assertFalse(testLogin);
    }
    @Test
    public void testLoginException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Returns Error"));
        boolean testLogin = userTest.validateLogin("test@example.com","Incorrect");
        assertFalse(testLogin);
    }

    @Test
    public void testAddSuccessful() throws SQLException {
        User test = new User("Jane", "Smith", "test@email.com", "password");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1); /// want to pass
        boolean testRego = userTest.createUser(test);
        assertTrue(testRego);
    }
    @Test
    public void testAddDupeEmail() throws SQLException {
        User test = new User("Jane", "Smith", "duplicate@email.com", "password");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenThrow(new SQLException("UNIQUE email constraint failed"));
        boolean testRego = userTest.createUser(test);
        assertFalse(testRego);
    }
    @Test
    public void testIncrementDailyActivity() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        // user last active yesterday
        Timestamp yesterday = Timestamp.valueOf(LocalDateTime.now().minusDays(1));
        userTest.updateDailyActivity(yesterday, 2, "test@example.com");

        verify(mockStatement).setInt(1, 3); // streak should increment
        verify(mockStatement).setString(2, "test@example.com");
        verify(mockStatement).executeUpdate();
    }

    @Test
    public void testResetDailyActivity() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        // user last active 2 days ago
        Timestamp twoDaysAgo = Timestamp.valueOf(LocalDateTime.now().minusDays(2));
        userTest.updateDailyActivity(twoDaysAgo, 5, "test@example.com");

        verify(mockStatement).setInt(1, 0); // streak reset
        verify(mockStatement).setString(2, "test@example.com");
        verify(mockStatement).executeUpdate();
    }

    @Test
    public void testKeepDailyActivity() throws SQLException {
        PreparedStatement spyStatement = spy(mockStatement);
        when(mockConnection.prepareStatement(anyString())).thenReturn(spyStatement);

        Timestamp now = Timestamp.valueOf(LocalDateTime.now());
        userTest.updateDailyActivity(now, 3, "test@example.com");

        verify(spyStatement, never()).executeUpdate(); // should not update for same day
    }

    @Test
    public void testCreateUserObjectFound() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("firstName")).thenReturn("John");
        when(mockResultSet.getString("lastName")).thenReturn("Doe");

        User user = userTest.createUserObject("john@doe.com", "pass123");

        assertNotNull(user);
        assertEquals("John", user.getFirstName());
        assertEquals("Doe", user.getLastName());
        assertEquals("john@doe.com", user.getEmail());
    }

    @Test
    public void testCreateUserObjectNotFound() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false);

        User user = userTest.createUserObject("john@doe.com", "wrong");
        assertNull(user);
    }

    @Test
    public void testUpdateLastPlayed() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        userTest.updateLastPlayed("test@example.com", "Game1");
        verify(mockStatement).setString(1, "Game1");
        verify(mockStatement).setString(2, "test@example.com");
        verify(mockStatement).executeUpdate();
    }

    @Test
    public void testUpdateLastPlayedThrowsException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Error"));
        assertThrows(SQLException.class, () -> {
            userTest.updateLastPlayed("test@example.com", "Game1");
        });
    }

    @Test
    public void testStoreActivity() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("streak")).thenReturn(5);

        userTest.storeActivity("test@example.com");
        verify(mockUser).setActivity(5);
    }

    @Test
    public void testStoreLastPlayed() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getString("lastPlayed")).thenReturn("GameX");

        userTest.storeLastPlayed("test@example.com");
        verify(mockUser).setLastPlayed("GameX");
    }
}