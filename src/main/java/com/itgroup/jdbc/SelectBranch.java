package com.itgroup.jdbc;

import com.itgroup.bean.Member;
import com.itgroup.dao.MemberDao;

import java.util.List;
import java.util.Scanner;

public class SelectBranch {
    public static void main(String[] args) {
        // 모든 회원 또는 특정 성별만 조회하기
        Scanner scan = new Scanner(System.in);
        System.out.print("all, 남자, 여자 중 1개 입력 : ");

        String gender = scan.next();

        MemberDao dao = new MemberDao();
        List<Member> allMember = dao.selectByGender(gender);
        System.out.println("항목 개수 : " + allMember.size());

        for(Member bean:allMember){
            ShowData.printData(bean);
        }
    }
}
// 오늘은 날씨가 엄청 더워요.
