package com.itgroup.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

public class CheckReceiptController implements Initializable {
    @FXML ListView<String> directoryListView, fileListView ;
    @FXML TextArea fileContentArea ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        fileContentArea.setEditable(false);

        String pathName = System.getProperty("user.dir") + "\\src\\main\\java\\com\\itgroup\\data\\receipt\\";
        File directory = new File(pathName);
        List<String> directoryList = new ArrayList<String>();
        for (File name : directory.listFiles()) {
            if (name.isDirectory()) {
                directoryList.add(name.getName());
            }
        }
        Collections.sort(directoryList, Collections.reverseOrder());
        directoryListView.setItems(FXCollections.observableArrayList(directoryList));
        String fileName = null;

        ChangeListener<String> directoryListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                fileListView.getSelectionModel().select(-1);

                List<String> fileList = new ArrayList<String>();
                File file = new File(pathName + directoryListView.getSelectionModel().getSelectedItem().toString() + "\\");

                for (String name : file.list()) {
                    fileList.add(name);
                }
                Collections.sort(fileList, Collections.reverseOrder());
                fileListView.setItems(FXCollections.observableArrayList(fileList));

            }
        };
        directoryListView.getSelectionModel().selectedItemProperty().addListener(directoryListener);


        ChangeListener<String> fileListener = new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldFile, String newFile) {
                fileContentArea.clear();
                if (fileListView.getSelectionModel().getSelectedItem() == null) {

                } else {


                    try {
                        String fileName = pathName + directoryListView.getSelectionModel().getSelectedItem().toString() + "\\" + fileListView.getSelectionModel().getSelectedItem().toString();
                        if (new File(fileName).exists()) {
                            BufferedReader br = new BufferedReader(new FileReader(new File(fileName)));
                            System.out.println("선택된 파일 : " + fileName);
                            String oneline = null;
                            while ((oneline = br.readLine()) != null) {
                                fileContentArea.setText(fileContentArea.getText() + oneline + "\n");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {

                    }
                }
            }
        };
        fileListView.getSelectionModel().selectedItemProperty().addListener(fileListener);
    }
}
