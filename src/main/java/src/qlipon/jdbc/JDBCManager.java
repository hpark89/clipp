package src.qlipon.jdbc;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import javax.sql.DataSource;
import java.beans.PropertyVetoException;
import java.io.FileInputStream;
import java.io.IOException;

import java.util.Properties;

/**
 * Manages the sql thread pool sizes and queries
 */
public class JDBCManager {

  private static final String CRED_PATH =                            "credentials/config.yaml";

  private static final String DB_USERNAME =                          "db.cred.username";
  private static final String DB_PASSWORD =                          "db.cred.password";

  private static final String DB_URL =                               "db.config.url";
  private static final String DB_DRIVER_CLASS =                      "db.config.driver_class";

  private static final String DB_MIN_POOL_SIZE =                     "db.c3p0.min_pool";
  private static final String DB_MAX_POOL_SIZE =                     "db.c3p0.max_pool";
  private static final String DB_INIT_POOL_SIZE =                    "db.c3p0.init_pool";
  private static final String DB_ACQUIRE_INCREMENT =                 "db.c3p0.acquire_increment";
  private static final String DB_MAX_STATEMENTS =                    "db.c3p0.max_statements";

  private static Properties properties = null;
  private static ComboPooledDataSource dataSource;

  static {
    try {
      properties = new Properties();
      properties.load(new FileInputStream( CRED_PATH ) );

      dataSource = new ComboPooledDataSource();
      dataSource.setDriverClass( properties.getProperty( DB_DRIVER_CLASS ) );
      dataSource.setJdbcUrl( properties.getProperty( DB_URL ) );

      dataSource.setUser( properties.getProperty( DB_USERNAME ) );
      dataSource.setPassword( properties.getProperty( DB_PASSWORD ) );

      dataSource.setInitialPoolSize( Integer.parseInt( properties.getProperty( DB_INIT_POOL_SIZE ) ) );
      dataSource.setMinPoolSize( Integer.parseInt( properties.getProperty( DB_MIN_POOL_SIZE ) ) );
      dataSource.setMaxPoolSize( Integer.parseInt( properties.getProperty( DB_MAX_POOL_SIZE ) ) );
      dataSource.setAcquireIncrement( Integer.parseInt( properties.getProperty( DB_ACQUIRE_INCREMENT ) ) );
      dataSource.setMaxStatements( Integer.parseInt( properties.getProperty( DB_MAX_STATEMENTS ) ) );
    }
    catch (IOException | PropertyVetoException e) {
      //LOGGER
      e.printStackTrace();
    }
  }

  public static DataSource getDataSource(){
    return dataSource;
  }
}

