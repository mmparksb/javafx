package com.itgroup.utility;

import javafx.scene.control.Alert;
import javafx.stage.Modality;

import java.time.LocalDate;

public class Utility {
    public final static String FXML_PATH = "/com.itgroup/fxml/";
    public final static String IMAGE_PATH = "/com.itgroup/images/";
    public final static String RECEIPT_PATH = "\\src\\main\\java\\com\\itgroup\\data\\receipt\\";


    public static void showAlert(Alert.AlertType alertType, String[] message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(message[0]);
        alert.setHeaderText(message[1]);
        alert.setContentText(message[2]);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.showAndWait();
    }

    public static LocalDate getDatePicker(String inputDate) {
        // 문자 열을 LocalDate 타입으로 변환하여 반환합니다.
        // 회원 가입일자, 게시물 작성 일자, 상품 등록 일자 등에서 사용할 수 있습니다.
        int year = Integer.valueOf(inputDate.substring(0, 4));
        int month = Integer.valueOf(inputDate.substring(5, 7));
        int day = Integer.valueOf(inputDate.substring(8, 10));
        return LocalDate.of(year, month, day);
    }
    public static String getDateString(String inputDate) {
        // 문자 열을 LocalDate 타입으로 변환하여 반환합니다.
        // 회원 가입일자, 게시물 작성 일자, 상품 등록 일자 등에서 사용할 수 있습니다.
        String year = inputDate.substring(0, 4);
        String month = inputDate.substring(5, 7);
        String day = inputDate.substring(8, 10);
        return year + "/" + month + "/" + day;
    }
}
