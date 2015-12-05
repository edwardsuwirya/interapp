/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gelael.interapp.domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author edo
 */
@Repository
public class OldMasterItemRepo implements IRepository {

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public final static String QRY_OLD_MASTER_ITEM = "select  * "
            + "from M_BARANG "
            + "where outlet_code=:oc and trans_date=:td";

    public final static String INSERT_OLD_MASTER_ITEM = "insert into M_BARANG "
            + "(OUTLET_CODE,TRANS_DATE,FMKODE,FMBARC,FMPLUS,FMMERK,FMNAMA,FMSING,FMKKEL,FMKGRP,FMKDEP,FMKSAT,FMISIS," +
            "FMPRFT,FLPRFT,FMFBKP,FMJUAL,FLJUAL,FMDTJN,FMRHRG,FMFDPT,FMFCRS,FMDVPO,FMBTDK,FMTGUP,FMUSER,FMTGIN,FMTGDC," +
            "FMTGAK,FMMDML,FMTMU1,FMTMU2,FMTHJ1,FMTHJ2) values(" +
            ":oc,:td,:FMKODE,:FMBARC,:FMPLUS,:FMMERK,:FMNAMA,:FMSING,:FMKKEL,:FMKGRP,:FMKDEP,:FMKSAT,:FMISIS,:FMPRFT,:FLPRFT," +
            ":FMFBKP,:FMJUAL,:FLJUAL,:FMDTJN,:FMRHRG,:FMFDPT,:FMFCRS,:FMDVPO,:FMBTDK,:FMTGUP,:FMUSER,:FMTGIN,:FMTGDC," +
            ":FMTGAK,:FMMDML,:FMTMU1,:FMTMU2,:FMTHJ1,:FMTHJ2" +
            ")";

    public final static String DEL_OLD_MASTER_ITEM = "delete from M_BARANG where outlet_code=:oc and trans_date=:td";

    public OldMasterItemRepo() {
    }

    @Autowired
    public OldMasterItemRepo(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional(readOnly = true)
    public List findAll(String sql, BeanPropertyRowMapper mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(readOnly = true)
    public Object findOne(String sql, Map param, BeanPropertyRowMapper mapper) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(readOnly = true)
    public List findMany(String sql, Map param, BeanPropertyRowMapper mapper) {
        return namedParameterJdbcTemplate.query(sql, param, mapper);
    }

    @Override
    @Transactional
    public void update(String sql, Map param) {
        namedParameterJdbcTemplate.update(sql, param);
    }

    @Override
    @Transactional
    public void updateBatch(String sql, Map[] param) {
        namedParameterJdbcTemplate.batchUpdate(sql, param);
    }

    @Override
    @Transactional
    public void cleanUpdate(String sqlClean, Map paramClean, String sqlUpdate, Map paramUpdate) {
        namedParameterJdbcTemplate.update(sqlClean, paramClean);
        namedParameterJdbcTemplate.update(sqlUpdate, paramUpdate);
    }

    @Override
    @Transactional
    public void cleanUpdateBatch(String sqlClean, Map paramClean, String sqlUpdate, Map[] paramUpdate) {
        namedParameterJdbcTemplate.update(sqlClean, paramClean);
        namedParameterJdbcTemplate.batchUpdate(sqlUpdate, paramUpdate);
    }

}
