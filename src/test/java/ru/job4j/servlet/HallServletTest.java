package ru.job4j.servlet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.Test;
import ru.job4j.exeption.ConstrainsViolationException;
import ru.job4j.model.Account;
import ru.job4j.model.Place;
import ru.job4j.model.Ticket;
import ru.job4j.store.DbStore;
import ru.job4j.store.Store;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HallServletTest {

    private static Store store = DbStore.instOff();
    private static final Gson GSON = new GsonBuilder().create();

    @Test
    public void whenCreateAccount() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("username")).thenReturn("john");
        when(req.getParameter("email")).thenReturn("dd@ss.com");
        when(req.getParameter("phone")).thenReturn("555");

        Account account = store.addAccount(new Account(req.getParameter("username"),
                req.getParameter("email"), req.getParameter("phone")));
        assertThat(account, notNullValue());
    }

    @Test
    public void whenBuyTicket() {
        HttpServletRequest req = mock(HttpServletRequest.class);
        HttpServletResponse resp = mock(HttpServletResponse.class);
        when(req.getParameter("username")).thenReturn("john");
        when(req.getParameter("email")).thenReturn("dd@ss.com");
        when(req.getParameter("phone")).thenReturn("555");
        when(req.getParameter("place")).thenReturn("{\"id\":2,\"row\":1,\"cell\":2,\"session\":1,\"available\":true}");

        Account account = store.addAccount(new Account(req.getParameter("username"),
                req.getParameter("email"), req.getParameter("phone")));
        Place place = GSON.fromJson(req.getParameter("place"), Place.class);
        Ticket ticket = null;
        try {
            ticket = store.buyTicket(account, place);
        } catch (ConstrainsViolationException e) {
            e.printStackTrace();
        }
        assertThat(ticket, notNullValue());
    }

}