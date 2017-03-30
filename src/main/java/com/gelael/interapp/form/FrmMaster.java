/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gelael.interapp.form;

import com.gelael.interapp.service.MasterService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class FrmMaster extends javax.swing.JFrame {

    private JPanel pnlMaster;
    private JFormattedTextField txtDateUpd;
    private JButton btnGetDataHO;
    private JTextPane txtInfo;
    private JButton btnKeluar;
    private JButton btnExportDBF;
    private static final Logger logger = LogManager.getLogger(FrmMaster.class.getName());

    @Autowired
    MasterService masterService;

    private boolean isSuccessMdw = false;

    public String infoNotif = "";
    public static String infoDateBeingExecute = "";

    /**
     * Creates new form FrmMain
     */
    public FrmMaster() {
        this.setResizable(false);
        this.setName("Aplikasi Interface Gelael-Terima Master");
        this.setSize(640,480);
        this.setContentPane(pnlMaster);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.setValidCharacters("0123456789");
            txtDateUpd.setFormatterFactory(new DefaultFormatterFactory(mf));
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Kesalahan Format Tanggal");
        }
        Thread threadInfo = new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    while (true) {
                        txtInfo.setText(infoNotif);
                        Thread.sleep(300);
                    }
                } catch (InterruptedException ex) {
                }
            }
        });
        threadInfo.start();

        btnKeluar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goExit();
            }
        });
        btnGetDataHO.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doGetDataFromHO();
            }
        });
        btnExportDBF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doExtractDBF();
            }
        });
    }

    private void goExit() {
        this.dispose();
    }

    private void doGetDataFromHO() {
        // TODO add your handling code here:
        if (infoDateBeingExecute.equals(txtDateUpd.getText())) {
            infoNotif = "Tanggal yang sama sedang dalam proses";
        } else {
            logger.info("User Ambil Master " + txtDateUpd.getText());
            infoNotif = "Harap tunggu...";
            infoDateBeingExecute = txtDateUpd.getText();
            btnGetDataHO.setEnabled(false);

            Thread threadGetData = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        String dateReq = txtDateUpd.getText();
                        infoNotif = masterService.getDataFromMDW(dateReq);
                        txtDateUpd.setEditable(false);
                        isSuccessMdw = true;
                        infoNotif = infoNotif + "\n Ambil Data Dari HO Berhasil, Silahkan melanjutkan Ekspor DBF";
                    } catch (Exception ex) {
                        logger.error("Ambil Master Dari HO Gaggal " + ex);
                        isSuccessMdw = false;
                        btnGetDataHO.setEnabled(true);
                        infoNotif = "Ambil Data Dari HO Gagal \n";
                    } finally {
                        txtDateUpd.setEditable(true);
                        infoDateBeingExecute = "";
                    }
                }
            });
            threadGetData.start();
        }
    }

    private void doExtractDBF() {
        if (infoDateBeingExecute.equals(txtDateUpd.getText())) {
            infoNotif = "Tanggal yang sama sedang dalam proses";
        } else {
            logger.info("User Membentuk File Master DBF " + txtDateUpd.getText());
            infoDateBeingExecute = txtDateUpd.getText();
            Thread threadExt = new Thread(new Runnable() {

                @Override
                public void run() {
                    try {
                        if (isSuccessMdw) {
                            masterService.doExtractDBF(txtDateUpd.getText());
                            infoNotif = infoNotif + "\n Ekspor DBF Sukses";
                        } else {
                            infoNotif = "Silahkan melakukan pengambilan data dari HO terlebih dahulu";
                        }
                    } catch (Exception ex) {
                        logger.error("Buat File Master DBF Gagal " + ex);
                        infoNotif = infoNotif + "\n Ekspor DBF Gagal";
                    } finally {
                        txtDateUpd.setEditable(true);
                        btnGetDataHO.setEnabled(true);
                        infoDateBeingExecute = "";
                    }
                }
            });
            threadExt.start();
        }
    }
}
