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

public class UpdateProductController implements Initializable {
    @FXML
    private TextField fxmlBest, fxmlBarcode, fxmlName, fxmlPrice, fxmlStock, fxmlImage;
    @FXML
    private ComboBox<String> fxmlCategory, fxmlEvent;
    @FXML
    private DatePicker fxmlExpirationDate, fxmlManufactureDate;

    ProductDao dao = null;
    Product oldbean = null;
    Product newbean = null;



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dao = new ProductDao();
        fxmlExpirationDate.setEditable(false);
        fxmlManufactureDate.setEditable(false);
    }

    public void setBean(Product bean) {
        this.oldbean = bean;

        fxmlBest.setEditable(false);

        fxmlName.setText(oldbean.getName());
        fxmlPrice.setText(String.valueOf(oldbean.getPrice()));
        fxmlStock.setText(String.valueOf(oldbean.getStock()));
        fxmlImage.setText(oldbean.getImage());
        switch (oldbean.getCategory()) {
            case "GIMBAP":
                fxmlCategory.getSelectionModel().select(0);
                break;
            case "SOJU":
                fxmlCategory.getSelectionModel().select(1);
                break;
            case "BEER":
                fxmlCategory.getSelectionModel().select(2);
                break;
            case "BREAD":
                fxmlCategory.getSelectionModel().select(3);
                break;
            case "BEVERAGE":
                fxmlCategory.getSelectionModel().select(4);
                break;
            case "SNACK":
                fxmlCategory.getSelectionModel().select(5);
                break;
            case "NOODLE":
                fxmlCategory.getSelectionModel().select(6);
                break;
        }

        fxmlEvent.getSelectionModel().select(0);

        for (String a : fxmlEvent.getItems()) {
            if (a.equals(oldbean.getEvent())) {
                fxmlEvent.getSelectionModel().select(a);
            }
        }
        fxmlBest.setText(oldbean.getBest());

        if (oldbean.getExpirationDate() != null) {
            fxmlExpirationDate.setValue(Utility.getDatePicker(oldbean.getExpirationDate()));
        }

        if (oldbean.getManufactureDate() != null) {
            fxmlManufactureDate.setValue(Utility.getDatePicker(oldbean.getManufactureDate()));
        }

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

    public void onProductUpdate(ActionEvent event) {
        if (validationCheck()) {
            ProductDao dao = new ProductDao();
            int cnt = -1; // -1이면 실패
            cnt = dao.updateProduct(newbean);
            if (cnt == -1) {
                Utility.showAlert(Alert.AlertType.ERROR, new String[]{"정보 수정", "정보 수정 실패", "정보 수정이 실패했습니다."});
                System.out.println("업데이트 실패");
            } else {
                Node source = (Node) event.getSource(); // 강등
                Stage stage = (Stage) source.getScene().getWindow();//강등
                Utility.showAlert(Alert.AlertType.INFORMATION, new String[]{"정보 수정", "정보 수정 성공", "정보 수정이 완료되었습니다."});
                stage.close();
                System.out.println("업데이트 성공");
            }
        } else {
            System.out.println("유효성 검사 통과 실패");
        }
    }

    private boolean validationCheck() {
        boolean bool = true;
        newbean = new Product();

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

        if (image.length() < 5 || image.length() > 20) {
            Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "이미지 입력 오류", "이미지 파일은 20글자 이하로 입력해야합니다."});
            return false;
        }
        boolean a = image.endsWith("jpg") || image.endsWith("png");
        if (!a) {
            Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "이미지 입력 오류", "이미지 파일은 jpg 또는 png 파일만 가능합니다."});
            return false;
        }
        if (!new File(System.getProperty("user.dir") + "\\src\\main\\resources" + Utility.IMAGE_PATH + image).exists()) {
            Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "이미지 파일 오류", "존재하지 않는 이미지 파일입니다."});
            return false;
        }


        String category = null;
        if (fxmlCategory.getSelectionModel().getSelectedIndex() < 0) {
            Utility.showAlert(Alert.AlertType.ERROR, new String[]{"유효성 검사", "카테고리 미선택", "카테고리를 선택해 주세요."});
            return false;
        } else {
            switch (fxmlCategory.getSelectionModel().getSelectedIndex()) {
                case 0:
                    category = "GIMBAP";
                    break;
                case 1:
                    category = "SOJU";
                    break;
                case 2:
                    category = "BEER";
                    break;
                case 3:
                    category = "BREAD";
                    break;
                case 4:
                    category = "BEVERAGE";
                    break;
                case 5:
                    category = "SNACK";
                    break;
                case 6:
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

        newbean.setBarcode(oldbean.getBarcode());
        newbean.setName(name);
        newbean.setPrice(price);
        newbean.setStock(stock);
        newbean.setImage(image);
        newbean.setCategory(category);
        newbean.setEvent(event);
        newbean.setBest(best);
        newbean.setExpirationDate(expirationDate);
        newbean.setManufactureDate(manufactureDate);

        return bool;
    }

    public void clearManufactureDate(ActionEvent event) {
        fxmlManufactureDate.setValue(null);
    }

    public void clearExpirationDate(ActionEvent event) {
        fxmlExpirationDate.setValue(null);
    }
}
