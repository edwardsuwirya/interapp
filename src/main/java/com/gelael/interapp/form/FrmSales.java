/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gelael.interapp.form;

import com.gelael.interapp.service.SalesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

/**
 * @author edo
 */
@Component
public class FrmSales extends javax.swing.JFrame {
    private JPanel pnlSales;
    private JButton btnSales;
    private JTextPane txtInfo;
    private JButton btnKeluar;
    private JFormattedTextField txtTransDate;

    @Autowired
    private SalesService salesService;

    public String infoNotif = "";
    public static String infoDateBeingExecute = "";

    /**
     * Creates new form FrmSales
     */
    public FrmSales() {
        this.setResizable(false);
        this.setName("Aplikasi Interface Gelael - Kirim Sales");
        this.setSize(640, 480);
        this.setContentPane(pnlSales);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.setValidCharacters("0123456789");
            txtTransDate.setFormatterFactory(new DefaultFormatterFactory(mf));
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Kesalahan Format Tanggal");
        }
        Thread threadInfo = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    while (true) {
                        txtInfo.setText(infoNotif);
                        Thread.sleep(1000);
                    }
                } catch (InterruptedException ex) {
                }
            }
        });
        threadInfo.start();
        btnSales.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doSendSales();
            }
        });
        btnKeluar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goExit();
            }
        });
    }


    public void doSendSales() {
        // TODO add your handling code here:

        if (infoDateBeingExecute.equals(txtTransDate.getText())) {
            infoNotif = "Tanggal yang sama sedang dalam proses";
        } else {
            infoNotif = "Harap tunggu...";
            infoDateBeingExecute = txtTransDate.getText();
            btnSales.setEnabled(false);

            Thread threadSales = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        String transDate = txtTransDate.getText();
                        int result = salesService.updateSalesTransaction(transDate);
                        if (result == 0) {
                            String info = salesService.getSalesSimpleInfo(transDate);
                            infoNotif = info;
                            info = salesService.sendSales(transDate);
                            infoNotif = infoNotif + "\n" + info;
                        } else {
                            infoNotif = "Baca Sales DBF gagal";
                        }
                    } catch (Exception ex) {
                        infoNotif = "Kirim Sales Ke HO gagal \n";
                    } finally {
                        btnSales.setEnabled(true);
                        infoDateBeingExecute = "";
                    }
                }
            });

            threadSales.start();
        }
    }

    private void goExit() {
        this.dispose();
    }
}
