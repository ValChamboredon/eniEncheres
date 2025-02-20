package fr.eni.eniEncheres.dal;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ConnectionProvider {

	private static DataSource dataSource;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		ConnectionProvider.dataSource = dataSource;
	}
	
	public static Connection getConnection() throws SQLException {
		return dataSource.getConnection();
	}
}