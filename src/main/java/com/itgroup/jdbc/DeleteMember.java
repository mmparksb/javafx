package com.itgroup.jdbc;

import com.itgroup.dao.MemberDao;

import java.util.Scanner;

public class DeleteMember {
    public static void main(String[] args) {
        MemberDao dao = new MemberDao();
        Scanner scan = new Scanner(System.in);
        System.out.print("삭제 할 아이디 입력 : ");
        String id = scan.next();
        int cnt = -1;

        cnt = dao.deleteMember(id);


        if (cnt == -1) {
            System.out.println("삭제 실패");
        } else {
            System.out.println("삭제 성공");
        }

    }
}
