package com.gelael.interapp.domain;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * Created by edo on 14/01/2016.
 */
@Getter
@Setter
public class Profit {
    String FDKCAB;
    String FDTGLT;
    String FDKPLU;
    BigDecimal FDPRFT;
    BigDecimal AMT;
    BigDecimal PRF;
}
