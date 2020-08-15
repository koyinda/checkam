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
@Table(name = "bet9ja_gamegroup")
@EntityListeners(AuditingEntityListener.class)
public class bet9ja_gameGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long idbet9ja_gamegroup;

    @Column(name = "competitionBody", nullable = false)
    private String competitionBody;

    @Column(name = "competitionName", nullable = false)
    private String competitionName;

    @NaturalId(mutable = false)
    @Column(name = "competitionId", nullable = false, unique = true, updatable = false)
    private long competitionId;

    @Column(name = "lastUpdateTime", nullable = false)
    private LocalDateTime lastUpdateTime;


    //the annotation solved the error but i dont understand
    @Tolerate
    public bet9ja_gameGroup() {
        
        // the content is rubbish
        this.idbet9ja_gamegroup = 1;
        this.competitionBody = "competitionBody";
        this.competitionName = "competitionName";
        this.competitionId = 2;
        this.lastUpdateTime = LocalDateTime.now();
    }

}