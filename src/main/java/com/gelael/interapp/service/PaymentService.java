package com.gelael.interapp.service;

import com.gelael.interapp.domain.ExtPath;
import com.gelael.interapp.domain.IRepository;
import com.gelael.interapp.domain.Payment;
import com.gelael.interapp.domain.PaymentRepo;
import com.hexiong.jdbf.DBFWriter;
import com.hexiong.jdbf.JDBFException;
import com.hexiong.jdbf.JDBField;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
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
        try {
            JDBField[] fields = {
                    new JDBField("FHRCID", 'C', 1, 0),
                    new JDBField("FHTIPE", 'C', 1, 0),
                    new JDBField("FHKCAB", 'C', 2, 0),
                    new JDBField("FHNOMD", 'C', 15, 0),
                    new JDBField("FHTGLD", 'D', 8, 0),
                    new JDBField("FHTGLT", 'D', 8, 0),
                    new JDBField("FHJTOP", 'N', 3, 0),
                    new JDBField("FHTGLJ", 'D', 8, 0),
                    new JDBField("FHKCTR", 'C', 2, 0),
                    new JDBField("FHSUPP", 'C', 5, 0),
                    new JDBField("FHFPPN", 'C', 1, 0),
                    new JDBField("FHFPJK", 'C', 1, 0),
                    new JDBField("FHNOMR", 'C', 9, 0),
                    new JDBField("FHTNIL", 'N', 14, 2),
                    new JDBField("FHTPPN", 'N', 13, 2),
                    new JDBField("FHTPPM", 'N', 13, 2),
                    new JDBField("FHNBTL", 'N', 10, 2),
                    new JDBField("FHDISC", 'N', 10, 2),
                    new JDBField("FHLAIN", 'N', 10, 2),
                    new JDBField("FHTNLL", 'N', 14, 2),
                    new JDBField("FHTPNL", 'N', 13, 2),
                    new JDBField("FHTPML", 'N', 13, 2),
                    new JDBField("FHNBLL", 'N', 10, 2),
                    new JDBField("FHDISL", 'N', 10, 2),
                    new JDBField("FHNLNL", 'N', 10, 2),
                    new JDBField("FHKETR", 'C', 30, 0),
                    new JDBField("FHUSER", 'C', 3, 0),
                    new JDBField("FHNOPC", 'C', 9, 0),
                    new JDBField("FHNOPL", 'C', 9, 0),
                    new JDBField("FHTGUP", 'D', 8, 0),
                    new JDBField("FHTGUL", 'D', 8, 0),
                    new JDBField("FHTAGP", 'C', 1, 0),
                    new JDBField("FHBYRT", 'C', 1, 0),
                    new JDBField("FHSTAT", 'C', 1, 0),
                    new JDBField("FHFLAG", 'C', 1, 0)
            };

            String dateReqSql = dateService.convertDateRequest(transDate);
            String dateReqDBF = dateService.convertDateDBF(transDate);

            String fileNameDBF = File.separator + "TH" + dateReqDBF + ".DBF";
            DBFWriter dbfwriter = new DBFWriter(extPath.getDbfTempPath() + fileNameDBF, fields);

            Map param = new HashMap();
            param.put("sd", dateReqSql);
            param.put("ed", dateReqSql);

            List<Payment> items = paymentRepo.findMany(PaymentRepo.QRY_PAYMENT, param, new BeanPropertyRowMapper(Payment.class));
            for (Iterator<Payment> iterator = items.iterator(); iterator.hasNext(); ) {
                Payment p = iterator.next();
                dbfwriter.addRecord(new Object[]{
                        p.getFHRCID(),
                        p.getFHTIPE(),
                        p.getFHKCAB(),
                        p.getFHNOMD(),
                        dateService.convertStringDate(p.getFHTGLD()),
                        dateService.convertStringDate(p.getFHTGLT()),
                        p.getFHJTOP().intValue(),
                        dateService.convertStringDate(p.getFHTGLJ()),
                        p.getFHKCTR(),
                        p.getFHSUPP(),
                        p.getFHFPPN(),
                        p.getFHFPJK(),
                        p.getFHNOMR(),
                        p.getFHTNIL().doubleValue(),
                        p.getFHTPPN().doubleValue(),
                        p.getFHTPPM().doubleValue(),
                        p.getFHNBTL().doubleValue(),
                        p.getFHDISC().doubleValue(),
                        p.getFHLAIN().doubleValue(),
                        p.getFHTNLL().doubleValue(),
                        p.getFHTPNL().doubleValue(),
                        p.getFHTPML().doubleValue(),
                        p.getFHNBLL().doubleValue(),
                        p.getFHDISL().doubleValue(),
                        p.getFHNLNL().doubleValue(),
                        p.getFHKETR(),
                        p.getFMTGAK(),
                        p.getFHUSER(),
                        p.getFHNOPC(),
                        p.getFHNOPL(),
                        dateService.convertStringDate(p.getFHTGUP()),
                        dateService.convertStringDate(p.getFHTGUL()),
                        p.getFHTAGP(),
                        p.getFHBYRT(),
                        p.getFHSTAT(),
                        p.getFHFLAG(),
                });
            }
            dbfwriter.close();
            logger.info("Creating Payment DBF...done");
        } catch (JDBFException ex) {
            logger.fatal("JDBF Gagal Membentuk File Pembayaran DBF " + ex);
        }
    }
}
