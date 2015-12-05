/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gelael.interapp.domain;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

import java.util.List;
import java.util.Map;

/**
 * @author edo
 */
public interface IRepository {

    public List findAll(String sql, BeanPropertyRowMapper mapper);

    public Object findOne(String sql, Map param, BeanPropertyRowMapper mapper);

    public List findMany(String sql, Map param, BeanPropertyRowMapper mapper);

    public void update(String sql, Map param);

    public void updateBatch(String sql, Map[] param);

    public void cleanUpdate(String sqlClean, Map paramClean, String sqlUpdate, Map paramUpdate);

    public void cleanUpdateBatch(String sqlClean, Map paramClean, String sqlUpdate, Map[] paramUpdate);
}
