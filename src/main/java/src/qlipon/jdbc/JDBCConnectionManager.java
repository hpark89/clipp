package src.qlipon.jdbc;

public class JDBCConnectionManager {

//  private static final long DEFAULT_MAX_POOL_SIZE =
//      (long)Math.max( Runtime.getRuntime().availableProcessors(), 75 );
//
//  protected final AtomicBoolean closed = new AtomicBoolean( false );
//
//  protected volatile Properties masterProperties ;
//
//  protected volatile JDBCPooledDataSource masterDataSource;
//
//  protected volatile RefCountedPool masterConnPool;
//
//  protected JDBCSchemaManager schemaManager;
//
//  protected JDBCStorageManager storageManger;
//
//  protected JDBCConnectionManager() { }
//
//  protected synchronized JDBCConnectionManager init( Properties props ) throws SQLException, IOException {
//    if ( props == null ) {
//      props = new Properties(  );
//    }
//    props.putIfAbsent( "jdbc.driverClass", "com.mysql.jdbc.Driver" );
//    props.putIfAbsent( "jdbc.url", "jdbc:mysql://localhost/biit_db?autoReconnect=true&useServerPrepStmts=true&rewriteBatchedStatements=true&useUnicode=true&characterEncoding=utf-8&prepStmtCacheSize=250&cachePrepStmts=true&prepStmtCacheSqlLimit=2048&maintainTimeStats=false&cacheServerConfiguration=true&useSSL=false");
//    props.putIfAbsent( "jdbc.db", "biit_db");
//    props.putIfAbsent( "jdbc.user", "biit");
//    props.putIfAbsent( "jdbc.password", "biit");
//    props.putIfAbsent( "jdbc.schema", "biit.sql");
//    this.masterProperties = props;
//    this.masterConnPool = makeRefCountedPool( props, true );
//    this.masterConnPool.refCount++;
//    this.masterDataSource = new JDBCPooledDataSource( masterConnPool.impl, props );
//    //Loggers.INTERNAL.info( "Connection manager master pool: " + masterDataSource.getUrl() );
//    this.storageManger = new JDBCStorageManager( this );
//    this.schemaManager = new JDBCSchemaManager( this );
//    return this;
//  }
//
//  protected void checkNotClosed() {
//    if ( this.closed.get() ) {
//      throw new IllegalStateException( "connection manager was closed" );
//    }
//  }
}