package com.adniyo.checkam.service;

import java.util.List;

import com.adniyo.checkam.entity.bet9ja;

public interface GameListServ {
    public void save(List<bet9ja> b);

    public void update(bet9ja b);

    public bet9ja findById(long bet9jaIndex);
}