package ru.kata.spring.boot_security.demo.service;

import org.hibernate.query.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class UserService implements UserRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TypedQuery<User> query = em.createQuery("select u from User u left join fetch u.roles where u.username=:username", User.class);
        User user = query.setParameter("username", username).getSingleResult();
        if (user == null)
            throw new UsernameNotFoundException("Username not found!");
        return user;
    }

    @Transactional
    public void addUser(User user) {
        em.persist(user);
    }

    @Transactional
    public void removeUserById(int id) {
        Query<User> query = (Query<User>) em.createQuery("delete from User u where id = :userId");
        query.setParameter("userId", id);
        query.executeUpdate();
    }

    public List<User> getAllUsers() {
        return em.createQuery("from User", User.class).getResultList();
    }

    public User getUserById(int id) { return em.find(User.class, id); }

    @Transactional
    public void updateUser(int id, User updatedUser) { em.merge(updatedUser); }

}