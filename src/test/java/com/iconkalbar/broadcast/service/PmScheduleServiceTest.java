package com.iconkalbar.broadcast.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.iconkalbar.broadcast.TestConstant;
import com.iconkalbar.broadcast.factory.ModelFactory;
import com.iconkalbar.broadcast.model.PmSchedule;

@SpringBootTest
public class PmScheduleServiceTest {

    @Autowired
    private PmScheduleService pmScheduleService;

    @Autowired
    private ModelFactory modelFactory;

    private SimpleDateFormat sdformat = new SimpleDateFormat(TestConstant.dateFormat);

    @AfterEach
    void cleanUp() {
        modelFactory.purgeAllData();
    }

    @Test
    void testFetchAllScheduleThisWeek() throws ParseException {
        Date PoP1Date = sdformat.parse("06-11-2023");
        Date PoP2Date = sdformat.parse("08-11-2023");
        Date PoP3Date = sdformat.parse("10-11-2023");
        Date broadcastDate = sdformat.parse("06-11-2023");
        PmSchedule schedule1 = modelFactory.generatePmSchedule(true, PoP1Date, null);
        PmSchedule schedule2 = modelFactory.generatePmSchedule(false, PoP2Date, null);
        PmSchedule schedule3 = modelFactory.generatePmSchedule(false, PoP3Date, null);

        List<PmSchedule> pmList = pmScheduleService.fetchAllScheduleThisWeek(broadcastDate);

        assertEquals(2, pmList.size());
        assertFalse(pmList.contains(schedule1));
        assertTrue(pmList.contains(schedule2));
        assertTrue(pmList.contains(schedule3));
    }
}
