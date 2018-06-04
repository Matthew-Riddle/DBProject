package com.matthew.launch;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

public class PersonDao {

	public static Person getById(Long id) {
		Connection connection = getConnection();
		Person person = null;

		try {

			Statement statement = connection.createStatement();

			if (id != null) {

				HashSet<Interest> interests = new HashSet<Interest>();
				ResultSet interestRes = statement
						.executeQuery("SELECT public.personinterest.interestid FROM public.person "
								+ "JOIN public.personinterest " + "ON person.id = public.personinterest.personid "
								+ "WHERE public.personinterest.personid = " + id);

				while (interestRes.next()) {

					Interest interest = InterestDao.getById(interestRes.getLong(1));

					interests.add(interest);
				}

				ResultSet locationRes = statement.executeQuery(
						"SELECT public.person.locationkey FROM public.person " + "WHERE public.person.id = " + id);
				locationRes.next();
				Location location = LocationDao.getById(locationRes.getLong(1));
				ResultSet result = statement.executeQuery("SELECT * FROM public.person WHERE public.person.id = " + id);
				result.next();
				person = new Person(result.getLong(1), result.getString(2), result.getString(3), result.getInt(4),
						interests, location);
				return person;
				// connection.close();
			} else {
				connection.close();
				throw new NullPointerException();
			}

		} catch (Exception e) {
			System.out.println("Query failed!");
			e.printStackTrace();
		} finally {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return person;
	}

	private static Connection getConnection() {
		try {

			Class.forName("org.postgresql.Driver");

			Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/spacejockey",
					"postgres", "bondstone");

			return connection;

		} catch (Exception e) {
			System.out.println("Connection failed!");
			e.printStackTrace();
		}
		return null;
	}

	public static void save(Person person) {

		Connection connection = getConnection();

		updateTables(person);
		try {

			if (person.getId() == null) {

				connection.createStatement()
						.executeUpdate("INSERT INTO public.person(firstname, lastname, age, locationkey) VALUES('"
								+ person.getFirstname() + "', '" + person.getLastname() + "', " + person.getAge() + ", "
								+ person.getLocation().getId() + ")");

				ResultSet justIn = connection.createStatement().executeQuery("SELECT MAX(id) from public.person");
				Long addedId = new Long(0);
				if (justIn.next()) {
					addedId = justIn.getLong(1);
				} else {
					System.out.println("Why hello there...");
				}
				person.setId(addedId);

				for (Interest interest : person.getInterest()) {
					System.out.println("Aw jeez: " + interest.getId());
					connection.createStatement()
							.executeUpdate("INSERT INTO public.personinterest(personid, interestid) " + "VALUES("
									+ person.getId() + ", " + interest.getId() + ")");

				}

			} else if (person.getId() != null) {
				Statement statement = connection.createStatement();

				ResultSet result = statement
						.executeQuery("SELECT * FROM public.person WHERE public.person.id = " + person.getId());

				if (result.next()) {
					connection.createStatement().executeUpdate(
							"UPDATE public.person SET firstname = '" + person.getFirstname() + "', lastname = '"
									+ person.getLastname() + "', age = " + person.getAge() + ", locationkey = "
									+ person.getLocation().getId() + " WHERE id = " + person.getId());
					removeOldInterestAddNew(person);
				}
			} else {
				throw new RuntimeException();
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private static void updateTables(Person person) {
		Connection connection = getConnection();
		LocationDao.save(person.getLocation());
		for (Interest interest : person.getInterest()) {
			InterestDao.save(interest);
		}
		// try {
		//
		// Statement statement = connection.createStatement();
		//
		// /*---LOCATION---*/
		// Location location = person.getLocation();
		//
		// ResultSet locationResult = statement.executeQuery("SELECT public.location.id
		// FROM public.location "
		// + "WHERE public.location.id = " + location.getId());
		//
		//
		// // If the location does not exist -- create it
		// if(locationResult.next() == false) {
		// statement.executeUpdate("INSERT INTO public.location "
		// + "VALUES ('" + location.getCity() + "', '" + location.getState() + "', '" +
		// location.getCountry() + "')");
		// } else {
		// statement.executeUpdate("UPDATE public.location "
		// + "SET city = '" + location.getCity() + "', "
		// + "\"state\" = '" + location.getState() + "', "
		// + "country = '" + location.getCountry() + "' "
		// + "WHERE public.location.id = " + location.getId());
		// }
		//
		// /*---INTERESTS---*/
		// HashSet<Interest> interests = person.getInterest();
		//
		//
		// for (Interest interest : interests) {
		// ResultSet interestResult = statement.executeQuery("SELECT public.interest.id
		// FROM public.interest "
		// + "WHERE public.interest.id = " + interest.getId());
		//
		// // If the interest does not exist -- create it
		// if()
		// }
		//
		//
		//
		//
		//
		//
		//
		//
		//
		//
		// } catch (SQLException e) {
		// // TODO Auto-generated catch block
		// e.printStackTrace();
		// }
	}

	private static void removeOldInterestAddNew(Person person) throws SQLException {
		Connection connection = getConnection();

		Set<Interest> currentInterest = new HashSet<Interest>();
		ResultSet interestRes = connection.createStatement()
				.executeQuery("SELECT * FROM public.personinterest " + "WHERE personid = " + person.getId());

		while (interestRes.next()) {
			currentInterest.add(InterestDao.getById(interestRes.getLong(2)));
		}

		Set<Interest> futureInterest = person.getInterest();
		HashSet<Long> oldIds = new HashSet<Long>();
		HashSet<Long> newIds = new HashSet<Long>();

		for (Interest interest : currentInterest) {
			oldIds.add(interest.getId());
		}

		for (Interest interest : futureInterest) {
			newIds.add(interest.getId());
		}

		for (Long oldId : oldIds) {
			if (!newIds.contains(oldId)) {
				connection.createStatement()
						.executeUpdate("DELETE FROM public.personinterest WHERE public.personinterest.personid = "
								+ person.getId() + " AND public.personinterest.interestid = " + oldId.longValue());
			}
		}
		for (Long newId : newIds) {
			if (!oldIds.contains(newId)) {
				connection.createStatement().executeUpdate("INSERT INTO public.personinterest(personid, interestid) "
						+ "VALUES(" + person.getId() + ", " + newId.longValue() + ")");
			}
		}
	}

	public static HashSet<Person> findInterestGroup(Interest interest, Location location) {
		HashSet<Person> people = new HashSet<Person>();
		Connection connection = getConnection();

		try {
			Statement statement = connection.createStatement();

			ResultSet result = statement
					.executeQuery("SELECT public.person.id, public.interest.id " + "FROM public.person "
							+ "JOIN public.personinterest " + "ON public.person.id = public.personinterest.personid "
							+ "JOIN public.interest " + "ON public.interest.id = public.personinterest.interestid "
							+ "AND public.interest.title = '" + interest.getTitle() + "' " + "JOIN public.location "
							+ "ON public.location.id = public.person.locationkey " + "AND public.location.city = '"
							+ location.getCity() + "' " + "WHERE interest.id = " + interest.getId()
							+ " AND \"location\".id = " + location.getId());

			while (result.next()) {
				people.add(PersonDao.getById(result.getLong(1)));
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return people;
	}

}
