package com.itgroup.controller;

import com.itgroup.bean.Product;
import com.itgroup.dao.ProductDao;
import com.itgroup.utility.Utility;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ManageProduct implements Initializable {
    @FXML private TableView<Product> lackStock, lackExpirationDate;
    private ProductDao dao = null;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dao = new ProductDao();
        setTableCoumn();
        List<Product> lackStockList = dao.selectByLackStock();
        lackStock.setItems(FXCollections.observableArrayList(lackStockList));

        List<Product> _lackExpirationDateList = dao.selectByLackExpirationDate();
        List<Product> lackExpirationDateList = new ArrayList<Product>();
        for (Product bean : _lackExpirationDateList) {
            bean.setExpirationDate(Utility.getDateString(bean.getExpirationDate()));
            lackExpirationDateList.add(bean);
        }

        lackExpirationDate.setItems(FXCollections.observableArrayList(lackExpirationDateList));
    }

    private void setTableCoumn() {
        lackStock.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        lackStock.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("stock"));
        lackExpirationDate.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("name"));
        lackExpirationDate.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("expirationDate"));
    }

}
