package bd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexao {
	private static final String URL = 
			"jdbc:mysql://localhost:3306/helpdesk?useSSL=false&serverTimezone=UTC";
	private static final String USUARIO = "root";
	private static final String SENHA = "admin123";
	
	public static Connection obterConexao() throws SQLException, ClassNotFoundException{
		Class.forName("com.mysql.cj.jdbc.Driver");
		Connection conexao = DriverManager.getConnection(URL, USUARIO, SENHA);
		
		return conexao; 
		
	}

}
