package ma.m2m.captcha.dao;

import org.springframework.stereotype.Component;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Component
public class ConnectionFactory {
    private Connection connection;
    private URL url;
    public ConnectionFactory(){
        url = this.getClass().getClassLoader().getResource("MXCaptcha.db");
    }

    public  Connection getConnection(){
        try {
            if ( connection ==null || connection.isClosed()){
                Class.forName("org.sqlite.JDBC");
                connection = DriverManager.getConnection("jdbc:sqlite:" + url);
            }
            return connection;

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
