package ru.job4j.model;

import java.util.Objects;

public class Place {
    private int id;
    private int row;
    private int cell;
    private int session;
    private boolean available;

    public Place(int id, int row, int cell, int session, boolean available) {
        this.id = id;
        this.row = row;
        this.cell = cell;
        this.session = session;
        this.available = available;
    }

    public Place(int row, int cell, int session, boolean available) {
        this.row = row;
        this.cell = cell;
        this.session = session;
        this.available = available;
    }

    public int getSession() {
        return session;
    }

    public void setSession(int session) {
        this.session = session;
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public boolean getAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Place place = (Place) o;
        return id == place.id && row == place.row && cell == place.cell && session == place.session && available == place.available;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, row, cell, session, available);
    }
}
