/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gelael.interapp.service;

import com.gelael.interapp.domain.*;
import com.gelael.interapp.form.Application;
import com.hexiong.jdbf.DBFReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.io.File;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.*;

/**
 * @author edo
 */
@Service
public class SalesService {
    @Autowired
    ExtPath extPath;

    @Autowired
    IRepository salesRepo;

    @Autowired
    DateService dateService;

    public void test() {
        System.out.println("sss");
    }

    public String sendSales(String transDate) {
        try {
            String dateReq = dateService.convertDateRequest(transDate);
            Map param = new HashMap();
            param.put("oc", extPath.getOutletCode());
            param.put("td", dateReq);

            SalesHeader hdr = (SalesHeader) salesRepo.findOne(SalesRepo.QRY_SALES_HEADER, param,
                    new BeanPropertyRowMapper(SalesHeader.class));
            if (hdr == null) {
                return "Tidak ditemukan data sales";
            } else {
                List<SalesDetail> dtl = salesRepo.findMany(SalesRepo.QRY_SALES_DETAIL, param,
                        new BeanPropertyRowMapper(SalesDetail.class));
                List<Profit> prf = salesRepo.findMany(SalesRepo.QRY_PROFIT, param,
                        new BeanPropertyRowMapper(Profit.class));

                int i = 0;
                SalesDetail[] details = new SalesDetail[dtl.size()];
                for (Iterator<SalesDetail> iterator = dtl.iterator(); iterator.hasNext(); ) {
                    SalesDetail detail = iterator.next();
                    details[i] = detail;
                    i++;
                }

                i = 0;
                Profit[] profits = new Profit[prf.size()];
                for (Iterator<Profit> iterator = prf.iterator(); iterator.hasNext(); ) {
                    Profit profit = iterator.next();
                    profits[i] = profit;
                    i++;
                }

                Sales sales = new Sales();
                sales.setSalesHeader(hdr);
                sales.setSalesDetails(details);
                sales.setProfits(profits);

                RestTemplate restTemplate = new RestTemplate();

                HttpHeaders headers = new HttpHeaders();
                headers.set("Authorization", Application.TOKEN_SALES_KEY);

                HttpEntity request = new HttpEntity(sales, headers);

                ResponseEntity<ResponseInfo> result = restTemplate.postForEntity(extPath.getMdwUrl() + "/sales/update",
                        request, ResponseInfo.class);
                HttpStatus acc = result.getStatusCode();
                String msg = "";
                if (acc == HttpStatus.OK) {
                    if (result.getBody().getResponseCode().equals("00")) {
                        msg = "Kirim Sales Sukses " + dateReq;
                    } else {
                        msg = "Kirim Sales Gagal " + dateReq;
                    }
                } else {
                    msg = "Kirim Sales Gagal " + acc.toString();
                }
                return msg;
            }
        } catch (Exception ex) {
            throw new RuntimeException("Error sending Sales " + ex);
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public int updateSalesTransaction(String transDate) {
        int result = 0;
        try {
            String dateReq = dateService.convertDateRequest(transDate);
            String fileNameReq = dateService.convertDateFileNameDBF(transDate);

            Map param = new HashMap();
            param.put("oc", extPath.getOutletCode());
            param.put("td", dateReq);

            salesRepo.update(SalesRepo.DEL_TRX_SALES_HEADER, param);
            salesRepo.update(SalesRepo.DEL_TRX_SALES_DETAIL, param);

            DBFReader dbfReaderHeader = new DBFReader(extPath.getDbfTempPath() + File.separator +
                    extPath.getOutletPrefix() + "_H" + fileNameReq + ".DBF");
            DBFReader dbfReaderDetail = new DBFReader(extPath.getDbfTempPath() + File.separator +
                    extPath.getOutletPrefix() + "_D" + fileNameReq + ".DBF");

            List<Map> paramHeaders = new ArrayList();
            for (int i = 0; dbfReaderHeader.hasNextRecord(); i++) {
                Object[] hdr = dbfReaderHeader.nextRecord(Charset.forName("UTF-8"));
                String dateSales = dateService.convertDateSales(hdr[6].toString());
                if (dateReq.equals(dateSales)) {
                    Map paramHeader = new HashMap();
                    paramHeader.put("FHRCID", hdr[0].toString());
                    paramHeader.put("FHTIPE", hdr[1].toString());
                    paramHeader.put("FHKCAB", hdr[2].toString());
                    paramHeader.put("FHKKSR", hdr[3].toString());
                    paramHeader.put("FHSTAT", hdr[4].toString());
                    paramHeader.put("FHNTRN", hdr[5].toString());
                    paramHeader.put("FHTGLT", dateService.convertDateSales(hdr[6].toString()));
                    paramHeader.put("FHPDIS", parsingNumber("FHPDIS",hdr[7].toString()));
                    paramHeader.put("FHRDIS", parsingNumber("FHRDIS",hdr[8].toString()));
                    paramHeader.put("FHNGDC", hdr[9].toString());
                    paramHeader.put("FHKDDC", hdr[10].toString());
                    paramHeader.put("FHTAMT", parsingNumber("FHTAMT",hdr[11].toString()));
                    paramHeader.put("FHTDIS", parsingNumber("FHTDIS",hdr[12].toString()));
                    paramHeader.put("FHBKAS", parsingNumber("FHBKAS",hdr[13].toString()));
                    paramHeader.put("FHKCCD", hdr[14].toString());
                    paramHeader.put("FHNCCD", hdr[15].toString());
                    paramHeader.put("FHBCCD", parsingNumber("FHBCCD",hdr[16].toString()));
                    paramHeader.put("FHNCCL", hdr[17].toString());
                    paramHeader.put("FHBCCL", parsingNumber("FHBCCL",hdr[18].toString()));
                    paramHeader.put("FHBJKP", parsingNumber("FHBJKP",hdr[19].toString()));
                    paramHeader.put("FHBRKP", parsingNumber("FHBRKP",hdr[20].toString()));
                    paramHeader.put("FHKPLK", hdr[21].toString());
                    paramHeader.put("FHJAMM", hdr[22].toString());
                    paramHeader.put("FHJAMS", hdr[23].toString());
                    paramHeader.put("FHPPAY", hdr[24].toString());
                    paramHeader.put("FHPREF", hdr[25].toString());
                    paramHeader.put("FHAREF", parsingNumber("FHAREF",hdr[26].toString()));
                    paramHeader.put("FHNREF", hdr[27].toString());
                    if (hdr[28] != null) {
                        paramHeader.put("FHTGLR", dateService.convertDateSales(hdr[28].toString()));
                    } else {
                        paramHeader.put("FHTGLR", "1901-01-01");
                    }
                    paramHeader.put("FHTVAL", hdr[29].toString());
                    paramHeader.put("FHNVAL", parsingNumber("FHNVAL",hdr[30].toString()));
                    paramHeader.put("FHPDPR", parsingNumber("FHPDPR",hdr[31].toString()));
                    paramHeader.put("FHRDPR", parsingNumber("FHRDPR",hdr[32].toString()));
                    paramHeader.put("FHPDPC", parsingNumber("FHPDPC",hdr[33].toString()));
                    paramHeader.put("FHRDPC", parsingNumber("FHRDPC",hdr[34].toString()));
                    paramHeader.put("FHBACK", parsingNumber("FHBACK",hdr[35].toString()));
                    paramHeaders.add(paramHeader);
                }
            }
            Map[] ph = paramHeaders.toArray(new Map[paramHeaders.size()]);
            salesRepo.updateBatch(SalesRepo.UPD_TRX_SALES_HEADER, ph);

            List<Map> paramDetails = new ArrayList();
            for (int i = 0; dbfReaderDetail.hasNextRecord(); i++) {
                Object[] dtl = dbfReaderDetail.nextRecord(Charset.forName("UTF-8"));
                String dateSales = dateService.convertDateSales(dtl[6].toString());
                if (dateReq.equals(dateSales)) {
                    Map paramDetail = new HashMap();
                    paramDetail.put("FDRCID", dtl[0].toString());
                    paramDetail.put("FDTIPE", dtl[1].toString());
                    paramDetail.put("FDKCAB", dtl[2].toString());
                    paramDetail.put("FDKKSR", dtl[3].toString());
                    paramDetail.put("FDSTAT", dtl[4].toString());
                    paramDetail.put("FDNTRN", dtl[5].toString());
                    paramDetail.put("FDTGLT", dateService.convertDateSales(dtl[6].toString()));

                    paramDetail.put("FDNOUR", parsingNumber("FDNOUR",dtl[7].toString()));
                    paramDetail.put("FDKPLU", dtl[8].toString());
                    paramDetail.put("FDLBLP", dtl[9].toString());
                    paramDetail.put("FDPRFT", parsingNumber("FDPRFT",dtl[10].toString()));
                    paramDetail.put("FDJQTY", parsingNumber("FDJQTY",dtl[11].toString()));
                    paramDetail.put("FDHSAT", parsingNumber("FDHSAT",dtl[12].toString()));
                    paramDetail.put("FDDISC", parsingNumber("FDDISC",dtl[13].toString()));
                    paramDetail.put("FDNAMT", parsingNumber("FDNAMT",dtl[14].toString()));
                    paramDetail.put("FDDIS1", parsingNumber("FDDIS1",dtl[15].toString()));
                    paramDetail.put("FDPPAY", dtl[16].toString());
                    paramDetail.put("FDNOTA", dtl[17].toString());

                    paramDetails.add(paramDetail);
                }
            }
            Map[] pd = paramDetails.toArray(new Map[paramDetails.size()]);
            salesRepo.updateBatch(SalesRepo.UPD_TRX_SALES_DETAIL, pd);

            return result;
        } catch (Exception ex) {
            throw new RuntimeException("Error updating Sales " + ex);
        }

    }

    public String getSalesSimpleInfo(String transDate) {
        String info = "";
        try {
            String dateReq = dateService.convertDateRequest(transDate);
            Map param = new HashMap();
            param.put("td", dateReq);
            List<SalesSimple> salesSimple = salesRepo.findMany(SalesRepo.QRY_SALES_SIMPLE_INFO_HEADER, param,
                    new BeanPropertyRowMapper(SalesSimple.class));
            TotalOnly to = (TotalOnly) salesRepo.findOne(SalesRepo.QRY_SALES_TRX_INFO_HEADER, param,
                    new BeanPropertyRowMapper(TotalOnly.class));
            info = "Informasi Singkat " + transDate + " \n----------------\n";

            for (Iterator<SalesSimple> iterator = salesSimple.iterator(); iterator.hasNext(); ) {
                SalesSimple ss = iterator.next();
                info = info + " " + ss.getType() + " " + NumberFormat.getNumberInstance(Locale.US).
                        format(ss.getAmount().doubleValue()) + "\n";
            }
            info = info + " Trx " + to.getTot().toString() + "\n";
            return info;
        } catch (Exception ex) {
            throw new RuntimeException("Error getting sales info from DB Local " + ex);
        }

    }

    private double parsingNumber(String field, String content) {
        try {
            return Double.parseDouble(content);
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Field " + field + " error parsing");
        }
    }
}
