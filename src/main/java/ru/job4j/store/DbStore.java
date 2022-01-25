package ru.job4j.store;

import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.job4j.model.Account;
import ru.job4j.model.Place;
import ru.job4j.model.Ticket;
import ru.job4j.exeption.ConstrainsViolationException;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class DbStore implements Store {
    private final BasicDataSource pool = new BasicDataSource();
    private final static Logger LOG = LoggerFactory.getLogger(DbStore.class.getName());

    public DbStore() {
        Properties cfg = new Properties();
        try (BufferedReader io = new BufferedReader(
                new InputStreamReader(
                        DbStore.class.getClassLoader()
                                .getResourceAsStream("db.properties")
                )
        )) {
            cfg.load(io);
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        try {
            Class.forName(cfg.getProperty("jdbc.driver"));
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
        pool.setDriverClassName(cfg.getProperty("jdbc.driver"));
        pool.setUrl(cfg.getProperty("jdbc.url"));
        pool.setUsername(cfg.getProperty("jdbc.username"));
        pool.setPassword(cfg.getProperty("jdbc.password"));
        pool.setMinIdle(5);
        pool.setMaxIdle(10);
        pool.setMaxOpenPreparedStatements(100);
    }

    private static final class Lazy {
        private static final Store INST = new DbStore();
    }

    public static Store instOff() {
        return Lazy.INST;
    }

    @Override
    public Ticket buyTicket(Account account, Place place) throws ConstrainsViolationException {
        Ticket ticket = new Ticket(place.getSession(), place.getRow(), place.getCell(), account.getId());
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("insert into ticket (session_id, rowplace, cell, account_id) values (?, ?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, place.getSession());
            ps.setInt(2, place.getRow());
            ps.setInt(3, place.getCell());
            ps.setInt(4, account.getId());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    ticket.setId(id.getInt(1));
                }
            }
        } catch (SQLException se) {
            if (se.getMessage().contains("Нарушение уникального индекса")) {
                    throw new ConstrainsViolationException();

            }
        }
        return ticket;
    }

    @Override
    public Account addAccount(Account account) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("INSERT INTO account(username, email, phone) VALUES (?, ?, ?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, account.getName());
            ps.setString(2, account.getEmail());
            ps.setString(3, account.getPhone());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    account.setId(id.getInt(1));
                }

            }
        } catch (Exception e) {
            LOG.error("Error add account", e);
        }
        return account;
    }

    @Override
    public Collection<Place> hall() {
        Collection<Place> allTicket = new ArrayList<>();
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("select * from hall ORDER BY id")) {
            try (ResultSet it = ps.executeQuery()) {
                while (it.next()) {
                    allTicket.add(new Place(it.getInt("id"),
                            it.getInt("rowplace"),
                            it.getInt("cell"),
                            it.getInt("session_id"),
                            it.getBoolean("available")));
                }
            }
        } catch (Exception e) {
            LOG.error("Error connect to table 'hall'", e);
        }
        return allTicket;
    }

    @Override
    public void modPlace(Place place) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("update hall set available = false where rowplace = ? and cell = ? and session_id = ?")) {
            ps.setInt(1, place.getRow());
            ps.setInt(2, place.getCell());
            ps.setInt(3, place.getSession());
            ps.execute();
        } catch (Exception e) {
            LOG.error("Error connect to table 'hall'", e);
        }
    }

    @Override
    public Place addPlace(Place place) {
        try (Connection cn = pool.getConnection();
             PreparedStatement ps = cn.prepareStatement("insert into hall(rowplace, cell, session_id, available) values(?, ? ,? ,?)",
                     PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, place.getRow());
            ps.setInt(2, place.getCell());
            ps.setInt(3, place.getSession());
            ps.setBoolean(4, place.getAvailable());
            ps.execute();
            try (ResultSet id = ps.getGeneratedKeys()) {
                if (id.next()) {
                    place.setId(id.getInt(1));
                }
            }
        } catch (Exception e) {
            LOG.error("Error connect to table 'hall'", e);
        }
        return place;
    }
}
