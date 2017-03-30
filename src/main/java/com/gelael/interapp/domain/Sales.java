/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gelael.interapp.domain;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author edo
 */
@Getter
@Setter
public class Sales {

    SalesHeader salesHeader;
    SalesDetail[] salesDetails;
    Profit[] profits;
}
