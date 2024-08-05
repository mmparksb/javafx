package com.itgroup.controller;

import com.itgroup.bean.Product;
import com.itgroup.dao.ProductDao;
import com.itgroup.utility.Paging;
import com.itgroup.utility.Utility;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.util.*;

public class POSController implements Initializable {
    @FXML private TableView<Product> productTableView, shoppingCart;
    @FXML private Pagination pagination;
    @FXML private ImageView imageView;
    @FXML private Label pageStatus, totalPrice;
    @FXML private ComboBox<String> categoryComboBox;
    @FXML private TextField searchTextField;

    private ProductDao dao = null;
    private String mode = null; // 카테고리
    private String temp = null; // 검색어
    private int number = 0;
    // number 는 물품 속성 0 = all / 1 = 행사상품 / 2 = 인기상품 / 3 = 검색 / 4 = 카테고리 선택

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dao = new ProductDao();

        setTableColumns();
        setPagination(0);

//        categoryComboBox.getSelectionModel().select(0);
        ChangeListener<Product> tableListener = new ChangeListener<Product>() {
            @Override
            public void changed(ObservableValue<? extends Product> observableValue, Product oldValue, Product newValue) {
                if (newValue != null) {
                    System.out.println("상품 정보");
                    System.out.println(newValue);

                    String imageFile = Utility.IMAGE_PATH + "defaultimage.png";
                    if (newValue.getImage() != null) {
                        imageFile = Utility.IMAGE_PATH + newValue.getImage().trim();
                    }
                    Image someImage = null;
                    if (getClass().getResource(imageFile) != null) {
                        someImage = new Image(getClass().getResource(imageFile).toString());
                        imageView.setImage(someImage);
                    } else {
                        setDefaultImage();
                    }
                }
            }
        };
        productTableView.getSelectionModel().selectedItemProperty().addListener(tableListener);

    }

    private void setTableColumns() {
        TableColumn tableColumn = null;
        String[] fields = {"name", "price", "stock", "best", "event"};
        String[] columnName = {"이름", "가격", "재고", "인기", "행사"};
        for (int i = 0; i < fields.length; i++) {
            tableColumn = productTableView.getColumns().get(i);
            tableColumn.setText(columnName[i]);
            tableColumn.setCellValueFactory(new PropertyValueFactory<>(fields[i]));

            tableColumn.setStyle("-fx-alignment: center; -fx-opacity: 0.9;");
        }
        shoppingCart.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        shoppingCart.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("count"));
        shoppingCart.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("shoppingCartPrice"));
        shoppingCart.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("event"));

        Image cat = new Image(getClass().getResource(Utility.IMAGE_PATH + "cat.gif").toString(), 682, 492, false, false);

        BackgroundImage bi = new BackgroundImage(cat, null, null, null, null);
        pagination.setBackground(new Background(bi));

        productTableView.setStyle("-fx-opacity: 0.9;");
    }

    public void setDefaultImage(){
        String name = Utility.IMAGE_PATH + "defaultimage.png";
        Image image = new Image(getClass().getResource(name).toString());
        imageView.setImage(image);
    }

    private void setPagination(int pageIndex) {

        if (number == 3) {
            categoryComboBox.getSelectionModel().select(0);
        }
        pagination.setPageFactory(this::createPage);
        pagination.setCurrentPageIndex(pageIndex);

        setDefaultImage();
        if (number != 4) {
            this.mode = "all";
        }
        searchTextField.clear();

        if (number == 0) {
            categoryComboBox.getSelectionModel().select(0);
        }
    }

    private Node createPage(Integer pageIndex) {
        int totalCount = 0;
        Paging pageInfo = null;
        totalCount = dao.getTotalCount(number, mode, temp);

        if (number == 0 || number == 4) {
            pageInfo = new Paging(String.valueOf(pageIndex + 1), "10", totalCount, null, mode, null);
        } else {
            pageInfo = new Paging(String.valueOf(pageIndex + 1), "10", totalCount, null, "all", null);
        }

        pagination.setPageCount(pageInfo.getTotalPage());

        fillTableData(pageInfo);

        VBox vbox = new VBox(productTableView);

        return vbox;
    }

    private void fillTableData(Paging pageInfo) {
        List<Product> productList = dao.getPaginationData(pageInfo, number, temp);
        ObservableList<Product> dataList  = FXCollections.observableArrayList(productList);

        productTableView.setItems(dataList);
        pageStatus.setText(pageInfo.getPagingStatus());
    }

    public void choiceCategory(ActionEvent event) {
        if (categoryComboBox.getSelectionModel().getSelectedIndex() == 1) {
            number = 2;
        } else if (categoryComboBox.getSelectionModel().getSelectedIndex() == 2) {
            number = 1;
        } else {
            String _category = categoryComboBox.getSelectionModel().getSelectedItem();
            String category = "";
            switch (_category) {
                case "전체 보기":
                    category = "all";
                    break;
                case "김밥":
                    category = "GIMBAP";
                    break;
                case "소주":
                    category = "SOJU";
                    break;
                case "맥주":
                    category = "BEER";
                    break;
                case "빵":
                    category = "BREAD";
                    break;
                case "음료수":
                    category = "BEVERAGE";
                    break;
                case "과자":
                    category = "SNACK";
                    break;
                case "라면/면":
                    category = "NOODLE";
                    break;
            }
            this.mode = category;
            number = 4;
        }
        setPagination(0);
    }

    public void searchText(ActionEvent event) {
        this.temp = searchTextField.getText();
        number = 3;
        setPagination(0);
    }

    public void productToReceipt(ActionEvent event) {
        if (productTableView.getSelectionModel().getSelectedIndex() < 0) {
            Utility.showAlert(Alert.AlertType.ERROR, new String[]{"상품 선택 확인", "상품 미선택" , "상품을 선택해 주세요."});
        } else {
            Product product = productTableView.getSelectionModel().getSelectedItem();

            for (Product bean : shoppingCart.getItems()) {
                if (product.getName().equals(bean.getName())) {
                    product = bean;
                }
            }

            if (product.getStock() > product.getCount()) {
                if (product.getCount() == 0) {
                    shoppingCart.getItems().add(product);
                    product.setCount(product.getCount() + 1);
                    product.setShoppingCartPrice(product.getPrice() * product.getCount());
                } else {
                    product.setCount(product.getCount() + 1);
                    if (product.getEvent() == null) {
                        product.setShoppingCartPrice(product.getPrice() * product.getCount());
                    } else if (product.getEvent().equals("1+1")) {
                        product.setShoppingCartPrice(product.getCount() / 2 * product.getPrice() + product.getCount() % 2 * product.getPrice());
                    } else if (product.getEvent().equals("2+1")) {
                        if (product.getCount() % 3 == 0) {
                            product.setShoppingCartPrice(product.getCount() * 2 / 3 * product.getPrice());
                        } else {
                            product.setShoppingCartPrice(product.getCount() * 2 / 3 * product.getPrice() + product.getPrice());
                        }
                    }
                }
                int _totalPrice = 0;
                for (Product bean : shoppingCart.getItems()) {
                    _totalPrice += bean.getShoppingCartPrice();
                }
                totalPrice.setText(new DecimalFormat("###,###,###").format(_totalPrice) + " 원");

            } else {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("재고 부족");
                alert.setHeaderText("재고가 부족합니다.");
                alert.setContentText("발주 넣어라..");
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.show();
            }
        }
    }

    public void deleteShoppingCartProduct(ActionEvent event) {
        Product bean = shoppingCart.getSelectionModel().getSelectedItem();
        if (bean != null) {
            shoppingCart.getItems().remove(bean);
            bean.setShoppingCartPrice(0);
            bean.setCount(0);
            int _totalPrice = 0;
            for (Product item : shoppingCart.getItems()) {
                _totalPrice += item.getShoppingCartPrice();
            }
            totalPrice.setText(String.valueOf(new DecimalFormat("###,###,###").format(_totalPrice)) + " 원");

            shoppingCart.getSelectionModel().select(null);
        } else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("안내");
            alert.setHeaderText("상품 미선택");
            alert.setContentText("장바구니에서 삭제할 상품을 선택해 주세요.");
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.show();
        }
    }

    public void payAndRecord(ActionEvent event) {
        boolean bool = true;
        for (Product item : shoppingCart.getItems()) {
            if (item.getEvent()==null){

            }else if (item.getEvent().toString().equals("1+1")) {
                if (item.getCount() % 2 == 1) {
                    Utility.showAlert(Alert.AlertType.INFORMATION, new String[]{"행사상품 미선택", item.getName() + "은(는) 1+1 상품 입니다.", "1개를 더 담아주세요."});
                    bool = false;
                }
            } else if (item.getEvent().toString().equals("2+1")) {
                if (item.getCount() % 3 == 2) {
                    Utility.showAlert(Alert.AlertType.INFORMATION, new String[]{"행사상품 미선택", item.getName() + "은(는) 2+1 상품 입니다.", "1개를 더 담아주세요."});
                    bool = false;
                }
            }
            if (bool) {
                if(item.getExpirationDate()==null){

                }else if (LocalDate.now().isAfter(Utility.getDatePicker(item.getExpirationDate()))) {
                    Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유통기한 확인", "유통기한이 지난 상품이 포함되어 있습니다.", item.getName() + "의 유통기한을 확인하세요."});
                    bool = false;
                } else if (LocalDate.now().isEqual(Utility.getDatePicker(item.getExpirationDate()))) {
                    Utility.showAlert(Alert.AlertType.WARNING, new String[]{"유통기한 확인", "유통기한이 오늘인 상품이 포함되어 있습니다.", item.getName() + "의 유통기한을 확인하세요."});
                    bool = false;
                }
            }
        }

        if (bool) {
            if (shoppingCart.getItems().size() != 0) {
                // 결제 방식 선택
                String payment = null;
                Alert alertPayment = new Alert(Alert.AlertType.CONFIRMATION);
                alertPayment.initModality(Modality.APPLICATION_MODAL);
                alertPayment.setTitle("결제 방식");
                alertPayment.setHeaderText("결제 방식을 선택해주세요.");
                alertPayment.getButtonTypes().setAll(new ButtonType("현금"), new ButtonType("카드"), new ButtonType("종료"));

                Optional<ButtonType> response = alertPayment.showAndWait();
                if (response.get().getText().equals("현금")) {
                    payment = "현금";
                } else if (response.get().getText().equals("종료")) {
                    alertPayment.close();
                    System.out.println("결제가 취소됩니다.");
                    return;
                } else {
                    payment = "카드";
                }

                // 데이터 베이스 상품 정보 반영
                boolean purchaseSuccess = true;
                // 중간에 정보 변경 실패 시 다시 원상복구 하기 위한 list
                List<Product> list = new ArrayList<Product>();
                for (Product bean : shoppingCart.getItems()) {
                    Product temp = bean; // 정보 변경 실패를 우려하여 정보 저장.
                    bean.setStock(bean.getStock() - bean.getCount());
                    if (bean.getStock() == 0) {
                        bean.setManufactureDate(null);
                        bean.setExpirationDate(null);
                    }
                    if (bean.getManufactureDate() != null) {
                        bean.setManufactureDate(Utility.getDateString(bean.getManufactureDate()));
                    }
                    if (bean.getExpirationDate() != null) {
                        bean.setExpirationDate(Utility.getDateString(bean.getExpirationDate()));
                    }
                    int cnt = dao.updateProduct(bean);

                    if (cnt == -1) {
                        purchaseSuccess =false;
                        Utility.showAlert(Alert.AlertType.ERROR, new String[]{"구매 실패", "데이터 베이스 업데이트 오류", "구매한 물품의 상품정보 반영 중 오류가 발생했습니다."});
                        break;
                    } else {
                        System.out.println(bean.getName() + " 상품의 정보가 변경되었습니다.");
                        list.add(temp); // 정보 변경에 성공한 상품 저장.
                    }
                }


                if (purchaseSuccess) {
                    // 판매 내역 ( 영수증 ) 기록
                    Calendar now = Calendar.getInstance();
                    DecimalFormat df = new DecimalFormat("00");
                    int year = now.get(Calendar.YEAR);
                    int month = now.get(Calendar.MONTH) + 1;
                    int day = now.get(Calendar.DAY_OF_MONTH);
                    int hour = now.get(Calendar.HOUR_OF_DAY);
                    int minute = now.get(Calendar.MINUTE);
                    int second = now.get(Calendar.SECOND);

                    String directoryName = System.getProperty("user.dir") + Utility.RECEIPT_PATH + year + "-" + df.format(month) + "-" + df.format(day) + "\\";
                    String fileName = directoryName + df.format(hour) + "시" + df.format(minute) + "분" + df.format(second) + "초.txt";
                    File file = new File(directoryName);
                    if (!file.exists()) {
                        if (file.mkdir()) {
                            System.out.println(file + "폴더가 생성되었습니다.");
                        }
                    }
                    BufferedWriter bw = null;
                    try {
                        if (!new File(fileName).exists()) {
                            if (new File(fileName).createNewFile()) {
                                System.out.println(fileName + "이 생성되었습니다.");
                            }
                        }
                        bw = new BufferedWriter(new FileWriter(new File(fileName)));
                        bw.write(year + "년" + df.format(month) + "월" + df.format(day) + "일" + df.format(hour) + "시" + df.format(minute) + "분" + df.format(second) + "초");
                        bw.newLine();
                        bw.write("결제 방식 : " + payment);
                        bw.newLine();
                        bw.write("결제 금액 : " + totalPrice.getText());
                        for (Product bean : shoppingCart.getItems()) {
                            bw.newLine();
                            bw.write("상품명 : " + bean.getName() + ", 개수 : " + bean.getCount() + ", 가격 : " + bean.getShoppingCartPrice());
                            if (bean.getEvent() != null) {
                                bw.write(", 비고 : " + bean.getEvent());
                            }
                        }
                        shoppingCart.getItems().clear();
                        categoryComboBox.getSelectionModel().select(0);
                        totalPrice.setText("0 원");
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        if (bw != null) {
                            try {
                                bw.close();
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                } else {
                    // 결제 중 문제 발생 시 변경된 정보 원상복귀
                    for (Product bean : list) {
                        int cnt = dao.updateProduct(bean);
                        if (cnt != -1) {
                            System.out.println(bean.getName() + "의 상품 정보가 원래대로 돌아왔습니다.");
                        }
                    }
                }
            } else {
                Alert alertInfo = new Alert(Alert.AlertType.INFORMATION);
                alertInfo.setTitle("안내");
                alertInfo.setHeaderText("상품이 없습니다.");
                alertInfo.setContentText("장바구니에 상품을 담아주세요.");
                alertInfo.initModality(Modality.APPLICATION_MODAL);
                alertInfo.show();
            }
            number = 0;
            setPagination(0);
        }
    }

    public void insertProduct(ActionEvent event) throws Exception{
        String fxmlFile = Utility.FXML_PATH + "InsertProduct.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));

        Parent container = fxmlLoader.load() ; // 승급
        Scene scene = new Scene(container);
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("상품 등록");
        stage.setScene(scene);
        stage.showAndWait();

        number = 0;
        setPagination(0);
    }

    public void updateProduct(ActionEvent event) throws Exception {
        if (productTableView.getSelectionModel().getSelectedIndex() < 0) {
            Utility.showAlert(Alert.AlertType.ERROR, new String[]{"상품 선택 확인", "상품 미선택" , "수정하고자 하는 상품을 선택해 주세요."});
        } else {
            String fxmlFile = Utility.FXML_PATH + "UpdateProduct.fxml";
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));

            Parent container = fxmlLoader.load(); // 승급

            Product bean = productTableView.getSelectionModel().getSelectedItem();
            UpdateProductController controller = fxmlLoader.getController();
            controller.setBean(bean);

            Scene scene = new Scene(container);
            Stage stage = new Stage();

            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.setTitle("상품 정보 수정");
            stage.setScene(scene);
            stage.showAndWait();

            number = 0;
            setPagination(0);
            shoppingCart.getItems().clear(); //
            totalPrice.setText("0 원");
        }
    }

    public void deleteProduct(ActionEvent event) {
        if (productTableView.getSelectionModel().getSelectedIndex() < 0) {
            Utility.showAlert(Alert.AlertType.ERROR, new String[]{"상품 선택 확인", "상품 미선택" , "삭제하고자 하는 상품을 선택해 주세요."});
        } else {
            Product bean = productTableView.getSelectionModel().getSelectedItem();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("삭제 확인");
            alert.setHeaderText("상품을 삭제합니다.");
            alert.setContentText("정말로 " + bean.getName() + " 상품의 정보를 삭제하시겠습니까?");
            Optional<ButtonType> response = alert.showAndWait();
            if (response.get() == ButtonType.OK) {
                int barcode = bean.getBarcode();
                int cnt = -1;
                cnt = dao.deleteProduct(barcode);
                if (cnt == -1) {
                    Utility.showAlert(Alert.AlertType.ERROR, new String[]{"삭제 실패", "데이터 베이스 삭제 오류", bean.getName() + " 상품 삭제에 실패했습니다."});
                } else {
                    Utility.showAlert(Alert.AlertType.INFORMATION, new String[]{"삭제 성공", "데이터 베이스 삭제 성공", bean.getName() + " 상품 삭제에 성공했습니다."});
                    number = 0;
                    setPagination(0);
                }
            } else {
                System.out.println("상품 삭제를 취소하였습니다.");
            }
            alert.close();
        }

    }

    public void salesCheck(ActionEvent event) throws Exception{
        String fxmlFile = Utility.FXML_PATH + "SalesCheck.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));

        Parent container = fxmlLoader.load(); // 승급

        Scene scene = new Scene(container);
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("매출 확인");
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void manageProduct(ActionEvent event) throws Exception{
        String fxmlFile = Utility.FXML_PATH + "ManageProduct.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));

        Parent container = fxmlLoader.load(); // 승급

        Scene scene = new Scene(container);
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("재고 관리");
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void openReceipt(ActionEvent event) throws Exception{
        String fxmlFile = Utility.FXML_PATH + "CheckReceipt.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));

        Parent container = fxmlLoader.load(); // 승급

        Scene scene = new Scene(container);
        Stage stage = new Stage();

        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.setTitle("거래 내역 확인");
        stage.setScene(scene);
        stage.showAndWait();
    }

    public void clear(ActionEvent event) {
        number = 0;
        setPagination(0);
    }
}
