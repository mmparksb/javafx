package com.itgroup.jdbc;

import com.itgroup.bean.Member;
import com.itgroup.dao.MemberDao;
import com.itgroup.utility.Paging;

import java.util.List;
import java.util.Scanner;

public class SelectPagination {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);

        System.out.print("몇 페이지 볼꺼니?");
        String pageNumber = scan.next();

        System.out.print("페이지 당 몇 건씩 볼꺼니?");
        String pageSize = scan.next();

        System.out.print("여자, 남자, all 중 1개 입력 : ");
        String mode = scan.next();

        MemberDao dao = new MemberDao();
        int totalCount = dao.selectGender(mode);

        String url = "prList.jsp";
        String keyword = "";

        Paging pageInfo = new Paging(pageNumber, pageSize, totalCount, url, mode, keyword);
        pageInfo.displayInformation();

        List<Member> memberList = dao.getPaginationData(pageInfo);

        for (Member bean : memberList) {
            ShowData.printData(bean);
        }

    }
}
