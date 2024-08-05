package com.itgroup.dao;

import com.itgroup.bean.Member;
import com.itgroup.utility.Paging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MemberDao extends SuperDao {
    public MemberDao() {
        super();
    }

    public List<Member> selectAll() {
        List<Member> alldata = new ArrayList<Member>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "select * from members order by name desc";
        ResultSet rs = null;
        try {
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Member bean = makebean(rs);
                alldata.add(bean);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return alldata;
    }

    private Member makebean(ResultSet rs) {
        Member bean = new Member();
        try {
            bean.setId(rs.getString("id"));
            bean.setName(rs.getString("name"));
            bean.setSsn(rs.getString("ssn"));
            bean.setAddress(rs.getString("address"));
            bean.setGender(rs.getString("gender"));
            bean.setEmail(rs.getString("email"));
            bean.setHiredate(String.valueOf(rs.getString("hiredate")));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return bean;
    }

    public Member selectByPK(String id) {
        Member bean = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from members where id = ?";

        try {
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                bean = makebean(rs);
            } else {
                System.out.println("그런 아이디는 없단다");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return bean;
    }

    public List<Member> selectByGender(String gender) {
        List<Member> selectData = new ArrayList<Member>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        String sql = "select * from members";
        boolean bool = gender == null || gender.equals("all");
        if (!bool) {
            sql += " where gender = ?";
        }
        try {
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);
            if (!bool) {
                pstmt.setString(1, gender);
            }
            rs = pstmt.executeQuery();
            while (rs.next()) {
                selectData.add(makebean(rs));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return selectData;
    }

    public int selectGender(String gender) {
        int cnt = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "select count(*) as mycnt from members";
        boolean bool = gender == null || gender.equals("all");
        if (!bool) {
            sql += " where gender = ? ";
        }
        ResultSet rs = null;
        try {
            conn = super.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            if (!bool) {
                pstmt.setString(1, gender);
            }
            rs = pstmt.executeQuery();
            if (rs.next()) {
                cnt = rs.getInt("mycnt");
            }
            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return cnt;
    }

    public int insertData(Member bean) {
        int cnt = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " insert into MEMBERS(id, name, ssn, address, gender, email, hiredate) values (?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bean.getId());
            pstmt.setString(2, bean.getName());
            pstmt.setString(3, bean.getSsn());
            pstmt.setString(4, bean.getAddress());
            pstmt.setString(5, bean.getGender());
            pstmt.setString(6, bean.getEmail());
            pstmt.setString(7, bean.getHiredate());
            cnt = pstmt.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return cnt;
    }

    public int deleteMember(String id) {
        int cnt = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " delete from members where id = ?";
        try {
            conn = super.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            cnt = pstmt.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return cnt;
    }

    public int updateMember(Member bean) {
        int cnt = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = " update members set name = ?, ssn = ?, address = ?, gender = ?, email = ?, hiredate = ?";
        sql += " where id = ?";
        try {
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, bean.getName());
            pstmt.setString(2, bean.getSsn());
            pstmt.setString(3, bean.getAddress());
            pstmt.setString(4, bean.getGender());
            pstmt.setString(5, bean.getEmail());
            pstmt.setString(6, bean.getHiredate());
            pstmt.setString(7, bean.getId());
            cnt = pstmt.executeUpdate();

            conn.commit();
        } catch (Exception e) {
            e.printStackTrace();
            try {
                conn.rollback();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        } finally {
            try {
                if (pstmt != null) {
                    pstmt.close();
                }
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return cnt;
    }

    public List<Member> getPaginationData(Paging pageInfo) {
        Connection conn = null;
        String sql = " select id, name, ssn, address, gender, email, hiredate";
        sql += " from (";
        sql += "    select id, name, ssn, address, gender, email, hiredate,";
        sql += "    rank() over(order by id desc) as ranking";
        sql += "    from members";
        String mode = pageInfo.getMode();
        boolean bool = mode.equals(null) || mode.equals("null") || mode.equals("all") || mode.equals("");
        if (!bool) {
            sql += " where gender = ? ";
        }
        sql += ") ";
        sql += " where ranking between ? and ? ";
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        List<Member> allData = new ArrayList<>();
        try {
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);
            if (!bool) {
                pstmt.setString(1, mode);
                pstmt.setInt(2, pageInfo.getBeginRow());
                pstmt.setInt(3, pageInfo.getEndRow());
            } else {
                pstmt.setInt(1, pageInfo.getBeginRow());
                pstmt.setInt(2, pageInfo.getEndRow());
            }
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Member bean = this.makebean(rs);
                allData.add(bean);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {rs.close();}
                if (pstmt != null) {pstmt.close();}
                if (conn != null) {conn.close();}
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return allData;
    }
}
