/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gelael.interapp.service;

import com.gelael.interapp.domain.ExtPath;
import com.gelael.interapp.domain.IRepository;
import com.gelael.interapp.domain.OldMasterItem;
import com.gelael.interapp.domain.OldMasterItemRepo;
import com.gelael.interapp.form.Application;
import com.hexiong.jdbf.DBFWriter;
import com.hexiong.jdbf.JDBFException;
import com.hexiong.jdbf.JDBField;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author edo
 */
@Service
public class MasterService {

    private static final Logger logger = LogManager.getLogger(MasterService.class.getName());

    @Autowired
    IRepository oldMasterItemRepo;

    @Autowired
    ExtPath extPath;

    private DateService ds = new DateService();

    public String getDataFromMDW(String transDate) throws Exception {
        String dateReqSql = ds.convertDateRequest(transDate);
        String info = "";

        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", Application.TOKEN_ITEM_KEY);

        HttpEntity request = new HttpEntity(headers);

        ResponseEntity<OldMasterItem[]> re = restTemplate.exchange(extPath.getMdwUrl() + "/master/olditem/" + extPath.getOutletCode() + "/" + dateReqSql, HttpMethod.GET, request, OldMasterItem[].class);
        HttpStatus acc = re.getStatusCode();
        if (acc == HttpStatus.OK) {
            OldMasterItem[] omi = re.getBody();
            logger.info("Getting New Master...done");
            Map[] params = new Map[omi.length];

            for (int i = 0; i < omi.length; i++) {
                Map param = new HashMap();
                OldMasterItem o = omi[i];
                String s = o.getFmKode() + "\t" + o.getFmMerk() + "\t" + o.getFmNama() + "\n";
                info = info + s;
                param.put("oc", o.getOutletCode());
                param.put("td", o.getTransDate());
                param.put("FMKODE", o.getFmKode());
                param.put("FMBARC", o.getFmBarc());
                param.put("FMPLUS", o.getFmPlus());
                param.put("FMMERK", o.getFmMerk());
                param.put("FMNAMA", o.getFmNama());
                param.put("FMSING", o.getFmSing());
                param.put("FMKKEL", o.getFmKkel());
                param.put("FMKGRP", o.getFmKgrp());
                param.put("FMKDEP", o.getFmKdep());
                param.put("FMKSAT", o.getFmKsat());
                param.put("FMISIS", o.getFmIsis());
                param.put("FMPRFT", o.getFmPrft());
                param.put("FLPRFT", o.getFlPrft());
                param.put("FMFBKP", o.getFmFbkp());
                param.put("FMJUAL", o.getFmJual());
                param.put("FLJUAL", o.getFlJual());
                param.put("FMDTJN", o.getFmDtjn());
                param.put("FMRHRG", o.getFmRhrg());
                param.put("FMFDPT", o.getFmFdpt());
                param.put("FMFCRS", o.getFmFcrs());
                param.put("FMDVPO", o.getFmDvpo());
                param.put("FMBTDK", o.getFmBtdk());
                param.put("FMTGUP", o.getFmTgup());
                param.put("FMUSER", o.getFmUser());
                param.put("FMTGIN", o.getFmTgin());
                param.put("FMTGDC", o.getFmTgdc());
                param.put("FMTGAK", o.getFmTgak());
                param.put("FMMDML", o.getFmMdml());
                param.put("FMTMU1", o.getFmTmu1());
                param.put("FMTMU2", o.getFmTmu2());
                param.put("FMTHJ1", o.getFmThj1());
                param.put("FMTHJ2", o.getFmThj2());
                params[i] = param;
            }

            if (info.length() > 0) {
                Map paramDel = new HashMap();
                paramDel.put("oc", extPath.getOutletCode());
                paramDel.put("td", dateReqSql);

                oldMasterItemRepo.cleanUpdateBatch(
                        OldMasterItemRepo.DEL_OLD_MASTER_ITEM,
                        paramDel,
                        OldMasterItemRepo.INSERT_OLD_MASTER_ITEM,
                        params);
                logger.info("Persist New Master...done");
            } else {
                throw new Exception("Tidak ditemukan update data untuk tanggal yang diminta");
            }
        } else {
            throw new Exception("Ambil Data Dari HO Gagal");
        }
        return info;
    }

