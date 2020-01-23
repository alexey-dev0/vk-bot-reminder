package com.example.testbot.dao;

import com.example.testbot.models.Reminder;
import com.example.testbot.utils.HibernateSessionFactoryUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ReminderDAO {
    public Reminder findById(int id) {
        return HibernateSessionFactoryUtil.getSessionFactory()
                .openSession()
                .get(Reminder.class, id);
    }

    public void save(Reminder reminder) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.save(reminder);
        tx1.commit();
        session.close();
    }

    public void update(Reminder reminder) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.update(reminder);
        tx1.commit();
        session.close();
    }

    public void delete(Reminder reminder) {
        Session session = HibernateSessionFactoryUtil.getSessionFactory().openSession();
        Transaction tx1 = session.beginTransaction();
        session.delete(reminder);
        tx1.commit();
        session.close();
    }

    @SuppressWarnings("unchecked")
    public List<Reminder> findAll() {
        return (List<Reminder>) HibernateSessionFactoryUtil.getSessionFactory()
                .openSession()
                .createQuery("From Reminder")
                .list();
    }
}
