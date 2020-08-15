package com.adniyo.checkam.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import com.adniyo.checkam.entity.teamList;

import org.hibernate.Session;
import org.springframework.stereotype.Service;

@Service
public class TeamListServImp implements TeamListServ {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(rollbackOn = Exception.class)
    public List<teamList> findBySiteName(final String siteTeamName, final String siteName) {

        Session session = entityManager.unwrap(Session.class);
        // System.out.println("The Answer is : ");
        // System.out.println(session.createQuery("FROM teamList where "+ siteName+" =
        // :sn ", teamList.class)
        // .setParameter("sn", "FK Suduva Marijampole").getResultList()
        // );
        // .setParameter("sTN", siteTeamName)
        // return null;
        return session.createQuery("FROM teamList where " + siteName + " = :sn ", teamList.class)
                .setParameter("sn", siteTeamName).getResultList();

        // return session.bySimpleNaturalId(teamList.class).load(siteTeamName);
    }

    // @Override
    // @Transactional(rollbackOn = Exception.class)
    // public teamList addTeamToMaster(String siteTeamName) {

    // }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void addTeamToMaster(List<teamList> addToMaster) {
        for (final teamList teamList : addToMaster) {
            entityManager.merge(teamList);
        }

    }

}