package com.itgroup.jdbc;

import com.itgroup.bean.Member;
import com.itgroup.dao.MemberDao;

import java.util.Scanner;

public class UpdateMember {
    public static void main(String[] args) {
        MemberDao dao = new MemberDao();
        Member bean = new Member();
        Scanner scan = new Scanner(System.in);
        System.out.print("변경 할 아이디 : ");
        String id = scan.next();
        System.out.print("이름 입력 : ");
        String name = scan.next();
        bean.setId(id);
        bean.setName(name);
        bean.setAddress("강원도 ");
        bean.setSsn("010414-3333333");
        bean.setGender("여자");
        bean.setHiredate("2025/05/05");
        bean.setEmail("aissaigkiowhb@naver.com");
        int cnt = -1;
        cnt = dao.updateMember(bean);

        if (cnt == -1) {
            System.out.println("업데이트 대 실패");
        } else {
            System.out.println("회원 정보 수정 성공");
        }
    }
}
