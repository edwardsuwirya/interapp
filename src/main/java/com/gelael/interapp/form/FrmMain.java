/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gelael.interapp.form;

import com.gelael.interapp.context.AppContext;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author edo
 */
public class FrmMain extends javax.swing.JFrame {

    private JPanel pnlMain;
    private JButton btnSales;
    private JButton btnMaster;
    private JButton btnExit;

    /**
     * Creates new form FrmMain
     */
    public FrmMain() {
        this.setName("Aplikasi Interface Gelael");
        this.setContentPane(pnlMain);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(425, 200);
        this.setLocationRelativeTo(null);
        btnExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        btnSales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmSales m = (FrmSales) AppContext.getBean("salesFrameBean");
                m.setLocationRelativeTo(null);
                m.setVisible(true);
            }
        });
        btnMaster.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                FrmMaster m = (FrmMaster) AppContext.getBean("masterFrameBean");
                m.setLocationRelativeTo(null);
                m.setVisible(true);
            }
        });
    }

}
