package com.adniyo.checkam.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.adniyo.checkam.entity.bet9ja_gameGroup;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

/**
 * gamegroupImpl
 */

@Service
public class GameGroupServImpl implements GameGroupServ {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void save(List<bet9ja_gameGroup> b) {
        for (bet9ja_gameGroup bGameGroup : b) {
            entityManager.merge(bGameGroup);
        }
    }

    @Override
    @Transactional
    public void update(bet9ja_gameGroup b) {
        // TODO Auto-generated method stub

    }

    @Override
    @Transactional
    public bet9ja_gameGroup findByGameId(long gameId) {
        Session session = entityManager.unwrap(Session.class);
        return session.bySimpleNaturalId(bet9ja_gameGroup.class).load(gameId);
    }

    @Override
    public List<bet9ja_gameGroup> findAll() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery(" FROM bet9ja_gameGroup", bet9ja_gameGroup.class).getResultList();

        //return session.unwrap(bet9ja_gameGroup.class);
    }

}