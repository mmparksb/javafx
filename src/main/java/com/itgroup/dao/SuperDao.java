package com.itgroup.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class SuperDao {
    private String driver = null;
    private String url = null;  // 데이터 베이스 출처
    private String id = null;   // 사용자 아이디
    private String password = null; // 사용자 비밀번호


    public SuperDao() {
        this.driver = "oracle.jdbc.driver.OracleDriver";
        this.url ="jdbc:oracle:thin:@localhost:1521:xe";
        this.id = "gomdori";
        this.password = "butterfly";

        try {
            Class.forName(driver);  // 동적 객체 생성
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    protected Connection getConnection(){
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, id, password);
            if (conn == null) {
                System.out.println("연결 실패");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return conn;
    }

}
