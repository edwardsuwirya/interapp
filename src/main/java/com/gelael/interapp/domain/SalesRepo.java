/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gelael.interapp.domain;

import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author edo
 */
@Repository
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class SalesRepo implements IRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public final static String DEL_TRX_SALES_HEADER = "DELETE FROM TRX_SLS_HEADER "
            + "WHERE FHKCAB=:oc AND FHTGLT=:td";

    public final static String DEL_TRX_SALES_DETAIL = "DELETE FROM TRX_SLS_DETAIL "
            + "WHERE FDKCAB=:oc AND FDTGLT=:td";

    public final static String UPD_TRX_SALES_HEADER = "INSERT INTO TRX_SLS_HEADER (FHRCID,FHTIPE,"
            + "FHKCAB,FHKKSR,FHSTAT,FHNTRN,FHTGLT,FHPDIS,"
            + "FHRDIS,FHNGDC,FHKDDC,FHTAMT,FHTDIS,FHBKAS,FHKCCD,FHNCCD,FHBCCD,FHNCCL,FHBCCL,"
            + "FHBJKP,FHBRKP,FHKPLK,FHJAMM,FHJAMS,FHPPAY,FHPREF,FHAREF,FHNREF,FHTGLR,"
            + "FHTVAL,FHNVAL,FHPDPR,FHRDPR,FHPDPC,FHRDPC,FHBACK) VALUES "
            + "(:FHRCID,:FHTIPE,:FHKCAB,:FHKKSR,:FHSTAT,:FHNTRN,:FHTGLT,:FHPDIS,"
            + ":FHRDIS,:FHNGDC,:FHKDDC,:FHTAMT,:FHTDIS,:FHBKAS,:FHKCCD,:FHNCCD,"
            + ":FHBCCD,:FHNCCL,:FHBCCL,:FHBJKP,:FHBRKP,:FHKPLK,:FHJAMM,:FHJAMS,:FHPPAY,"
            + ":FHPREF,:FHAREF,:FHNREF,:FHTGLR,:FHTVAL,:FHNVAL,:FHPDPR,:FHRDPR,:FHPDPC,:FHRDPC,:FHBACK) ";

    public final static String UPD_TRX_SALES_DETAIL = "INSERT INTO TRX_SLS_DETAIL (FDRCID,FDTIPE,"
            + "FDKCAB,FDKKSR,FDSTAT,FDNTRN,FDTGLT,FDNOUR,FDKPLU,FDLBLP,FDPRFT,FDJQTY,"
            + "FDHSAT,FDDISC,FDNAMT,FDDIS1,FDPPAY,FDNOTA) VALUES "
            + "(:FDRCID,:FDTIPE,"
            + ":FDKCAB,:FDKKSR,:FDSTAT,:FDNTRN,:FDTGLT,:FDNOUR,:FDKPLU,:FDLBLP,:FDPRFT,:FDJQTY,"
            + ":FDHSAT,:FDDISC,:FDNAMT,:FDDIS1,:FDPPAY,:FDNOTA) ";

    public final static String QRY_SALES_SIMPLE_INFO_HEADER = "select FHTIPE type,sum(FHTAMT) amount "
            + "from trx_sls_header "
            + "where FHTGLT=:td "
            + "group by FHTIPE ";

    public final static String QRY_SALES_TRX_INFO_HEADER = "select count(FHNTRN) tot "
            + "from trx_sls_header "
            + "where FHTGLT=:td ";

    public final static String QRY_SALES_SIMPLE_INFO_DETAIL = "select FDTIPE type,sum(FDNAMT) amount "
            + "from trx_sls_detail "
            + "where FDTGLT=:td "
            + "group by FDTIPE ";

    public final static String QRY_SALES_HEADER = "select " +
            "  h.FHKCAB, " +
            "  h.FHTGLT, " +
            "  count(h.FHNTRN) AS TRX, " +
            "  sum(h.FHTAMT) AS AMT, " +
            "  sum(h.FHTDIS) AS DISC " +
            "from " +
            "  trx_sls_header h " +
            "where " +
            "  (h.FHKCAB=:oc AND h.FHTGLT=:td AND h.FHTIPE = 'S') " +
            "group by h.FHKCAB , h.FHTGLT";

    public final static String QRY_SALES_DETAIL = "SELECT " +
            "  tota.FDKCAB, " +
            "  tota.FDTGLT, " +
            "  tota.FDKPLU, " +
            "  tota.QTY, " +
            "  tota.AMT, " +
            "  totd.DISC " +
            "FROM " +
            "  ((SELECT " +
            "      amt.FDKCAB, " +
            "      amt.FDTGLT, " +
            "      amt.FDKPLU, " +
            "      sum(amt.FDJQTY) AS QTY, " +
            "      sum(amt.FDNAMT) AS AMT " +
            "    FROM " +
            "      trx_sls_detail amt " +
            "    WHERE " +
            "      (amt.FDKCAB = :oc AND " +
            "       amt.FDTGLT = :td AND " +
            "       amt.FDTIPE = 'S') " +
            "    GROUP BY amt.FDKCAB, " +
            "      amt.FDTGLT, amt.FDKPLU) tota " +
            "    LEFT JOIN (SELECT " +
            "                 dis.FDKCAB      AS FDKCAB, " +
            "                 dis.FDTGLT      AS FDTGLT, " +
            "                 dis.FDKPLU      AS FDKPLU, " +
            "                 sum(dis.FDDISC) AS DISC " +
            "               FROM " +
            "                 trx_sls_detail dis " +
            "               WHERE " +
            "                 (dis.FDKCAB = :oc AND " +
            "                  dis.FDTGLT = :td AND " +
            "                  dis.FDLBLP NOT IN ('S', 'T') " +
            "                 ) " +
            "               GROUP BY dis.FDKCAB, " +
            "                 dis.FDTGLT, dis.FDKPLU) totd " +
            "      ON (((tota.FDKCAB = totd.FDKCAB) " +
            "           AND (tota.FDTGLT = totd.FDTGLT) " +
            "           AND (tota.FDKPLU = totd.FDKPLU)))) ";

    public final static String QRY_PROFIT = "select " +
            "  prf.FDKCAB, " +
            "  prf.FDTGLT, " +
            "  prf.FDKPLU, " +
            "  prf.FDPRFT, " +
            "  sum(prf.FDNAMT) AS AMT, " +
            "  sum(((prf.FDNAMT - prf.FDDIS1) * (prf.FDPRFT / 100))) AS PRF " +
            "from " +
            "  trx_sls_detail prf " +
            "where prf.FDKCAB=:oc AND prf.FDTGLT=:td " +
            "group by prf.FDKCAB , prf.FDTGLT , prf.FDKPLU , prf.FDPRFT";

    public SalesRepo() {
    }

    @Autowired
    public SalesRepo(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    public List findAll(String sql, BeanPropertyRowMapper mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Object findOne(String sql, Map param, BeanPropertyRowMapper mapper) {
        try {
            return namedParameterJdbcTemplate.queryForObject(sql, param, mapper);
        } catch (EmptyResultDataAccessException ex) {
            return null;
        }
    }

    @Override
    public List findMany(String sql, Map param, BeanPropertyRowMapper mapper) {
        return namedParameterJdbcTemplate.query(sql, param, mapper);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void update(String sql, Map param) {
        try {
            namedParameterJdbcTemplate.update(sql, param);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void updateBatch(String sql, Map[] param) {
        try {
            namedParameterJdbcTemplate.batchUpdate(sql, param);
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    @Override
    public void cleanUpdate(String sqlClean, Map paramClean, String sqlUpdate, Map paramUpdate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void cleanUpdateBatch(String sqlClean, Map paramClean, String sqlUpdate, Map[] paramUpdate) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
