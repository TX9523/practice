package login;

import java.sql.Connection;
import java.sql.DriverManager;

public class DbUtil {
    private final String dbUtil="jdbc:mysql://localhost:3306/bjpowernode";//���ݿ����ӵ�ַ
    private final String dbUserName="root";//�û���
    private final String dbPassword="1234";//����
    private final String jdbcName="com.mysql.jdbc.Driver";

    public Connection getCon() throws Exception{
        Class.forName(jdbcName);
        Connection conn = DriverManager.getConnection(dbUtil,dbUserName,dbPassword);
        return conn;
    }//��ȡ���ݿ�����
}