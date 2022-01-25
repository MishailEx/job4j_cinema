package ru.job4j.model;

public class Ticket {
    private int id;
    private int row;
    private int cell;
    private int sessionId;
    private int owner;

    public Ticket() {
    }

    public Ticket(int id, int sessionId, int row, int cell, int owner) {
        this.id = id;
        this.sessionId = sessionId;
        this.row = row;
        this.cell = cell;
                this.owner = owner;
    }

    public Ticket(int sessionId, int row, int cell, int owner) {
        this.row = row;
        this.cell = cell;
        this.sessionId = sessionId;
        this.owner = owner;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getSessionId() {
        return sessionId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getOwner() {
        return owner;
    }

    public void setOwner(int owner) {
        this.owner = owner;
    }
}
