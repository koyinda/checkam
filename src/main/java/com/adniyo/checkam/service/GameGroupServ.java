package com.adniyo.checkam.service;

import java.util.List;

import com.adniyo.checkam.entity.bet9ja_gameGroup;
public interface GameGroupServ {
    public void save(List<bet9ja_gameGroup> b);

    public void update(bet9ja_gameGroup b);

    public bet9ja_gameGroup findByGameId(long gameId);

    public List<bet9ja_gameGroup> findAll();

}