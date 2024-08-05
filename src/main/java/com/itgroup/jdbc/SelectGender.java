package com.itgroup.jdbc;

import com.itgroup.dao.MemberDao;

import java.util.Scanner;

public class SelectGender {
    public static void main(String[] args) {
        MemberDao dao = new MemberDao();
        Scanner scan = new Scanner(System.in);
        System.out.print("조회할 성별 입력 : ");
        String gender = scan.next();
        int cnt = -1;
        cnt = dao.selectGender(gender);

        if (cnt == -1) {
            System.out.println("성별 조회 실패");
        } else {
            System.out.println("성별 조회 성공");
            System.out.println("성별 회원 수 : " + cnt);
        }
// 깃허브 시험하기 위해서 적었어요
    }
}
