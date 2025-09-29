import com.example.FACT.model.*;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.Instant;
/// done by summer n11187450
import static org.junit.jupiter.api.Assertions.*;
/// use to create mock data -> safer than testing using an actual database.
import static org.mockito.Mockito.*;
public class SQLiteUserTest {

    private Connection mockConnection;
    private PreparedStatement mockStatement;
    private ResultSet mockResultSet;
    private SqliteUserDAO userTest;

    @BeforeEach
    public void setUp(){
        /// initialise all mock information
        mockConnection = mock(Connection.class);
        mockStatement = mock(PreparedStatement.class);
        mockResultSet = mock(ResultSet.class);

        userTest = new SqliteUserDAO();
        userTest.setConnection(mockConnection);
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
        boolean testLogin = userTest.isLogin("test@example.com","Correct");
        assertTrue(testLogin);
    }

    @Test
    public void testLoginNoRecord() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(false); /// want to fail
        boolean testLogin = userTest.isLogin("test@example.com","Incorrect");
        assertFalse(testLogin);
    }
    @Test
    public void testLoginException() throws SQLException {
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException("Returns Error"));
        boolean testLogin = userTest.isLogin("test@example.com","Incorrect");
        assertFalse(testLogin);
    }

    @Test
    public void testAddSuccessful() throws SQLException {
        User test = new User("Jane", "Smith", "test@email.com", "password");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenReturn(1); /// want to pass
        boolean testRego = userTest.addUser(test);
        assertTrue(testRego);
    }
    @Test
    public void testAddDupeEmail() throws SQLException {
        User test = new User("Jane", "Smith", "duplicate@email.com", "password");
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeUpdate()).thenThrow(new SQLException("UNIQUE email constraint failed"));
        boolean testRego = userTest.addUser(test);
        assertFalse(testRego);
    }
}