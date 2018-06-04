package com.matthew.launch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class LocationDao {

	public static Location getById(Long id) {
		Connection connection = getConnection();
		Location location = null;
		
		try {
			
			Statement statement = connection.createStatement();
			
			if(id != null) {
				ResultSet result = statement.executeQuery("SELECT * FROM public.location WHERE public.location.id = " + id);
				result.next();
				return new Location(result.getLong(1), result.getString(2), result.getString(3), result.getString(4));
			} else {
				
			}
			ResultSet result = statement.executeQuery("SELECT * FROM public.location WHERE public.location.id = " + id);
			result.next();
			
			location = new Location(result.getLong(1), result.getString(2), result.getString(3), result.getString(4));
			
			connection.close();
			
		} catch (Exception e) {
			System.out.println("Query failed!");
			e.printStackTrace();
		}
		
		return location;
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
	
public static void save(Location location) {
		
		Connection connection = getConnection();
		
		try {
			
			if(location.getId() == null) {

				connection.createStatement().executeUpdate("INSERT INTO public.location(city, state, country) VALUES('"
						+ location.getCity() + "', '" + location.getState() + "', '" + location.getCountry() + "')");
				
				ResultSet justIn = connection.createStatement().executeQuery("SELECT MAX(id) from public.location");
				Long addedId = new Long(0);
				if(justIn.next()) {
					addedId = justIn.getLong(1);
				} else {
					System.out.println("Why hello there...");
				}
				location.setId(addedId);
			}
			else if(location.getId() != null) {
				Statement statement = connection.createStatement();
				
				ResultSet result = statement.executeQuery("SELECT * FROM public.location WHERE public.location.id = " + location.getId());
				
				if(result.next()) {
					connection.createStatement().executeUpdate("UPDATE public.location SET city = '" + location.getCity() + "', state = '" 
							+ location.getState() + "', country = '" + location.getCountry() + "' WHERE id = " + location.getId());
				} else {
					throw new RuntimeException();
					}
			}
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
