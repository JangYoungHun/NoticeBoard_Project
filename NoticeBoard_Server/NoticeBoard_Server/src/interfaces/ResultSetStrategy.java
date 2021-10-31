package interfaces;

import java.sql.ResultSet;
import java.sql.SQLException;


public interface ResultSetStrategy<T> {
	
	public T getResult(ResultSet rs) throws SQLException; 
}
