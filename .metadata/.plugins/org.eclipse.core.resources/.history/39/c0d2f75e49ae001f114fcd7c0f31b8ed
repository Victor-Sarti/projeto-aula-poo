package br.senac.sp.projetopoo.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionFactory {
	private static Connection conexao;
	
	public static Connection getConexao() {
		if(conexao == null) {
			try {
				conexao = DriverManager.getConnection
						("jdbc:mysql://localhost:3306/poo","root","guigasoares12");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return conexao;
	}
}
