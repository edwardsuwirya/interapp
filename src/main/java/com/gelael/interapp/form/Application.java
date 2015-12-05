package com.gelael.interapp.form;


import com.gelael.interapp.context.AppContext;

/**
 * Created by edo on 04/12/2015.
 */

public class Application {
    public static void main(String[] args) {
        FrmMain m = (FrmMain) AppContext.getBean("mainFrameBean");
        m.setVisible(true);
    }


}
