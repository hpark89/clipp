package src.qlipon.jdbc;

public class JDBCStorageManager {

  private final JDBCConnectionManager connMgr;

  public JDBCStorageManager( JDBCConnectionManager connMgr ) {
    this.connMgr = connMgr;
  }

//  public int createImageMapping(Connection conn, String host, int port, String db, String user, String pwd,
//      boolean reuse ) throws SQLException {
//    Preconditions.checkNotNull( conn );
//    PreparedStatement lockSt = null;
//    ResultSet lockRs = null;
//    PreparedStatement st = null;
//    ResultSet rs = null;
//
//    try {
//      // lock the storage table exclusively while we check-then-act.
//      // share mode (read locks) is no good if we need to update.
//      lockSt = conn.prepareStatement(
//          "select * from " + IJDBCConstants.IMAGE_TABLE + " for update" );
//
//      lockRs = lockSt.executeQuery();
//
//      if ( reuse ) {
//        localStorageId =
//            findStorageId( conn, host, port, db, user, pwd, remoteProjectId, remoteStorageId );
//      }
//      if ( localStorageId == IJDBCConstants.NON_EXISTENT_STORAGE_ID ) {
//        st = conn.prepareStatement( "insert into " +
//            IJDBCConstants.IMAGE_TABLE + "(host,port,db,user,pwd,project_id,storage_id) " +
//            "values(?,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS );
//
//        int i = 1;
//        st.setString( i++, host );
//        if ( port > 0 ) {
//          st.setInt( i++, port );
//        }
//        else {
//          st.setNull( i++, Types.INTEGER );
//        }2aq1bli+52
//        st.setString( i++, db );
//        st.setString( i++, user );
//        st.setString( i++, pwd );
//        if ( remoteProjectId > 0 ) {
//          st.setInt( i++, remoteProjectId );
//        }
//        else {
//          st.setNull( i++, Types.INTEGER );
//        }
//        if ( remoteStorageId > 0 ) {
//          st.setInt( i++, remoteStorageId );
//        }
//        else {
//          st.setNull( i++, Types.INTEGER );
//        }
//
//        st.executeUpdate();
//
//        rs = st.getGeneratedKeys();
//        if ( rs.next() ) {
//          localStorageId = rs.getInt( 1 );
//        }
//      }
//    }
//    finally {
//      JDBCUtils.close( null, lockSt, lockRs );
//      JDBCUtils.close( null, st, rs );
//    }
//    return localStorageId;
//
//
//  }

}