package com.matthew.launch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class InterestDao {

	public static Interest getById(Long id) {
		Connection connection = getConnection();
		Interest interest = null;
		
		try {
			
			Statement statement = connection.createStatement();
			
			if(id != null) {
				ResultSet result = statement.executeQuery("SELECT * FROM public.interest WHERE public.interest.id = " + id);
				result.next();
				return new Interest(result.getLong(1), result.getString(2));
			} else {
				
			}
			ResultSet result = statement.executeQuery("SELECT * FROM public.interest WHERE public.interest.id = " + id);
			result.next();
			
			interest = new Interest(result.getLong(1), result.getString(2));
			
			connection.close();
			
		} catch (Exception e) {
			System.out.println("Query failed!");
			e.printStackTrace();
		}
		
		return interest;
	}
	
	
	private static Connection getConnection() {
		try {
			
			Class.forName("org.postgresql.Driver");
			
			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/spacejockey", "postgres", "bondstone");
			
			return connection;
			
			
		} catch (Exception e) {
			System.out.println("Connection failed!");
			e.printStackTrace();
		}
		return null;
	}
	
	
	public static void save(Interest interest) {
		
		Connection connection = getConnection();
		
		try {
			
			if(interest.getId() == null) {

				connection.createStatement().executeUpdate("INSERT INTO public.interest(title) VALUES('"
						+ interest.getTitle() + "')");
				
				ResultSet justIn = connection.createStatement().executeQuery("SELECT MAX(id) from public.interest");
				Long addedId = new Long(0);
				if(justIn.next()) {
					addedId = justIn.getLong(1);
				} else {
					System.out.println("Why hello there...");
				}
				interest.setId(addedId);

			}
			else if(interest.getId() != null) {
				Statement statement = connection.createStatement();
				
				ResultSet result = statement.executeQuery("SELECT * FROM public.interest WHERE public.interest.id = " + interest.getId());
				
				if(result.next()) {
					connection.createStatement().executeUpdate("UPDATE public.interest SET title = '" + interest.getTitle() + "'" + " WHERE id = " + interest.getId());
				}else {
					throw new RuntimeException();
				}
			} 
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
