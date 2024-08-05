package com.itgroup.controller;

import com.itgroup.bean.Product;
import com.itgroup.dao.ProductDao;
import com.itgroup.utility.Utility;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

public class InsertProductController implements Initializable {
    @FXML
    private TextField fxmlBest, fxmlBarcode, fxmlName, fxmlPrice, fxmlStock, fxmlImage;
    @FXML
    private ComboBox<String> fxmlCategory, fxmlEvent;
    @FXML
    private DatePicker fxmlExpirationDate, fxmlManufactureDate;

    ProductDao dao = null;
    Product bean = null;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dao = new ProductDao();
        fxmlCategory.getSelectionModel().select(0);
        fxmlEvent.getSelectionModel().select(0);
        fxmlBest.setEditable(false);
        fxmlExpirationDate.setEditable(false);
    }

    public void onBest(ActionEvent event) {
        if (fxmlBest.getText() == null) {
            fxmlBest.setText("BEST");
        } else if (!fxmlBest.getText().equals("BEST")) {
            fxmlBest.setText("BEST");
        } else {
            fxmlBest.setText(null);
        }
    }

    public void onProductInsert(ActionEvent event) {
        if (validationCheck()) {
            int cnt = -1;
            cnt = dao.insertProduct(bean);
            if (cnt == -1) {
                Utility.showAlert(Alert.AlertType.ERROR, new String[]{"상품 등록 실패", "데이터 베이스 입력 오류", "상품 등록에 실패했습니다."});
            } else {
                Node source = (Node) event.getSource(); // 강등
                Stage stage = (Stage) source.getScene().getWindow();//강등
                stage.close();
                Utility.showAlert(Alert.AlertType.INFORMATION, new String[]{"상품 등록", "상품 등록 완료", bean.getName()+" 상품 등록을 성공적으로 마쳤습니다."});
            }
        } else {
            System.out.println("유효성 검사 통과 실패");
        }
    }

    private boolean validationCheck() {
        boolean bool = true;
        bean = new Product();

        int barcode = 0;
        try {
            String _barcode = fxmlBarcode.getText().trim();
            barcode = Integer.valueOf(_barcode);
            if (_barcode.length() < 5 || _barcode.length() > 9) {
                Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "바코드 길이 오류", "바코드의 길이는 5개 이상, 9개 이하여야 합니다."});
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "바코드 입력 오류", "바코드는 숫자만 입력해야 합니다."});
            return false;
        }

        String name = fxmlName.getText();
            if (name.length() < 2 || name.length() > 12) {
                Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "상품명 입력 오류", "상품명의 길이는 2글자 이상 12글자 이하여야 합니다."});
                return false;
            }


        int price = 0;
        try {
            String _price = fxmlPrice.getText().trim();
            price = Integer.valueOf(_price);
            if (_price.length() < 1 || _price.length() > 100000) {
                Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "가격 오류", "가격은 1원 이상, 10만원 이하여야 합니다."});
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "가격 입력 오류", "가격은 숫자만 입력해야 합니다."});
            return false;
        }

        int stock = 0;
        try {
            String _stock = fxmlStock.getText().trim();
            stock = Integer.valueOf(_stock);
            if (_stock.length() < 1 || _stock.length() > 100) {
                Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "재고 오류", "재고는 1개 이상, 100개 이하여야 합니다."});
                return false;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "재고 입력 오류", "재고는 숫자만 입력해야 합니다."});
            return false;
        }

        String image = fxmlImage.getText().trim();

            if (image.length() < 5 || image.length() > 20 ) {
                Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "이미지 입력 오류", "이미지 파일은 20글자 이하로 입력해야합니다."});
                return false;
            }
            boolean a = image.endsWith("jpg")||image.endsWith("png");
        if (!a) {
            Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "이미지 입력 오류", "이미지 파일은 jpg 또는 png 파일만 가능합니다."});
            return false;
        }
            if (!new File(System.getProperty("user.dir") + "\\src\\main\\resources" + Utility.IMAGE_PATH + image).exists()) {
                Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "이미지 파일 오류", "존재하지 않는 이미지 파일입니다."});
                return false;
            }


        String category = null;
        if (fxmlCategory.getSelectionModel().getSelectedIndex() == 0) {
            Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "카테고리 미선택", "카테고리를 선택해 주세요."});
            return false;
        } else {
            switch (fxmlCategory.getSelectionModel().getSelectedIndex()) {
                case 1 :
                    category = "GIMBAP";
                    break;
                case 2:
                    category = "SOJU";
                    break;
                case 3:
                    category = "BEER";
                    break;
                case 4:
                    category = "BREAD";
                    break;
                case 5:
                    category = "BEVERAGE";
                    break;
                case 6:
                    category = "SNACK";
                    break;
                case 7 :
                    category = "NOODLE";
                    break;
            }
        }
        String event = null;
        if (fxmlEvent.getSelectionModel().getSelectedIndex() != 0) {
            event = fxmlEvent.getSelectionModel().getSelectedItem();
        }

        String best = null;
        if (fxmlBest.getText() == null) {

        } else if (fxmlBest.getText().equals("BEST")) {
            best = "BEST";
        }

        String expirationDate = null;
        if (fxmlExpirationDate != null) {
            LocalDate _expirationDate = fxmlExpirationDate.getValue();
            if (_expirationDate != null) {
                expirationDate = _expirationDate.toString();
                expirationDate.replace("-", "/");
            }
        }

        String manufactureDate = null;
        if (fxmlManufactureDate != null) {
            LocalDate _manufactureDate = fxmlManufactureDate.getValue();
            if (_manufactureDate != null) {
                manufactureDate = _manufactureDate.toString();
                manufactureDate.replace("-", "/");
            }
        }




        bean.setBarcode(barcode);
        bean.setName(name);
        bean.setPrice(price);
        bean.setStock(stock);
        bean.setImage(image);
        bean.setCategory(category);
        bean.setEvent(event);
        bean.setBest(best);
        bean.setExpirationDate(expirationDate);
        bean.setManufactureDate(manufactureDate);

        return bool;
    }

}
