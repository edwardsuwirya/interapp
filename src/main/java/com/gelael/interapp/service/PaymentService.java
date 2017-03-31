package com.gelael.interapp.service;

import com.gelael.interapp.domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 15050978 on 3/31/2017.
 */
@Service
public class PaymentService {
    @Autowired
    ExtPath extPath;

    @Autowired
    IRepository paymentRepo;

    @Autowired
    DateService dateService;

    private static final Logger logger = LogManager.getLogger(PaymentService.class.getName());

    public void doExtractDBF(String transDate) {
        String dateReqSql = dateService.convertDateRequest(transDate);
        String dateReqDBF = dateService.convertDateDBF(transDate);

        String fileNameDBF = File.separator + "A" + dateReqDBF + ".DBF";
//        DBFWriter dbfwriter = new DBFWriter(extPath.getDbfTempPath() + fileNameDBF, fields);

        Map param = new HashMap();
        param.put("sd", dateReqSql);
        param.put("ed", dateReqSql);

        List<OldMasterItem> items = paymentRepo.findMany(PaymentRepo.QRY_PAYMENT, param, new BeanPropertyRowMapper(Payment.class));

    }
}
