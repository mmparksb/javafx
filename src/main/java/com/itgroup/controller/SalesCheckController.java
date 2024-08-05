package com.itgroup.controller;

import com.itgroup.utility.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;

import java.io.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SalesCheckController implements Initializable {
    @FXML
    private LineChart<String, Integer> lineChart;
    @FXML
    private DatePicker datePicker;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fillDataChart();
    }

    private void fillDataChart() {
        XYChart.Series<String, Integer> series = new XYChart.Series<String, Integer>();
        series.setName("매출액");

        XYChart.Data<String, Integer> data = new XYChart.Data<String, Integer>();
        BufferedReader br = null;
        FileWriter fr = null;
        File file = null;
        Calendar today = Calendar.getInstance();
        DecimalFormat df = new DecimalFormat("00");
        String pathName = System.getProperty("user.dir") + Utility.RECEIPT_PATH;
        String fileName = today.get(Calendar.YEAR) + "-" + df.format(today.get(Calendar.MONTH) + 1) + "-" + df.format(today.get(Calendar.DAY_OF_MONTH));

        try {
            int weekendTotalPrice = 0;
            LocalDate now = LocalDate.now();
            for (int i = 6; i >= 0; i--) {
                fileName = now.minusDays(i).toString();

                file = new File(pathName + fileName + "\\");
                if (file.exists()) {
                    int totalPrice = 0;
                    for (File a : file.listFiles()) {
                        br = new BufferedReader(new FileReader(a));
                        String oneline = null;
                        while ((oneline = br.readLine()) != null) {
                            if (oneline.indexOf("결제 금액") >= 0) {
                                int e = -1;
                                e = oneline.indexOf(",");
                                if (e != -1) {
                                    totalPrice += Integer.valueOf(oneline.substring(8, e)) * 1000;
                                    totalPrice += Integer.valueOf(oneline.substring(e + 1, e + 4));
                                }
                            }
                        }
                    }
                    series.getData().add(new XYChart.Data<>(fileName, totalPrice));
                    System.out.println(fileName + " 총 매출액 : " + new DecimalFormat("###,###,###,###").format(totalPrice) + "원");
                    weekendTotalPrice += totalPrice;
                } else {
                    series.getData().add(new XYChart.Data<>(fileName, 0));
                    System.out.println(fileName + " 총 매출액 : 0원");
                }
            }
            lineChart.getData().add(series);
            lineChart.setTitle("최근 1주일 매출액 : " + new DecimalFormat("###,###,###,###").format(weekendTotalPrice) + "원");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    public void searchDateSales(ActionEvent event) {
        String pathName = System.getProperty("user.dir") + Utility.RECEIPT_PATH;
        String fileName = datePicker.getValue().toString();
        if (datePicker.getValue().isAfter(LocalDate.now())) {
            Utility.showAlert(Alert.AlertType.ERROR, new String[]{"날짜 선택 오류", "오늘까지만 선택 가능합니다.", "내일부터를 어떻게 조회할까요.."});
        } else {
            BufferedReader br = null;

            try {
                File file = new File(pathName + fileName + "\\");
                if (file.exists()) {
                    int totalPrice = 0;
                    for (File a : file.listFiles()) {
                        br = new BufferedReader(new FileReader(a));
                        String oneline = null;
                        while ((oneline = br.readLine()) != null) {
                            if (oneline.indexOf("결제 금액") >= 0) {
                                int e = 0;
                                e = oneline.indexOf(",");
                                if (e != 0) {
                                    totalPrice += Integer.valueOf(oneline.substring(8, e)) * 1000;
                                    totalPrice += Integer.valueOf(oneline.substring(e + 1, e + 4));
                                }
                            }
                        }
                    }

                    Utility.showAlert(Alert.AlertType.INFORMATION, new String[]{"매출액", fileName + "매출액", fileName + " 매출액은 " + new DecimalFormat("###,###,###").format(totalPrice) + "원입니다."});
                } else {
                    Utility.showAlert(Alert.AlertType.INFORMATION, new String[]{"매출액", fileName + "매출액", fileName + " 매출액은 0원입니다."});
                }


            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }
}
