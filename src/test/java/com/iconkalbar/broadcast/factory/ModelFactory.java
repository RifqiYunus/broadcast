package com.iconkalbar.broadcast.factory;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;
import com.iconkalbar.broadcast.model.BroadcastNumber;
import com.iconkalbar.broadcast.model.PmSchedule;
import com.iconkalbar.broadcast.model.RecipientNumber;
import com.iconkalbar.broadcast.model.SitePOP;
import com.iconkalbar.broadcast.repository.BroadcastNumberRepository;
import com.iconkalbar.broadcast.repository.PmScheduleRepository;
import com.iconkalbar.broadcast.repository.RecipientNumberRepository;
import com.iconkalbar.broadcast.repository.SitePOPRepository;

@Component
public class ModelFactory {

    @Autowired
    private BroadcastNumberRepository broadcastNumberRepository;

    @Autowired
    private RecipientNumberRepository recipientNumberRepository;

    @Autowired
    private SitePOPRepository sitePOPRepository;

    @Autowired
    private PmScheduleRepository pmScheduleRepository;

    private Faker faker = new Faker();
    
    public BroadcastNumber generateBroadcastNumber() {
        BroadcastNumber randomContact = BroadcastNumber.builder()
                                        .userName(faker.name().fullName())
                                        .waNumber(faker.phoneNumber().cellPhone())
                                        .build();
        return broadcastNumberRepository.save(randomContact);
    }

    public RecipientNumber generateRecipientNumber() {
        RecipientNumber randomContact = RecipientNumber.builder()
                                        .userName(faker.name().fullName())
                                        .waNumber(faker.phoneNumber().cellPhone())
                                        .build();
        return recipientNumberRepository.save(randomContact);
    }

    public SitePOP generateSitePOP() {
        SitePOP randomSite = SitePOP.builder()
                            .name(faker.country().capital())
                            .popId(faker.idNumber().valid())
                            .type("Shelter")
                            .build();
        return sitePOPRepository.save(randomSite);
    }

    public PmSchedule generatePmSchedule(boolean isMaintenanceDone, Date scheduledDate, Date realizationDate) {
        RecipientNumber recipientNumber = generateRecipientNumber();
        SitePOP sitePOP = generateSitePOP();

        PmSchedule schedule = PmSchedule.builder()
                                .recipient(recipientNumber)
                                .sitePop(sitePOP)
                                .isMaintenanceDone(isMaintenanceDone)
                                .scheduledDate(scheduledDate)
                                .realizationDate(realizationDate)
                                .build();
        return pmScheduleRepository.save(schedule);
    }

    public void purgeAllData() {
        pmScheduleRepository.deleteAllInBatch();
        sitePOPRepository.deleteAllInBatch();
        recipientNumberRepository.deleteAllInBatch();
        broadcastNumberRepository.deleteAllInBatch();
    }
}
