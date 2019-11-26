package src.qlipon.jdbc.model;

import org.checkerframework.checker.nullness.compatqual.NonNullDecl;
import src.qlipon.jdbc.JDBCManager;
import src.qlipon.scrape.model.WeeklyItem;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.sql.*;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * create table `weekly_items` (
 * `uuid` binary(16) not null,
 * `product_name` varchar(255) not null,
 * `brand_name` varchar(255),
 * `store_name` varchar(255),
 * `category` varchar(255),
 * `sale_price` int(11),
 * `original_price` int(11),
 * `sale_non_digit` varchar(255),
 * `start_date` long,
 * `end_date` long,
 * 'description' varchar(255),
 * primary key (`uuid`) );
 */

/**
 * -- insert into weekly_items ( uuid, name, brand_name, sale_price ) values ( '123dsfsd13', 'Cheetos', 'WholeFoods', 499 );
 */

/**
 * -- select BIN_TO_UUID(uuid) from weekly_items;  ( This will help you search/convert BIN(16) to UUID
 */

public class JDBCWeeklyItem {

  private final String tableName = "weekly_items";

  private Statement statement;

  public JDBCWeeklyItem() {
    try {
      Connection connection = JDBCManager.getDataSource().getConnection();
      statement = connection.createStatement();
    }
    catch ( Exception ex ) {
      ex.printStackTrace();
    }
  }


  //TODO: obviously do not allow a full statement to be made to be queried.
  // Get the UUID for what is being searched so I can query by the UUID/Compare/Relate
  public void search( String fullStatement ) {
    try {
      String query = fullStatement;
      ResultSet rs = statement.executeQuery( query );

      ResultSetMetaData resultSetMetaData = rs.getMetaData();
      final int columnCount = resultSetMetaData.getColumnCount();

      List<Object[]> ans = new ArrayList<>();

      while ( rs.next() ) {
        Object[] values = new Object[columnCount];
        for ( int i = 1; i <= columnCount; i++ ) {
          values[i - 1] = rs.getObject( i );
        }
        ans.add( values );
      }
      ans.size();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void create( WeeklyItem item ) {
    try {
      String SQL = MessageFormat.format( "INSERT INTO " + tableName + " " +
              "( product_name, brand_name, store_name, category, sale_price, original_price, sale_non_digit, start_date, end_date, uuid, description,  ) " +
              "VALUES ( {0}, {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9}, {10} )",
          item.productName,
                      item.brandName,
                      item.storeName,
                      item.categoryName,
                      item.salePrice,
                      item.regPrice,
                      item.salePriceNonDigit,
                      item.startDate,
                      item.endDate,
                      item.description,
                      "UUID_TO_BIN(UUID())" );

      statement.executeUpdate( SQL );
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

}

//      while ( rs.next() ) {
//        System.out.println( rs.getString( "name" ) );
//        int empId = rs.getInt( "employee_id" );
//        String eName = rs.getString( "employee_name" );
//        String email = rs.getString( "email" );
//        Double salary = rs.getDouble( "salary" );
//        BigDecimal bonus = rs.getBigDecimal( "bonus" );
//
//        System.out.println(empId + "\t" + eName + "\t" + salary + "\t" + email + "\t" + bonus);
//      }
