package ma.m2m.captcha.dao;

import ma.m2m.captcha.bean.Host;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class HostDao {

    ConnectionFactory connectionFactory;

    @Autowired
    public HostDao(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Host getByHostIdentifier(String hostIdentifier) {
        Connection connection = connectionFactory.getConnection();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM HOST WHERE HOST_IDENTIFIER = ?");
            preparedStatement.setString(1, hostIdentifier);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                Host host = new Host();
                host.setHostIdentifier(hostIdentifier);
                host.setId(resultSet.getInt("ID"));
                host.setSecretKey(resultSet.getString("SECRET_KEY"));
                return host;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }


}
