package com.exadel.training.dao.impl;

import com.exadel.training.dao.AttendanceDAO;
import com.exadel.training.dao.domain.Attendance;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

/**
 * Created by ayudovin on 06.10.2015.
 */
@Repository
public class AttendanceDAOImpl implements AttendanceDAO {

    @Autowired
    SessionFactory sessionFactory;

    @Override
    public void save(Attendance attendance) {
        Session session = sessionFactory.getCurrentSession();
        session.save(attendance);
    }

    @Override
    public void delete(Attendance attendance) {
        Session session = sessionFactory.getCurrentSession();
        session.delete(attendance);
    }

    @Override
    public void update(Attendance attendance) {
        Session session = sessionFactory.getCurrentSession();
        session.update(attendance);
    }

    @Override
    public Attendance getAttendanceByID(long id) {
        return sessionFactory.getCurrentSession()
                .load(Attendance.class, id);
    }

    @Override
    public List<Attendance> getAllAttendanceByUserIDBetweenDates(long idUser, Date from, Date to) {
        Session session = sessionFactory.getCurrentSession();
        Criteria criteria = session.createCriteria(Attendance.class, "attendance");

        criteria.createAlias("attendance.user", "user");
        criteria.add(Restrictions.eq("user.id", idUser));

        criteria.createAlias("attendance.lesson", "lesson");
        criteria.add(Restrictions.gt("lesson.date", from.getTime()));
        criteria.add(Restrictions.lt("lesson.date", to.getTime()));

        return criteria.list();
    }

}
