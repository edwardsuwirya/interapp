/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gelael.interapp.domain;

import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author edo
 */
@Getter
@Setter
public class SalesHeader {
    String FHKCAB;
    String FHTGLT;
    BigDecimal TRX;
    BigDecimal AMT;
    BigDecimal DISC;
}
