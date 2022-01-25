package ru.job4j.store;

import ru.job4j.model.Account;
import ru.job4j.model.Place;
import ru.job4j.model.Ticket;
import ru.job4j.exeption.ConstrainsViolationException;

import java.util.Collection;

public interface Store {
    Ticket buyTicket(Account account, Place place) throws ConstrainsViolationException;
    Account addAccount(Account account);
    Collection<Place> hall();
    void modPlace(Place place);
    Place addPlace(Place place);
}
