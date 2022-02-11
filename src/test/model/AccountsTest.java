package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AccountsTest {

    private Accounts accounts;
    private static final Account ACC1 = new Account("tom","123","Tommy");
    private static final Account ACC2 = new Account("may","456","June");

    @BeforeEach
    void runBefore() {
        accounts = new Accounts();
    }

    @Test
    void testConstructor() {
        assertEquals(0,accounts.size());
        assertTrue(accounts.isEmpty());
    }

    @Test
    void testAddAccount() {
        accounts.addAccount(ACC1);
        accounts.addAccount(ACC2);
        assertEquals(2,accounts.size());
        assertEquals(ACC1,accounts.getAccount(0));
        assertEquals(ACC2,accounts.getAccount(1));
    }

    @Test
    void testIsUsernameUsedAlreadyTrue() {
        accounts.addAccount(ACC1);
        accounts.addAccount(ACC2);
        assertTrue(accounts.isUsernameUsedAlready("may"));
    }

    @Test
    void testIsUsernameUsedAlreadyFalse() {
        accounts.addAccount(ACC1);
        accounts.addAccount(ACC2);
        assertFalse(accounts.isUsernameUsedAlready("Rachmaninov"));
    }

    @Test
    void testGetAccountNullWrongPassword() {
        accounts.addAccount(ACC1);
        accounts.addAccount(ACC2);
        assertEquals(null,accounts.getAccount("may","789"));
    }

    @Test
    void testGetAccountNullWrongUsername() {
        accounts.addAccount(ACC1);
        accounts.addAccount(ACC2);
        assertEquals(null,accounts.getAccount("jun","456"));
    }

    @Test
    void testGetAccountNullAllWrong() {
        accounts.addAccount(ACC1);
        accounts.addAccount(ACC2);
        assertEquals(null,accounts.getAccount("jun","789"));
    }

    @Test
    void testGetAccountSuccess() {
        accounts.addAccount(ACC1);
        accounts.addAccount(ACC2);
        assertEquals(ACC2,accounts.getAccount("may","456"));
    }

}
