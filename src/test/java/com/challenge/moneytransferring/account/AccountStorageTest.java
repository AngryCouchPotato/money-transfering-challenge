package com.challenge.moneytransferring.account;

import com.challenge.moneytransferring.exception.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class AccountStorageTest {

    private AccountStorage accountStorage;

    @BeforeEach
    void setUp() {
        this.accountStorage = new AccountStorage();
    }

    @Test
    void shouldCreateAcount() {
        // Given
        String number = "Second";

        // When
        Account account = accountStorage.create(number);

        // Then
        assertNotNull(account);
        assertEquals(2, account.getId());
        assertEquals(number, account.getNumber());
    }

    @Test
    void shouldFindNullableOptional() {
        // Given
        long id = 777l;

        // When
        Optional<Account> accountOpt = accountStorage.find(id);

        // Then
        assertNotNull(accountOpt);
        assertFalse(accountOpt.isPresent());
    }

    @Test
    void shouldFindOptional() {
        // Given
        String number = "Second";
        long id = 2L;

        // When
        accountStorage.create(number);
        Optional<Account> accountOpt = accountStorage.find(id);

        // Then
        assertNotNull(accountOpt);
        assertNotNull(accountOpt.get());
        assertEquals(number, accountOpt.get().getNumber());
    }

    @Test
    void shouldGetAccount() {
        // Given
        String number = "Second";
        long id = 2L;

        // When
        accountStorage.create(number);
        Account account = accountStorage.get(id);

        // Then
        assertNotNull(account);
        assertEquals(id, account.getId());
        assertEquals(number, account.getNumber());
    }

    @Test()
    void shouldFailGetAccount() {
        // Given
        long id = 777l;

        // When
        Throwable exception = assertThrows(EntityNotFoundException.class, () -> accountStorage.get(id));

        // Then
        assertEquals("Could not find Entity with id = 777", exception.getMessage());
    }

    @Test
    void shouldGetAllAccounts() {
        // Given
        String number = "Second";
        long id = 2L;

        // When
        accountStorage.create(number);
        List<Account> accounts = accountStorage.getAll();

        // Then
        assertNotNull(accounts);
        assertEquals(2, accounts.size());
        assertEquals("Base account", accounts.get(0).getNumber());
        assertEquals(number, accounts.get(1).getNumber());
    }

    @Test
    void shouldPutAccount() {
        // Given
        String number = "Third";
        long id = 3L;

        // When
        accountStorage.put(id, new Account(id, number));
        Account account = accountStorage.get(id);

        // Then
        assertNotNull(account);
        assertEquals(id, account.getId());
        assertEquals(number, account.getNumber());
    }
}