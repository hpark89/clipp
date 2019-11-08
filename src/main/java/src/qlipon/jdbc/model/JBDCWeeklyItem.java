package src.qlipon.jdbc.model;

import src.qlipon.jdbc.JDBCManager;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;

public class JBDCWeeklyItem {
/**
 * create table `weekly_items` (
 * `uuid` binary(16) not null,
 * `name` varchar(255) not null,
 * `brand_name` varchar(255),
 * `sale_price` int(11),
 * `original_price` int(11),
 * `sale_non_digit` varchar(255),
 * `start_date` long,
 * `end_date` long,
 * `store_name` varchar(255),
 * `category` varchar(255),
 * primary key (`uuid`) );
 */

/**
 * -- insert into weekly_items ( uuid, name, brand_name, sale_price ) values ( '123dsfsd13', 'Cheetos', 'WholeFoods', 499 );
 */

/**
 * -- select BIN_TO_UUID(uuid) from weekly_items;  ( This will help you search/convert BIN(16) to UUID
 */

  private final String tableName = "weekly_items";

  public static void main( String[] args ) throws Exception {
    create( "'henryItemName'", "'BrandName'", "'StoreName'", "'Category'", "199", "499", "'199'", "1500000000", "1550000000" );
  }

  public static void create( String itemName,
                      String brandName,
                      String storeName,
                      String category,
                      String salePrice,
                      String originalPrice,
                      String saleNonDigit,
                      String startDate,
                      String endDate ) throws SQLException {

    try {
      Connection connection = JDBCManager.getDataSource().getConnection();
      Statement st = connection.createStatement();

      String SQL = MessageFormat.format( "INSERT INTO weekly_items ( name, brand_name, store_name, category, sale_price, original_price, sale_non_digit, start_date, end_date, uuid ) " +
              "VALUES ( {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9} )",
          itemName, brandName, storeName, category, salePrice, originalPrice, saleNonDigit, startDate, endDate, "UUID_TO_BIN(UUID())" );
      //"SELECT * FROM weekly_items";
      int rs = st.executeUpdate( SQL );
//      while ( rs.next() ) {
//        System.out.println( rs.getString( "name" ) );
////        int empId = rs.getInt( "employee_id" );
//        String eName = rs.getString( "employee_name" );
//        String email = rs.getString( "email" );
//        Double salary = rs.getDouble( "salary" );
//        BigDecimal bonus = rs.getBigDecimal( "bonus" );
//
//        System.out.println(empId + "\t" + eName + "\t" + salary + "\t" + email + "\t" + bonus);
//      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }


}
