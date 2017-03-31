package com.gelael.interapp.form;

import com.gelael.interapp.service.PaymentService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import javax.swing.*;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.MaskFormatter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;

/**
 * Created by 15050978 on 3/31/2017.
 */
public class FrmPayment extends javax.swing.JFrame {
    private JPanel pnlPayment;
    private JFormattedTextField txtDateUpd;
    private JButton btnKeluar;
    private JButton btnExportDBF;
    private JTextArea txtInfo;
    private static final Logger logger = LogManager.getLogger(FrmMaster.class.getName());

    @Autowired
    PaymentService paymentService;

    public String infoNotif = "";
    public static String infoDateBeingExecute = "";

    public FrmPayment() {
        this.setResizable(false);
        this.setName("Aplikasi Interface Gelael-Pembuatan Data Pembayaran");
        this.setSize(640, 480);
        this.setContentPane(pnlPayment);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try {
            MaskFormatter mf = new MaskFormatter("##/##/####");
            mf.setValidCharacters("0123456789");
            txtDateUpd.setFormatterFactory(new DefaultFormatterFactory(mf));
        } catch (ParseException ex) {
            JOptionPane.showMessageDialog(null, "Kesalahan Format Tanggal");
        }
        btnExportDBF.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                doExtractDBF();
            }
        });
        btnKeluar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                goExit();
            }
        });
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
    }

    private void goExit() {
        this.dispose();
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
                        txtDateUpd.setEditable(false);
                        paymentService.doExtractDBF(txtDateUpd.getText());
                        infoNotif = infoNotif + "\n Ekspor DBF Sukses";
                    } catch (Exception ex) {
                        logger.error("Buat File Master DBF Gagal " + ex);
                        infoNotif = infoNotif + "\n Ekspor DBF Gagal";
                    } finally {
                        txtDateUpd.setEditable(true);
                        infoDateBeingExecute = "";
                    }
                }
            });
            threadExt.start();
        }
    }
}
