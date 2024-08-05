package com.itgroup.jdbc;

import com.itgroup.bean.Member;
import com.itgroup.dao.MemberDao;

import java.util.Scanner;

public class InsertMember {
    public static void main(String[] args) {
        MemberDao dao = new MemberDao();
        System.out.print("아이디 입력 해 : ");
        Scanner scan = new Scanner(System.in);
        String id = scan.next();
        System.out.print("이름 입력 해 : ");
        String name = scan.next();

        // 나 안철수 먼저 수정함 ㅋㅋ .먼저 push 함. ㅋㅋ
        //근데 왜이래 도대체
        Member bean = new Member();
        bean.setId(id);
        bean.setName(name);
        bean.setAddress("제주도");
        bean.setSsn("010414-3054012");
        bean.setGender("남자");
        bean.setEmail("mweougb@gmail.com");
        bean.setHiredate("2024/07/17");
        int cnt = -1;
        cnt = dao.insertData(bean);

        if (cnt == -1) {
            System.out.println("멤버 추가 실패");
        } else {
            System.out.println("멤버 추가 성공");
        }
    }
}
