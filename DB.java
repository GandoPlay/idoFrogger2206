package frogger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DB {
	//the location of the sqlite dataBase
	private String url = "jdbc:sqlite:/C:\\SQLite\\sqlite-tools-win32-x86-3380500\\Froggerdb.db";
	private Connection connection;
	private PreparedStatement preparedStatement;
	private Statement statement;

	
	public DB() {
		try { 
			connection = DriverManager.getConnection(url);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public ArrayList<DataBaseObject> showDataBase() {
		 ArrayList<DataBaseObject> db = new ArrayList<DataBaseObject>();
		String select = "SELECT * FROM players ORDER BY score DESC LIMIT 10";
		try {
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(select);
			while(resultSet.next()) {
				db.add(new DataBaseObject(resultSet.getString("name"),resultSet.getInt("score")));
		
			}
			return db;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	} 
	public void insertToDataBase(String name,int score) {
		String insert = "INSERT INTO players(name,score) VALUES(?,?)";
		try {
			preparedStatement = connection.prepareStatement(insert);
		
			preparedStatement.setString(1, name);
			preparedStatement.setInt(2, score);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		
	}

}
