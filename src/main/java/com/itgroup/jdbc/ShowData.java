package com.itgroup.jdbc;

import com.itgroup.bean.Member;

import java.text.DecimalFormat;

public class ShowData {
    public static void printData(Member bean) {
        System.out.println("ID : " + bean.getId());
        System.out.println("이름 : " + bean.getName());
        System.out.println("주민 등록 번호 : " + bean.getSsn());
        System.out.println("주소 : " + bean.getAddress());
        System.out.println("성별 : " + bean.getGender());
        System.out.println("이메일 : " + bean.getEmail());
        System.out.println("입사 일자 : " + bean.getHiredate().substring(0, 10));
    }
}