package interfaces;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public interface StatementStratagy {
	
	public PreparedStatement createStatement(Connection con) throws SQLException;
}
