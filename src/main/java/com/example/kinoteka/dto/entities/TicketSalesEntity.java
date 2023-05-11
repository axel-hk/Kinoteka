package com.example.kinoteka.dto.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.sql.Timestamp;

@Entity
@Table(name = "ticket_sales", schema = "public", catalog = "kino")
public class TicketSalesEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "sale_id")
    private int saleId;
    @Basic
    @Column(name = "session_id")
    @NotNull
    private Integer sessionId;
    @Basic
    @Column(name = "sale_time")
    private Timestamp saleTime;
    @Basic
    @Column(name = "num_tickets")
    private int numTickets;
    @Basic
    @Column(name = "price")
    private int price;
    @ManyToOne
    @JoinColumn(name = "session_id", referencedColumnName = "session_id", insertable=false, updatable=false)
    private SessionsEntity sessionsBySessionId;

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public Integer getSessionId() {
        return sessionId;
    }

    public void setSessionId(Integer sessionId) {
        this.sessionId = sessionId;
    }

    public Timestamp getSaleTime() {
        return saleTime;
    }

    public void setSaleTime(Timestamp saleTime) {
        this.saleTime = saleTime;
    }

    public int getNumTickets() {
        return numTickets;
    }

    public void setNumTickets(int numTickets) {
        this.numTickets = numTickets;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketSalesEntity that = (TicketSalesEntity) o;

        if (saleId != that.saleId) return false;
        if (numTickets != that.numTickets) return false;
        if (price != that.price) return false;
        if (sessionId != null ? !sessionId.equals(that.sessionId) : that.sessionId != null) return false;
        if (saleTime != null ? !saleTime.equals(that.saleTime) : that.saleTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = saleId;
        result = 31 * result + (sessionId != null ? sessionId.hashCode() : 0);
        result = 31 * result + (saleTime != null ? saleTime.hashCode() : 0);
        result = 31 * result + numTickets;
        result = 31 * result + price;
        return result;
    }

    public SessionsEntity getSessionsBySessionId() {
        return sessionsBySessionId;
    }

    public void setSessionsBySessionId(SessionsEntity sessionsBySessionId) {
        this.sessionsBySessionId = sessionsBySessionId;
    }
}
