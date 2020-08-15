package com.adniyo.checkam.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Tolerate;

@Value
@Builder
@Entity
@Table(name = "bet9ja_soccer")
@EntityListeners(AuditingEntityListener.class)
public class bet9ja {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long gameIndex;

    @NaturalId(mutable = false)
    @Column(name = "bet9jaIndex", nullable = false, unique = true, updatable = false)
    private long bet9jaIndex;

    @Column(name = "homeTeam_id", nullable = false)
    private long homeTeam_id;

    @Column(name = "awayTeam_id", nullable = false)
    private long awayTeam_id;

    @Column(name = "matchTime", nullable = false)
    private LocalDateTime matchTime;

    @Column(name = "homeWin", nullable = false)
    private double homeWin;

    @Column(name = "draw", nullable = false)
    private double draw;

    @Column(name = "awayWin", nullable = false)
    private double awayWin;

    @Column(name = "homeWinOrDraw", nullable = false)
    private double homeWinOrDraw;

    @Column(name = "homeWinOrAwayWin", nullable = false)
    private double homeWinOrAwayWin;

    @Column(name = "awayWinOrDraw", nullable = false)
    private double awayWinOrDraw;

    @Column(name = "over2Goals", nullable = false)
    private double over2Goals;

    @Column(name = "under3Goals", nullable = false)
    private double under3Goals;

    @Column(name = "homeWinCode", nullable = false, updatable = false)
    private double homeWinCode;

    @Column(name = "drawCode", nullable = false, updatable = false)
    private double drawCode;

    @Column(name = "awayWinCode", nullable = false, updatable = false)
    private double awayWinCode;

    @Column(name = "homeWinOrDrawCode", nullable = false, updatable = false)
    private double homeWinOrDrawCode;

    @Column(name = "homeWinOrAwayWinCode", nullable = false, updatable = false)
    private double homeWinOrAwayWinCode;

    @Column(name = "awayWinOrDrawCode", nullable = false, updatable = false)
    private double awayWinOrDrawCode;

    @Column(name = "over2GoalsCode", nullable = false, updatable = false)
    private double over2GoalsCode;

    @Column(name = "under3GoalsCode", nullable = false, updatable = false)
    private double under3GoalsCode;

    @Tolerate
    public bet9ja() {
        this.gameIndex = 0;
        this.bet9jaIndex = 0;
        this.homeTeam_id = 0;
        this.awayTeam_id = 0;
        this.matchTime = null;
        this.homeWin = 0;
        this.draw = 0;
        this.awayWin = 0;
        this.homeWinOrDraw = 0;
        this.homeWinOrAwayWin = 0;
        this.awayWinOrDraw = 0;
        this.over2Goals = 0;
        this.under3Goals = 0;
        this.homeWinCode = 0;
        this.drawCode = 0;
        this.awayWinCode = 0;
        this.homeWinOrDrawCode = 0;
        this.homeWinOrAwayWinCode = 0;
        this.awayWinOrDrawCode = 0;
        this.over2GoalsCode = 0;
        this.under3GoalsCode = 0;
    }

}