    public void doExtractDBF(String transDate) {
        try {
            // TODO add your handling code here:
            JDBField[] fields = {
                    new JDBField("FMKODE", 'C', 7, 0),
                    new JDBField("FMBARC", 'C', 13, 0),
                    new JDBField("FMPLUS", 'C', 15, 0),
                    new JDBField("FMMERK", 'C', 15, 0),
                    new JDBField("FMNAMA", 'C', 30, 0),
                    new JDBField("FMSING", 'C', 28, 0),
                    new JDBField("FMKKEL", 'C', 2, 0),
                    new JDBField("FMKGRP", 'C', 1, 0),
                    new JDBField("FMKDEP", 'C', 2, 0),
                    new JDBField("FMKSAT", 'C', 3, 0),
                    new JDBField("FMISIS", 'N', 4, 0),
                    new JDBField("FMPRFT", 'N', 6, 2),
                    new JDBField("FLPRFT", 'N', 6, 2),
                    new JDBField("FMFBKP", 'C', 1, 0),
                    new JDBField("FMJUAL", 'N', 7, 0),
                    new JDBField("FLJUAL", 'N', 7, 0),
                    new JDBField("FMDTJN", 'C', 2, 0),
                    new JDBField("FMRHRG", 'C', 1, 0),
                    new JDBField("FMFDPT", 'C', 1, 0),
                    new JDBField("FMFCRS", 'C', 1, 0),
                    new JDBField("FMDVPO", 'C', 1, 0),
                    new JDBField("FMBTDK", 'C', 1, 0),
                    new JDBField("FMTGUP", 'D', 8, 0),
                    new JDBField("FMUSER", 'C', 15, 0),
                    new JDBField("FMTGIN", 'D', 8, 0),
                    new JDBField("FMTGDC", 'D', 8, 0),
                    new JDBField("FMTGAK", 'D', 8, 0),
                    new JDBField("FMMDML", 'C', 20, 0),
                    new JDBField("FMTMU1", 'D', 8, 0),
                    new JDBField("FMTMU2", 'D', 8, 0),
                    new JDBField("FMTHJ1", 'D', 8, 0),
                    new JDBField("FMTHJ2", 'D', 8, 0)
            };

            String dateReqSql = ds.convertDateRequest(transDate);
            String dateReqDBF = ds.convertDateDBF(transDate);

            String fileNameDBF = File.separator + "A" + dateReqDBF + ".DBF";
            DBFWriter dbfwriter = new DBFWriter(extPath.getDbfTempPath() + fileNameDBF, fields);

            Map param = new HashMap();
            param.put("oc", extPath.getOutletCode());
            param.put("td", dateReqSql);

            List<OldMasterItem> items = oldMasterItemRepo.findMany(OldMasterItemRepo.QRY_OLD_MASTER_ITEM, param, new BeanPropertyRowMapper(OldMasterItem.class));

            for (Iterator<OldMasterItem> iterator = items.iterator(); iterator.hasNext(); ) {
                OldMasterItem o = iterator.next();
                dbfwriter.addRecord(new Object[]{
                                o.getFmKode(),
                                o.getFmBarc(),
                                o.getFmPlus(),
                                o.getFmMerk(),
                                o.getFmNama(),
                                o.getFmSing(),
                                o.getFmKkel(),
                                o.getFmKgrp(),
                                o.getFmKdep(),
                                o.getFmKsat(),
                                new Integer(o.getFmIsis()),
                                new Double(o.getFmPrft()),
                                new Double(o.getFlPrft()),
                                o.getFmFbkp(),
                                new Integer(o.getFmJual()),
                                new Integer(o.getFlJual()),
                                o.getFmDtjn(),
                                o.getFmRhrg(),
                                o.getFmFdpt(),
                                o.getFmFcrs(),
                                o.getFmDvpo(),
                                o.getFmBtdk(),
                                ds.convertStringDate(o.getFmTgup()),
                                o.getFmUser(),
                                ds.convertStringDate(o.getFmTgin()),
                                ds.convertStringDate(o.getFmTgdc()),
                                ds.convertStringDate(o.getFmTgak()),
                                o.getFmMdml(),
                                ds.convertStringDate(o.getFmTmu1()),
                                ds.convertStringDate(o.getFmTmu2()),
                                ds.convertStringDate(o.getFmThj1()),
                                ds.convertStringDate(o.getFmThj2())
                        }
                );
            }
            dbfwriter.close();
            logger.info("Creatinf Master DBF...done");
        } catch (JDBFException ex) {
            logger.fatal("JDBF Gagal Membentuk File Master DBF " + ex);
        }
    }
}
