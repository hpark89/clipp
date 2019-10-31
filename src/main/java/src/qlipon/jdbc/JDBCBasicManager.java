package src.qlipon.jdbc;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCBasicManager {
  private final String IMAGE_TABLE = "image";

  public void create( String imagePath, Long createdAt, String category, String notes, String errors ) {

  }

  public static void main( String[] args  ) throws SQLException {

    try ( Connection connection = JDBCManager.getDataSource().getConnection();
         Statement st = connection.createStatement(); ) {

      String SQL = "SELECT * FROM employee_table";
      ResultSet rs = st.executeQuery( SQL );
      while ( rs.next() ) {
        int empId = rs.getInt( "employee_id" );
        String eName = rs.getString( "employee_name" );
        String email = rs.getString( "email" );
        Double salary = rs.getDouble( "salary" );
        BigDecimal bonus = rs.getBigDecimal( "bonus" );

        System.out.println(empId + "\t" + eName + "\t" + salary + "\t" + email + "\t" + bonus);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
