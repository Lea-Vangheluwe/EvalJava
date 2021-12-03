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

    //    static EntityManager em = emf.createEntityManager();

    public static void enrollUser(UberUser uberUser){
        EntityManager em = EntityManagerFactorySingleton
                .getInstance().createEntityManager();
        em.getTransaction().begin();
        em.persist(uberUser);
        em.getTransaction().commit();
        em.close();
    }

    public static void enrollDriver(UberDriver uberDriver){
        EntityManager em = EntityManagerFactorySingleton
                .getInstance().createEntityManager();
        em.getTransaction().begin();
        em.persist(uberDriver);
        em.getTransaction().commit();
        em.close();
    }

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
