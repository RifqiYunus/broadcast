package com.iconkalbar.broadcast.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PmSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    private SitePOP sitePop;

    @ManyToOne
    private RecipientNumber recipient;

    private Date scheduledDate;

    private Date realizationDate;

    private boolean isMaintenanceDone;

    private int reminderSentTimes;

    @UpdateTimestamp
    private Date updatedDate;

    @CreationTimestamp
    private Date createdDate;
}
