package com.bobocode.dao;

import com.bobocode.exception.AccountDaoException;
import com.bobocode.model.Account;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import java.util.List;

public class AccountDaoImpl implements AccountDao {

  private EntityManagerFactory emf;

  public AccountDaoImpl(EntityManagerFactory emf) {
    this.emf = emf;
  }

  @Override
  public void save(Account account) {
    try (EntityManager entityManager = emf.createEntityManager()) {
      entityManager.getTransaction().begin();
      entityManager.persist(account);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      throw new AccountDaoException("", e);
    }
  }

  @Override
  public Account findById(Long id) {
    try (EntityManager entityManager = emf.createEntityManager()) {
      return entityManager.find(Account.class, id);
    } catch (Exception e) {
      throw new AccountDaoException("", e);
    }
  }

  @Override
  public Account findByEmail(String email) {
    try (EntityManager entityManager = emf.createEntityManager()) {
      String jpql = "SELECT a FROM Account a WHERE a.email = '" + email + "'";
      TypedQuery<Account> query = entityManager.createQuery(jpql, Account.class);
      return query.getSingleResult();
    } catch (Exception e) {
      throw new AccountDaoException("", e);
    }
  }

  @Override
  public List<Account> findAll() {
    try (EntityManager entityManager = emf.createEntityManager()) {
      String jpql = "SELECT a FROM Account a";
      TypedQuery<Account> query = entityManager.createQuery(jpql, Account.class);
      return query.getResultList();
    } catch (Exception e) {
      throw new AccountDaoException("", e);
    }
  }

  @Override
  public void update(Account account) {
    try (EntityManager entityManager = emf.createEntityManager()) {
      entityManager.getTransaction().begin();
      entityManager.merge(account);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      throw new AccountDaoException("", e);
    }
  }

  @Override
  public void remove(Account account) {
    try (EntityManager entityManager = emf.createEntityManager()) {
      entityManager.getTransaction().begin();
      Account account1 = entityManager.find(Account.class, account.getId());
      entityManager.remove(account1);
      entityManager.getTransaction().commit();
    } catch (Exception e) {
      throw new AccountDaoException("", e);
    }
  }
}

