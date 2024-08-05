module com.itgroup.www {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires osdt.core;
    requires java.desktop; // for JDBC

    opens com.itgroup.www to javafx.fxml;
    exports com.itgroup.www;

    // JavaFX 애플리케이션에서 발생하는 접근 제어 문제
    // graphics 모듈이 application 폴더를 접근할 수 있도록 처리합니다.
    exports com.itgroup.application to javafx.graphics;

    // 모듈 com.itgroup.www가 com.itgroup.controller 패키지를 javafx.fxml 모듈에 공개합니다.
    opens com.itgroup.controller to javafx.fxml;
//
//    opens com.itgroup.bean to javafx.base;
    opens com.itgroup.application to javafx.fxml;

    opens com.itgroup.bean to javafx.base;

    // fxml 모듈이 com.itgroup.controller 패키지에 접근이 가능하도록 설정합니다.
    //    exports com.itgroup.controller to javafx.fxml;
}