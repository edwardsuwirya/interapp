package com.gelael.interapp.form;


import com.gelael.interapp.context.AppContext;
import com.gelael.interapp.service.TokenService;

import java.util.Date;

/**
 * Created by edo on 04/12/2015.
 */

public class Application {

    public static String TOKEN_SALES_KEY;
    public static String TOKEN_ITEM_KEY;

    public static void main(String[] args) {

        try {
            TokenService ts = (TokenService) AppContext.getBean("tokenBean");
            TOKEN_SALES_KEY = ts.getToken("sales");
            TOKEN_ITEM_KEY = ts.getToken("olditem");
        } catch (Exception ex) {
            System.out.println(new Date()+"... Can not contact main server");
            System.exit(0);
        }

        if (args[0].equalsIgnoreCase("nogui")) {
            NoGUIApplication noGUIApplication = (NoGUIApplication) AppContext.getBean("noGuiAppBean");
            if (args[1].equalsIgnoreCase("sales")) {
                if (args.length == 4) {
                    noGUIApplication.salesProcessInPeriod(args[2], args[3]);
                } else if (args.length == 3) {
                    noGUIApplication.salesProcessSingle(args[2]);
                } else {
                    System.out.println("Missing parameter");
                }
            } else if (args[1].equalsIgnoreCase("item")) {

            } else {
                System.out.println("Service is unknown");
            }
        } else if (args[0].equalsIgnoreCase("gui")) {
            FrmMain m = (FrmMain) AppContext.getBean("mainFrameBean");
            m.setVisible(true);
        } else {
            System.out.println(new Date()+"... Unknown paramter for executing main application");
            System.exit(0);
        }
    }


}
