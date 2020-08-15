package com.adniyo.checkam.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.adniyo.checkam.entity.bet9ja;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

@Service
public class GameListServImpl implements GameListServ {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void update(bet9ja b) {
        // TODO Auto-generated method stub

    }

    @Override
    @Transactional
    public bet9ja findById(long bet9jaIndex) {

        Session session = entityManager.unwrap(Session.class);
        return session.bySimpleNaturalId(bet9ja.class).load(bet9jaIndex);
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void save(List<bet9ja> b) {
        // TODO Auto-generated method stub

        for (bet9ja bet9ja : b) {
            entityManager.merge(bet9ja);
        }

    }

}