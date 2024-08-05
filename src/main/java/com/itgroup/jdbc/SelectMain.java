package com.itgroup.jdbc;

import com.itgroup.bean.Member;
import com.itgroup.dao.MemberDao;

import java.util.List;

public class SelectMain {
    public static void main(String[] args) {
        // 모든 회원 목록을 이름으로 내림차순으로 조회합니다.
        MemberDao dao = new MemberDao();
        List<Member> allData = dao.selectAll();
        System.out.println("총 회원수 : " + allData.size());

        for (Member bean : allData) {
//            System.out.println(bean);
            ShowData.printData(bean);
        }
    }
}
