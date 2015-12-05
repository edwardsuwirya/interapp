/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gelael.interapp.context;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author edo
 */
public class AppContext {

    public static ApplicationContext getAppContext() {
        return new ClassPathXmlApplicationContext(new String[]{"applicationContext.xml"});
    }

    public static Object getBean(String beanName) {
        return getAppContext().getBean(beanName);
    }
}
