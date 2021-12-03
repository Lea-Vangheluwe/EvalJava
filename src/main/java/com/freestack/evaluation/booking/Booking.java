package com.freestack.evaluation.booking;

import com.freestack.evaluation.driver.UberDriver;
import com.freestack.evaluation.user.UberUser;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table (name="booking")
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;
    @Column (name = "end_of_the_booking", columnDefinition = "TIMESTAMP WITH TIME ZONE")
    Instant endOfTheBooking;
    @Column (name = "start_of_the_booking",columnDefinition = "TIMESTAMP WITH TIME ZONE")
    Instant startOfTheBooking;
    @Column(name = "evaluation")
    Integer score;
    @ManyToOne
    @JoinColumn(name="driver_id")
    UberDriver driver;
    @ManyToOne
    @JoinColumn(name="user_id")
    UberUser user;

    public Booking() {
    }

    public Integer getId() {
        return id;
    }

    public Instant getEndOfTheBooking() {
        return endOfTheBooking;
    }

    public void setEndOfTheBooking(Instant endOfTheBooking) {
        this.endOfTheBooking = endOfTheBooking;
    }

    public Instant getStartOfTheBooking() {
        return startOfTheBooking;
    }

    public void setStartOfTheBooking(Instant startOfTheBooking) {
        this.startOfTheBooking = startOfTheBooking;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public UberDriver getDriver() {
        return driver;
    }

    public void setDriver(UberDriver driver) {
        this.driver = driver;
    }

    public UberUser getUser() {
        return user;
    }

    public void setUser(UberUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Booking{" +
                "id=" + id +
                ", endOfTheBooking=" + endOfTheBooking +
                ", startOfTheBooking=" + startOfTheBooking +
                ", score=" + score +
                ", driver=" + driver +
                ", user=" + user +
                '}';
    }
}
