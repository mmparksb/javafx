package com.itgroup.application;

import com.itgroup.utility.Utility;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class POSApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        String fxmlFile = Utility.FXML_PATH + "POSTest.fxml";
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(fxmlFile));

        Parent container = fxmlLoader.load() ; // 승급
        Scene scene = new Scene(container);
        stage.setTitle("POS 프로그래밍 ");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.show();
        stage.setOnCloseRequest(windowEvent -> {
            System.out.println("POS 프로그램이 종료됩니다.");
        });

    }

    public static void main(String[] args) {
        launch(args);
    }
}
