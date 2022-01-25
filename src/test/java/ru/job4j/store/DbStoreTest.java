package ru.job4j.store;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.model.Account;
import ru.job4j.model.Place;
import ru.job4j.model.Ticket;
import ru.job4j.exeption.ConstrainsViolationException;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Properties;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DbStoreTest {
    static Connection connection;

    @BeforeClass
    public static void initConnection() {
        try (InputStream in = DbStore.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties config = new Properties();
            config.load(in);
            Class.forName(config.getProperty("jdbc.driver"));
            connection = DriverManager.getConnection(
                    config.getProperty("jdbc.url"),
                    config.getProperty("jdbc.username"),
                    config.getProperty("jdbc.password")
            );
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    @AfterClass
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @After
    public void wipeTable() throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("DELETE FROM ticket;"
                + " ALTER TABLE ticket ALTER COLUMN id RESTART WITH 1;"
                + " DELETE FROM account; ALTER TABLE account ALTER COLUMN id RESTART WITH 1;"
                + " DELETE FROM hall; ALTER TABLE hall ALTER COLUMN id RESTART WITH 1")) {
            statement.execute();
        }
    }

    @Test
    public void whenShowAllTicket() {
        Store store = DbStore.instOff();
        Place place = new Place(1, 1, 1, 1, true);
        Place place2 = new Place(2, 1, 2, 1, true);
        store.addPlace(place);
        store.addPlace(place2);
        Collection<Place> allPlace = List.of(place, place2);
        assertThat(store.hall(), is(allPlace));
    }

    @Test
    public void whenBuyTicket() throws ConstrainsViolationException {
        Store store = DbStore.instOff();
        Account account = store.addAccount(new Account("john", "aa@aa.com", "555"));
        Place place = store.addPlace(new Place(1, 2, 1, true));
        Ticket ticket = store.buyTicket(account, place);
        assertThat(ticket.getId(), is(account.getId()));
    }

    @Test
    public void whenAddAccount() {
        Store store = DbStore.instOff();
        Account account = store.addAccount(new Account("john", "aa@aa.com", "555"));
        assertThat(account.getId(), is(1));
    }

    @Test (expected = ConstrainsViolationException.class)
    public void whenBuyTicketSimultaneously() throws ConstrainsViolationException {
        Store store = DbStore.instOff();
        Account account = store.addAccount(new Account("john", "aa@aa.com", "555"));
        Account account2 = store.addAccount(new Account("john2", "bb@bb.com", "444"));
        Place place = store.addPlace(new Place(1, 2, 1, true));
        Ticket ticket = store.buyTicket(account, place);
        Ticket ticket2 = store.buyTicket(account2, place);
    }
}