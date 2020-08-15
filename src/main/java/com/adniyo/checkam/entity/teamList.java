package com.adniyo.checkam.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Tolerate;

@Value
@Builder
@Entity
@Table(name = "teamList", indexes = { 
        @Index(columnList = "teamName", name = "teamName_hidx"),
        @Index(columnList = "bet9jaName", name = "bet9jaName_hidx") })

@EntityListeners(AuditingEntityListener.class)
public class teamList {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long teamListId;

    @Column(name = "teamName", nullable = false, unique = true, updatable = false)
    private String teamName;

    @NaturalId(mutable = false)
    @Column(name = "bet9jaName", nullable = false, unique = true, updatable = false, length = 100)
    private String bet9jaName;

    // the annotation solved the error but i dont understand
    @Tolerate
    public teamList() {

        // the content is rubbish
        this.teamListId = 1;
        this.teamName = "competitionBody";
        this.bet9jaName = "competitionName";
    }

}