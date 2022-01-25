package ru.job4j.servlet;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.job4j.exeption.ConstrainsViolationException;
import ru.job4j.model.Account;
import ru.job4j.model.Place;
import ru.job4j.store.DbStore;
import ru.job4j.store.Store;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

public class HallServlet extends HttpServlet {

    private static final Gson GSON = new GsonBuilder().create();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Store store = DbStore.instOff();
        Collection<Place> hall = store.hall();
        resp.setContentType("application/json; charset=utf-8");
        OutputStream output = resp.getOutputStream();
        String json = GSON.toJson(hall);
        output.write(json.getBytes(StandardCharsets.UTF_8));
        output.flush();
        output.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Store store = DbStore.instOff();
        Account account = new Account(req.getParameter("username"),
                req.getParameter("email"), req.getParameter("phone"));
        Place place = GSON.fromJson(req.getParameter("place"), Place.class);
        store.addAccount(account);
        try {
            store.buyTicket(account, place);
        } catch (ConstrainsViolationException e) {
            e.printStackTrace();
            resp.sendRedirect("http://localhost:8080/job4j_cinema/error");
        }
        store.modPlace(place);
        resp.sendRedirect("http://localhost:8080/job4j_cinema");
    }
}
