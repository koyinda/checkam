package com.adniyo.checkam.service;

import java.util.List;

import com.adniyo.checkam.entity.teamList;

public interface TeamListServ {
    public void addTeamToMaster(List<teamList> addToMaster);

    public List<teamList> findBySiteName(String siteTeamName, String siteName);
}