package com.example.kinoteka.dto;


import java.sql.Timestamp;
import java.time.LocalDateTime;

public class CurrentSession {
   private String name;
   private int ticketCost;
   private LocalDateTime startTime;
   private int availableSeats;
   private int sessionNumber;

   public CurrentSession(){}
    public CurrentSession(String name, int ticketCost, LocalDateTime startTime, int availableSeats, int sessionNumber) {
        this.name = name;
        this.ticketCost = ticketCost;
        this.startTime = startTime;
        this.availableSeats = availableSeats;
        this.sessionNumber = sessionNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTicketCost() {
        return ticketCost;
    }

    public void setTicketCost(int ticketCost) {
        this.ticketCost = ticketCost;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public int getAvailableSeats() {
        return availableSeats;
    }

    public void setAvailableSeats(int availableSeats) {
        this.availableSeats = availableSeats;
    }

    public int getSessionNumber() {
        return sessionNumber;
    }

    public void setSessionNumber(int sessionNumber) {
        this.sessionNumber = sessionNumber;
    }
}
