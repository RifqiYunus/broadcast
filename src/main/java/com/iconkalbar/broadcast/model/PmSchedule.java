package com.iconkalbar.broadcast.model;

import java.util.Date;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Builder
@AllArgsConstructor
public class PmSchedule {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private SitePOP sitePop;

    private RecipientNumber recipient;

    private Date scheduledDate;

    private Date realizationDate;

    private boolean isReminderSent;

    @UpdateTimestamp
    private Date updatedDate;

    @CreationTimestamp
    private Date createdDate;
}
