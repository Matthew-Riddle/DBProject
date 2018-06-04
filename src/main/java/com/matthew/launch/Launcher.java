package com.matthew.launch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

public class Launcher {

	public static void main(String[] args) throws SQLException {
		
		Person person = PersonDao.getById((long)1);
		
		System.out.println(person);
		
		person.setAge(20);
		person.setLastname("Voldemort");
		PersonDao.save(person);
		
		System.out.println(person);
		
	}
}
