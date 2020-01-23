package com.example.testbot.models;

import javax.persistence.*;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminders")
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "creation_date")
    private Timestamp creationDate;

    @Column(name = "appointment_date")
    private Timestamp appointmentDate;

    @Column(name = "interval_date")
    private Timestamp intervalDate;

    private boolean regular;

    @Column(name = "user_id")
    private int userId;

    private String message;

    private Reminder() {
    }

    public Reminder(Timestamp appointmentDate, int userId, String message) {
        this.appointmentDate = appointmentDate;
        this.creationDate = Timestamp.valueOf(LocalDateTime.now());
        this.userId = userId;
        this.message = message;
        this.regular = false;
    }

    public int getId() {
        return id;
    }

    public Timestamp getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Timestamp creationDate) {
        this.creationDate = creationDate;
    }

    public Timestamp getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(Timestamp appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public Timestamp getIntervalDate() {
        return intervalDate;
    }

    public void setIntervalDate(Timestamp intervalDate) {
        this.intervalDate = intervalDate;
    }

    public boolean isRegular() {
        return regular;
    }

    public void setRegular(boolean regular) {
        this.regular = regular;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return String.format("%s[%tc]->[%tc]: %s", userId, creationDate, appointmentDate, message);
    }
}
