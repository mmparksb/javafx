package com.itgroup.jdbc;

import com.itgroup.bean.Member;
import com.itgroup.dao.MemberDao;

import java.util.Scanner;

public class SelectOnlyOne {
    public static void main(String[] args) {
        // 회원 아이디를 이용하여 특정 회원 정보 가져 오기
        Scanner scan = new Scanner(System.in);
        System.out.print("아이디 입력 : ");

        String id = scan.next();

        MemberDao dao = new MemberDao();
        Member bean = dao.selectByPK(id);

        if(bean == null){
            System.out.println("회원 아이디 " + id + "는(은) 존재하지 않습니다." );
        }else{
            ShowData.printData(bean);
        }

    }
}
