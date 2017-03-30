package com.gelael.interapp.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * Created by edo on 19/02/2016.
 */

public class SalesServiceRunnable implements Runnable {
    private String transDate;
    private SalesService salesService;

    private static Logger logger = LogManager.getLogger(SalesServiceRunnable.class.getName());

    public SalesServiceRunnable(){}

    public SalesServiceRunnable(SalesService salesService, String transDate){
        this.transDate = transDate;
        this.salesService = salesService;
    }
    @Override
    public void run() {
        try {
            int result = salesService.updateSalesTransaction(transDate);
            if (result == 0) {
                String info = salesService.getSalesSimpleInfo(transDate);
                System.out.println(info);
                info = salesService.sendSales(transDate);
                System.out.println(info);
                System.out.println(" ");
            } else {
                System.out.println("Baca Sales DBF gagal");
            }
        } catch (Exception ex) {
            logger.error("Kirim Sales " + transDate + " " + ex);
            System.out.println("Kirim Sales Ke HO gagal \n" + ex);
        } finally {
            logger.info("User Selesai Kirim Sales " + transDate + " @ " + new Date());
        }
    }
}
