package com.gelael.interapp.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Created by 15050978 on 3/31/2017.
 */
@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class PaymentRepo implements IRepository {
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public static final String QRY_PAYMENT = "SELECT FHRCID, FHTIPE, FHKCAB, FHNOMD, FHTGLD, FHTGLT, FHJTOP, FHTGLJ, FHKCTR, FHSUPP, FHFPPN, FHFPJK, FHNOMR, FHTNIL, FHTPPN, FHTPPM, " +
            "FHNBTL, FHDISC, FHLAIN, FHTNLL, FHTPNL, FHTPML, FHNBLL, FHDISL, FHNLNL, FHKETR, FHUSER, FHNOPC, FHNOPL, FHTGUP, FHTGUL, FHTAGP, FHBYRT, " +
            "FHSTAT,FHFLAG FROM " +
            "( " +
            "SELECT '' AS FHRCID, 0 AS FHTIPE, a.BranchId AS FHKCAB, CASE WHEN a.journalid IS NULL OR a.journalid = '' OR a.journalid = ' ' THEN NULL ELSE a.journalid END AS FHNOMD, " +
            "DATE_FORMAT(a.fakturdate,'%d/%m/%y') AS FHTGLD, " +
            "DATE_FORMAT(a.recodate,'%d/%m/%y') AS FHTGLT, b.PayDays AS FHJTOP, DATE_FORMAT(a.arrivaldate,'%d/%m/%y') AS FHTGLJ, " +
            "'' AS FHKCTR, a.supplierId AS FHSUPP, '' AS FHFPPN, '' AS FHFPJK, " +
            "CONCAT(a.branchid, RIGHT(DATE_FORMAT(a.recodate,'%Y'),1), DATE_FORMAT(a.recodate,'%m'), RIGHT(b.poid,4)) AS FHNOMR, a.TotalPrice AS FHTNIL, " +
            "a.OtherExp AS FHTPPN, 0 AS FHTPPM, 0 AS FHNBTL, 0 AS FHDISC, 0 AS FHLAIN, a.TotalPrice AS FHTNLL, a.OtherExp AS FHTPNL, " +
            "0 AS FHTPML, 0 AS FHNBLL, 0 AS FHDISL, 0 AS FHNLNL, '' AS FHKETR, 'SYS' AS FHUSER, " +
            "CONCAT(a.branchid, RIGHT(DATE_FORMAT(a.recodate,'%Y'),1), DATE_FORMAT(a.recodate,'%m'), RIGHT(a.recoid,4)) AS FHNOPC, " +
            "'' AS FHNOPL, DATE_FORMAT(a.recodate,'%d/%m/%y') AS FHTGUP, '' AS FHTGUL, 'P' AS FHTAGP, CASE WHEN customerid = 'Tunai' THEN 'Y' ELSE NULL END AS FHBYRT, " +
            "NULL AS FHSTAT, '' AS FHFLAG FROM tbreco a JOIN tbpo b ON a.StatPOID = b.POID WHERE a.recodate BETWEEN :sd AND :ed " +
            " " +
            "UNION ALL " +
            " " +
            "SELECT '' AS FHRCID, 3 AS FHTIPE, a.BranchId AS FHKCAB, CASE WHEN a.noreff IS NULL OR a.noreff = '' OR a.noreff = ' ' THEN NULL ELSE a.noreff END AS FHNOMD, " +
            "DATE_FORMAT(a.returdate,'%d/%m/%y') AS FHTGLD, " +
            "DATE_FORMAT(a.returdate,'%d/%m/%y') AS FHTGLT, 0 AS FHJTOP, NULL AS FHTGLJ, " +
            "'' AS FHKCTR, a.supplierId AS FHSUPP, '' AS FHFPPN, '' AS FHFPJK, " +
            "CONCAT(a.branchid, RIGHT(DATE_FORMAT(a.returdate,'%Y'),1), DATE_FORMAT(a.returdate,'%m'), RIGHT(b.statpoid,4)) AS FHNOMR, a.Total AS FHTNIL, " +
            "a.PPN AS FHTPPN, 0 AS FHTPPM, 0 AS FHNBTL, 0 AS FHDISC, 0 AS FHLAIN, a.Total AS FHTNLL, a.PPN AS FHTPNL, " +
            "0 AS FHTPML, 0 AS FHNBLL, 0 AS FHDISL, 0 AS FHNLNL, '' AS FHKETR, 'SYS' AS FHUSER, " +
            "CONCAT(a.branchid, RIGHT(DATE_FORMAT(a.returdate,'%Y'),1), DATE_FORMAT(a.returdate,'%m'), RIGHT(a.returid,4)) AS FHNOPC, " +
            "'' AS FHNOPL, DATE_FORMAT(a.returdate,'%d/%m/%y') AS FHTGUP, '' AS FHTGUL, 'P' AS FHTAGP, null AS FHBYRT, " +
            "NULL AS FHSTAT, '' AS FHFLAG FROM tbretur a JOIN tbreco b ON a.BTB = b.recoid " +
            "JOIN tbpo c ON b.statpoid = c.poid WHERE a.returdate BETWEEN :sd AND :ed " +
            ") A " +
            "ORDER BY A.FHTGLD, A.FHTIPE";

    @Autowired
    public PaymentRepo(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List findAll(String sql, BeanPropertyRowMapper mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findOne(String sql, Map param, BeanPropertyRowMapper mapper) {
        return null;
    }

    @Override
    public List findMany(String sql, Map param, BeanPropertyRowMapper mapper) {
        try {
            return namedParameterJdbcTemplate.query(sql, param, mapper);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public void update(String sql, Map param) {

    }

    @Override
    public void updateBatch(String sql, Map[] param) {

    }

    @Override
    public void cleanUpdate(String sqlClean, Map paramClean, String sqlUpdate, Map paramUpdate) {

    }

    @Override
    public void cleanUpdateBatch(String sqlClean, Map paramClean, String sqlUpdate, Map[] paramUpdate) {

    }
}
