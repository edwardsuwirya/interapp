package com.gelael.interapp.form;

import com.gelael.interapp.service.DateService;
import com.gelael.interapp.service.SalesService;
import com.gelael.interapp.service.SalesServiceRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Days;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by edo on 17/02/2016.
 */
@Component
public class NoGUIApplication {

    @Autowired
    SalesService salesService;

    private static Logger logger = LogManager.getLogger(NoGUIApplication.class.getName());

    private ExecutorService executor = Executors.newFixedThreadPool(5);

    public void salesProcessSingle(String transDate){
        System.out.println("Starting Text Based Application @ "+ new DateTime().toString());
        System.out.println("Processing Sales With Trans. Date : "+transDate);
        System.out.println("===============================================");
        Runnable worker = new SalesServiceRunnable(salesService,transDate);
        executor.execute(worker);
        executor.shutdown();
        while (!executor.isTerminated()) {   }
        System.out.println("Finish @ "+new DateTime().toString());
        System.exit(0);
    }
    public void salesProcessInPeriod(String startDate,String endDate){
        System.out.println("Starting Text Based Application @ "+ new DateTime().toString());
        System.out.println("Processing Sales With Trans. Date : "+startDate +" until "+endDate);
        System.out.println("===============================================");
        DateService ds = new DateService();
        DateTime dt1 = ds.convertStringToDateTime(startDate,"dd/MM/yyyy");
        DateTime dt2 = ds.convertStringToDateTime(endDate,"dd/MM/yyyy");
        int totDays = Days.daysBetween(dt1,dt2).getDays();
        for (int i = 0; i < totDays+1 ; i++) {
            String dd = ds.convertDateTimeToString(dt1.plusDays(i),"dd/MM/yyyy");
            Runnable worker = new SalesServiceRunnable(salesService,dd);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {   }
        System.out.println("Finish @ "+new DateTime().toString());
        System.exit(0);
    }
    public void masterItemProcess(){

    }
}
