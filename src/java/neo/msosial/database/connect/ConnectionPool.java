package neo.msosial.database.connect;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.Statement;

public class ConnectionPool implements Serializable {

    private static DataSource ds = null;

    private static void init() {
        String message = "Khong ket noi den db";
        ResultSet rst = null;
        Statement stmt = null;
        Connection conn = null; 
        try {
            Context initContext = new InitialContext();
            Context envContext = (Context) initContext.lookup("java:/comp/env");
            ds = (DataSource) envContext.lookup("DEFAULT");
            if (envContext == null) {
                throw new Exception("Error: No Context");
            }
            if (ds == null) {
                throw new Exception("Error: No DataSource");
            }
            if (ds != null) {
                conn = ds.getConnection();
            }

            if (conn != null) {
                stmt = conn.createStatement();
                rst = stmt.executeQuery("SELECT 'Ket noi thanh cong den database' FROM DUAL");
            }
            if (rst.next()) {
                message = rst.getString(1);
            }
            System.out.println("Thong tin ket noi den database: " + message);

            rst.close();
            rst = null;
            stmt.close();
            stmt = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            //Đóng resultSet va Statement,
            if (rst != null) {
                try {
                    rst.close();
                } catch (SQLException ignored) {
                }
            }

            if (stmt != null) {
                try {
                    stmt.close();
                } catch (SQLException ignored) {
                }
            }
        }
    }

    public static Connection getConnection() {
        if (ds == null) {
            init();
        }
        if (ds != null) {
            try {
                return ds.getConnection();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
