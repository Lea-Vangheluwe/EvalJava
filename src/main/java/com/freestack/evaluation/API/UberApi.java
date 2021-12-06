package com.freestack.evaluation.API;

import com.freestack.evaluation.booking.Booking;
import com.freestack.evaluation.driver.UberDriver;
import com.freestack.evaluation.persistence.EntityManagerFactorySingleton;
import com.freestack.evaluation.user.UberUser;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class UberApi {

    /**
     * Register/enroll a user. Take a User as a paramater
     * @param uberUser
     */
    public static void enrollUser(UberUser uberUser){
        EntityManager em = EntityManagerFactorySingleton
                .getInstance().createEntityManager();
        em.getTransaction().begin();
        em.persist(uberUser);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Register/enroll a Driver. Take a UserDriver as a parameter
     * @param uberDriver
     */
    public static void enrollDriver(UberDriver uberDriver){
        EntityManager em = EntityManagerFactorySingleton
                .getInstance().createEntityManager();
        em.getTransaction().begin();
        em.persist(uberDriver);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Allow a user to book a course. Check if there's an available driver. If so, create a new booking wit a driver, a user and a starting date.
     * Set the driver to "unavailable"
     * @param uberUser
     * @return the created booking
     */
    public static Booking bookOneDriver(UberUser uberUser) {
        EntityManager em = EntityManagerFactorySingleton
                .getInstance().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("select COUNT(d) from UberDriver d where d.available = true");
        Long nbrOfAvailableDrivers = (Long) query.getSingleResult();
        if (nbrOfAvailableDrivers > 0) {
            Query query2 = em.createQuery("select d from UberDriver d where d.available = true");
            UberDriver driver = (UberDriver) query2.setMaxResults(1).getSingleResult();
            Booking booking = new Booking();
            booking.setDriver(driver);
            booking.setUser(uberUser);
            booking.setStartOfTheBooking(Instant.now());
            driver.setAvailable(false);
            em.persist(booking);
            em.getTransaction().commit();
            em.close();
            return booking;
        }
        else{
            return null;
        }
    }

    /**
     * CLose a booking. Set an ending date. Set the driver to "available"
     * @param booking
     */
    public static void finishBooking(Booking booking){
        EntityManager em = EntityManagerFactorySingleton
                .getInstance().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("select b from Booking b where b.id = :id");
        query.setParameter("id", booking.getId());
        Booking bookingToCommit = (Booking)query.getSingleResult();
        bookingToCommit.setEndOfTheBooking(Instant.now());
        bookingToCommit.getDriver().setAvailable(true);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Allow a user to evaluate a driver.
     * @param booking
     * @param evaluation
     */
    public static void evaluateDriver(Booking booking, Integer evaluation){
        EntityManager em = EntityManagerFactorySingleton
                .getInstance().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("select b from Booking b where b.id = :id");
        query.setParameter("id", booking.getId());
        Booking bookingToCommit = (Booking)query.getSingleResult();
        bookingToCommit.setScore(evaluation);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Get all Bookings (current and past) of a giver driver
     * @param uberDriver
     * @return a list of bookings
     */
    public static List<Booking> listDriverBookings (UberDriver uberDriver){
        List<Booking> bookings = new ArrayList<>();
        EntityManager em = EntityManagerFactorySingleton
                .getInstance().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("select b from Booking b where b.driver = :driver");
        query.setParameter("driver", uberDriver);
        bookings = query.getResultList();
        em.getTransaction().commit();
        em.close();
        return bookings;
    }

    /**
     * Get all the ongoing bookings
     * @return a list of bookings
     */
    public static List<Booking> listUnfinishedBookings (){
        List<Booking> bookings = new ArrayList<>();
        EntityManager em = EntityManagerFactorySingleton
                .getInstance().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("select b from Booking b where b.endOfTheBooking IS NULL");
        bookings = query.getResultList();
        em.getTransaction().commit();
        em.close();
        return bookings;
    }

    /**
     * Calculate the average score of a driver
     * @param uberDriver
     * @return the average score of a driver
     */
    public static Float meanScore(UberDriver uberDriver){
        float meanScore = 0F;
        Double meanScoreLong = 0D;
        List<Booking> bookings = new ArrayList<>();
        EntityManager em = EntityManagerFactorySingleton
                .getInstance().createEntityManager();
        em.getTransaction().begin();
        Query query = em.createQuery("select AVG(b.score) from Booking b where b.driver = :driver");
        query.setParameter("driver", uberDriver);
        meanScoreLong = (Double) query.getSingleResult();
        meanScore = meanScoreLong.floatValue();
        em.getTransaction().commit();
        em.close();
        return meanScore;
    }



}
