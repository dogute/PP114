package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.HibernateException;
import org.hibernate.Session;

import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = Util.getSession().openSession()) {
            try {
                session.getTransaction().begin();
                String sqlComm = """
                        create table IF NOT EXISTS users(
                            id BIGINT NOT NULL AUTO_INCREMENT,
                            name varchar(30),
                            lastName varchar(30),
                            age TINYINT,
                        \t\tPRIMARY KEY (id)
                        );
                        """;
                session.createSQLQuery(sqlComm).executeUpdate();
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = Util.getSession().openSession()) {
            try {
                session.getTransaction().begin();

                session.createNativeQuery("DROP table IF EXISTS users", User.class).executeUpdate();
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = Util.getSession().getCurrentSession()) {
            try {
                session.getTransaction().begin();
                User user = new User(name, lastName, age);
                session.save(user);
                session.getTransaction().commit();
                System.out.println("User с именем " + name + " добавлен в БД");
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                e.printStackTrace();
            }
        }

    }

    @Override
    public void removeUserById(long id) {
        try (Session session = Util.getSession().openSession()) {
            try {
                session.getTransaction().begin();
                session.delete(session.get(User.class, id));
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                e.printStackTrace();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> resultList = null;
        try (Session session = Util.getSession().getCurrentSession()) {
            try {
                session.getTransaction().begin();
                resultList = session.createQuery("SELECT i from User i", User.class).getResultList();
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                e.printStackTrace();
            }
        }
        return resultList;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = Util.getSession().getCurrentSession()) {
            try {
                session.getTransaction().begin();
                session.createNativeQuery("DELETE FROM users").executeUpdate();
                session.getTransaction().commit();
            } catch (HibernateException e) {
                session.getTransaction().rollback();
                e.printStackTrace();
            }
        }
    }
}
