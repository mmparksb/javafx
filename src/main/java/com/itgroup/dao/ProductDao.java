package com.itgroup.dao;

import com.itgroup.bean.Product;
import com.itgroup.utility.Paging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductDao extends SuperDao{
    public ProductDao() {super();}

    public int getTotalCount(int number, String category, String temp) {
        //        if (number == 0 || number == 4) {
        //            totalCount = dao.getTotalCount(mode);
        //        } else if (number==1){
        //            totalCount = dao.getTotalCountByBest();
        //        } else if (number ==2) {
        //            totalCount = dao.getTotalCountByEvent();
        //        } else if (number == 3) {
        //            totalCount = dao.getTotalCountBySearch(temp);
        //        }
        int totalCount = 0;
        String sql = "";
        if (number == 0 || number == 4) {
            sql = "select count(*) as mycnt from products " ;
        } else if (number == 1) {
            sql = " select count(*) as mycnt from products where Best = 'BEST' ";
        } else if (number == 2) {
            sql = " select count(*) as mycnt from products where event in('1+1', '2+1')" ;
        } else if (number == 3) {
            sql = " SELECT count(*) as mycnt FROM products WHERE name LIKE ?" ;
        }

        if (number == 0 || number == 4) {
            if (!(category == null || category.equals("all"))) {

                sql += " where category = ? ";
            }
        }

        Connection conn = null ;
        PreparedStatement pstmt = null ;
        ResultSet rs = null ;

        try{
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);

            if (number == 0 || number == 4) {
                if (!(category == null || category.equals("all"))) {
                    pstmt.setString(1, category);
                }
            } else if (number == 3) {
                pstmt.setString(1, "%" +temp + "%");
            }

            rs = pstmt.executeQuery() ;

            if(rs.next()){
                totalCount = rs.getInt("mycnt") ;
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try{
                if(rs!=null){rs.close();}
                if(pstmt!=null){pstmt.close();}
                if(conn!=null){conn.close();}
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return totalCount ;

    }

    public int insertProduct(Product bean) {
        int cnt = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "insert into products(barcode, category, name, price, stock, manufactureDate, expirationDate, best, event, image) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?) ";
        try {
            conn = super.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);

            pstmt.setInt(1, bean.getBarcode());
            pstmt.setString(2, bean.getCategory());
            pstmt.setString(3, bean.getName());
            pstmt.setInt(4,bean.getPrice());
            pstmt.setInt(5,bean.getStock());
            pstmt.setString(6,bean.getManufactureDate());
            pstmt.setString(7,bean.getExpirationDate());
            pstmt.setString(8, bean.getBest());
            pstmt.setString(9, bean.getEvent());
            pstmt.setString(10, bean.getImage());

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
                if (pstmt != null) {pstmt.close();}
                if (pstmt != null) {pstmt.close();}
            } catch (Exception e) {e.printStackTrace();}
        }

        return cnt;
    }

    public int updateProduct(Product bean) {
        int cnt = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;
        int bool = 0;
        if (bean.getExpirationDate() == null && bean.getManufactureDate() == null) {
            bool = 1; // 둘 다 null 이면
        } else if (bean.getExpirationDate() == null) {
            bool = 2; // 유통기한만 null 이면
        } else if (bean.getManufactureDate() == null) {
            bool = 3; // 제조일자만 null 이면
        }
        String sql = "";
        if (bool == 1) {
            sql = "update products set category = ?, name = ?, price = ?, stock = ?, ManufactureDate = null, ExpirationDate = null, best = ?, event = ?, image = ? where barcode = ?";
        } else if (bool == 2) {
            sql = "update products set category = ?, name = ?, price = ?, stock = ?, ManufactureDate = ?, ExpirationDate = null, best = ?, event = ?, image = ? where barcode = ?";
        } else if (bool == 3) {
            sql = "update products set category = ?, name = ?, price = ?, stock = ?, ManufactureDate = null, ExpirationDate = ?, best = ?, event = ?, image = ? where barcode = ?";
        } else {
            sql = "update products set category = ?, name = ?, price = ?, stock = ?, ManufactureDate = ?, ExpirationDate = ?, best = ?, event = ?, image = ? where barcode = ?";
        }

        try {
            conn = super.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            if (bool == 1) {
                pstmt.setString(1, bean.getCategory());
                pstmt.setString(2, bean.getName());
                pstmt.setInt(3,bean.getPrice());
                pstmt.setInt(4,bean.getStock());
                pstmt.setString(5, bean.getBest());
                pstmt.setString(6, bean.getEvent());
                pstmt.setString(7, bean.getImage());
                pstmt.setInt(8, bean.getBarcode());
            } else if (bool == 2) {
                pstmt.setString(1, bean.getCategory());
                pstmt.setString(2, bean.getName());
                pstmt.setInt(3,bean.getPrice());
                pstmt.setInt(4,bean.getStock());
                pstmt.setString(5,bean.getManufactureDate());
                pstmt.setString(6, bean.getBest());
                pstmt.setString(7, bean.getEvent());
                pstmt.setString(8, bean.getImage());
                pstmt.setInt(9, bean.getBarcode());
            } else if (bool == 3) {
                pstmt.setString(1, bean.getCategory());
                pstmt.setString(2, bean.getName());
                pstmt.setInt(3,bean.getPrice());
                pstmt.setInt(4,bean.getStock());
                pstmt.setString(5,bean.getExpirationDate());
                pstmt.setString(6, bean.getBest());
                pstmt.setString(7, bean.getEvent());
                pstmt.setString(8, bean.getImage());
                pstmt.setInt(9, bean.getBarcode());
            } else {
                pstmt.setString(1, bean.getCategory());
                pstmt.setString(2, bean.getName());
                pstmt.setInt(3,bean.getPrice());
                pstmt.setInt(4,bean.getStock());
                pstmt.setString(5,bean.getManufactureDate());
                pstmt.setString(6,bean.getExpirationDate());
                pstmt.setString(7, bean.getBest());
                pstmt.setString(8, bean.getEvent());
                pstmt.setString(9, bean.getImage());
                pstmt.setInt(10, bean.getBarcode());
            }

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
                if (pstmt != null) {pstmt.close();}
                if (conn != null) {conn.close();}
            } catch (Exception e) {e.printStackTrace();}
        }

        return cnt;
    }

    public int deleteProduct(int barcode) {
        int cnt = -1;
        Connection conn = null;
        PreparedStatement pstmt = null;
        String sql = "delete from products where barcode = ?";
        try {
            conn = super.getConnection();
            conn.setAutoCommit(false);
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, barcode);

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
                if (pstmt != null) {pstmt.close();}
                if (pstmt != null) {pstmt.close();}
            } catch (Exception e) {e.printStackTrace();}
        }

        return cnt;
    }

    public List<Product> getPaginationData(Paging pageInfo, int number, String temp) {
        List<Product> productList = new ArrayList<Product>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        boolean bool = false;
        String mode = null;

        String sql = " select barcode, category, name, price, stock, manufactureDate, expirationDate, best, event, image ";
        sql += " from ( ";
        sql += "   select barcode, category, name, price, stock, manufactureDate, expirationDate, best, event, image,  ";
        sql += "   rank() over(order by barcode desc) as ranking ";
        sql += "   from products ";

        if (number == 0 || number == 4) {
            mode = pageInfo.getMode();
            bool = mode == null || mode.equals(null) || mode.equals("null") || mode.equals("") || mode.equals("all");
            if (!bool) {
                sql += " where category = ?";
            }
        } else if (number == 1) {
            sql += " where event in ('1+1', '2+1') ";
        } else if (number == 2) {
            sql += " where best = 'BEST' ";
        } else if (number == 3) {
            sql += " where name like ?  ";
        }

        sql += " ) ";
        sql += " where ranking between ? and ?  " ;


        try{
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);
            if (number == 0 || number == 4) {
                if (!bool) {
                    pstmt.setString(1, mode);
                    pstmt.setInt(2, pageInfo.getBeginRow());
                    pstmt.setInt(3, pageInfo.getEndRow());

                } else {
                    pstmt.setInt(1, pageInfo.getBeginRow());
                    pstmt.setInt(2, pageInfo.getEndRow());
                }
            } else if (number == 1 || number == 2) {
                pstmt.setInt(1, pageInfo.getBeginRow());
                pstmt.setInt(2, pageInfo.getEndRow());
            } else if (number == 3) {
                pstmt.setString(1,"%" +temp+"%");
                pstmt.setInt(2, pageInfo.getBeginRow());
                pstmt.setInt(3, pageInfo.getEndRow());
            }

            rs = pstmt.executeQuery() ;

            while(rs.next()){
                Product bean = this.makeBean(rs);
                productList.add(bean);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try{
                if(rs!=null){rs.close();}
                if(pstmt!=null){pstmt.close();}
                if(conn!=null){conn.close();}
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }
        return productList;
    }

    private Product makeBean(ResultSet rs){
        Product bean = new Product();
        try {
            bean.setBarcode(rs.getInt("barcode"));
            bean.setCategory(rs.getString("category"));
            bean.setName(rs.getString("name"));
            bean.setPrice(rs.getInt("price"));
            bean.setStock(rs.getInt("stock"));
            bean.setManufactureDate(rs.getString("manufactureDate"));
            bean.setExpirationDate(rs.getString("ExpirationDate"));
            bean.setBest(rs.getString("best"));
            bean.setEvent(rs.getString("event"));
            bean.setImage(rs.getString("image"));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return bean;
    }

    public List<Product> selectByLackStock() {
        List<Product> productList = new ArrayList<Product>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "select * from ( select barcode, category, name, price, stock, manufactureDate, expirationDate, best, event, image, rank() over(order by stock asc) as ranking from products where stock<5)";
        try{
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery() ;

            while(rs.next()){
                Product bean = this.makeBean(rs);
                productList.add(bean);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try{
                if(rs!=null){rs.close();}
                if(pstmt!=null){pstmt.close();}
                if(conn!=null){conn.close();}
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        return productList;
    }

    public List<Product> selectByLackExpirationDate() {
        List<Product> productList = new ArrayList<Product>();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "select * from ( select barcode, category, name, price, stock, manufactureDate, expirationDate, best, event, image, rank() over(order by expirationDate asc) as ranking from products where expirationDate < sysdate + Interval '2' day)";
        try{
            conn = super.getConnection();
            pstmt = conn.prepareStatement(sql);

            rs = pstmt.executeQuery() ;

            while(rs.next()){
                Product bean = this.makeBean(rs);
                productList.add(bean);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }finally{
            try{
                if(rs!=null){rs.close();}
                if(pstmt!=null){pstmt.close();}
                if(conn!=null){conn.close();}
            }catch(Exception ex){
                ex.printStackTrace();
            }
        }

        return productList;
    }
}
