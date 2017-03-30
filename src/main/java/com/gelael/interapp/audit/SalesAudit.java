package com.gelael.interapp.audit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;

import java.util.Date;

/**
 * Created by edo on 27/02/2016.
 */

@Aspect
public class SalesAudit {
    private static final Logger logger = LogManager.getLogger(SalesAudit.class.getName());

    @Before("execution(* com.gelael.interapp.service.TokenService.getToken(..))")
    public void logToken(JoinPoint joinPoint) {
        if (joinPoint.getArgs()[0].equals("sales")) {
            System.out.println(new Date() + "... Token is being requested for sales");
        }
    }

    @Before("execution(* com.gelael.interapp.service.SalesService.updateSalesTransaction(..))")
    public void logUpdateSales(JoinPoint joinPoint) {
        System.out.println(new Date() + "... Sales is being update for " + joinPoint.getArgs()[0]);
    }

    @AfterReturning(value = "execution(* com.gelael.interapp.service.SalesService.updateSalesTransaction(..))", returning = "result")
    public void logSuccessUpdateSales(JoinPoint joinPoint, Object result) {
        System.out.println(new Date() + "... Success updating sales for " + joinPoint.getArgs()[0] + " : " + result);
    }

    @AfterThrowing(value = "execution(* com.gelael.interapp.service.SalesService.updateSalesTransaction(..))", throwing = "ex")
    public void logErrorUpdateSales(JoinPoint joinPoint, Exception ex) {
        System.out.println(new Date() + "... Error updating sales for " + joinPoint.getArgs()[0] + " : " + ex);
    }

    @Before("execution(* com.gelael.interapp.service.SalesService.sendSales(..))")
    public void logSendSales(JoinPoint joinPoint) {
        System.out.println(new Date() + "... Sales is being send for " + joinPoint.getArgs()[0]);
    }

    @AfterReturning(value = "execution(* com.gelael.interapp.service.SalesService.sendSales(..))", returning = "result")
    public void logSuccessSendSales(JoinPoint joinPoint, Object result) {
        System.out.println(new Date() + "... Success sending sales for " + joinPoint.getArgs()[0] + " : " + result);
    }

    @AfterThrowing(value = "execution(* com.gelael.interapp.service.SalesService.sendSales(..))", throwing = "ex")
    public void logErrorSendSales(JoinPoint joinPoint, Exception ex) {
        System.out.println(new Date() + "... Error sending sales for " + joinPoint.getArgs()[0] + " : " + ex);
    }

    @Before("execution(* com.gelael.interapp.service.SalesService.getSalesSimpleInfo(..))")
    public void logGetInfoSales(JoinPoint joinPoint) {
        System.out.println(new Date() + "... Sales Info is being retrieve for " + joinPoint.getArgs()[0]);
    }

    @AfterReturning(value = "execution(* com.gelael.interapp.service.SalesService.getSalesSimpleInfo(..))", returning = "result")
    public void logSuccessGetInfoSales(JoinPoint joinPoint, Object result) {
        System.out.println(new Date() + "... Success retriving sales info for " + joinPoint.getArgs()[0] + " : " + result);
    }

    @AfterThrowing(value = "execution(* com.gelael.interapp.service.SalesService.getSalesSimpleInfo(..))", throwing = "ex")
    public void logErrorGetInfoSales(JoinPoint joinPoint, Exception ex) {
        System.out.println(new Date() + "... Error retriving sales info for " + joinPoint.getArgs()[0] + " : " + ex);
    }

}
