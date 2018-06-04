package com.matthew.launch;

import java.util.HashSet;
import java.util.Set;
public class Person {

	private Long id;
	private String firstname;
	private String lastname;
	private Integer age;
	//private Long locationkey;
	private Location location;
	private HashSet<Interest> interest;
	
	Person(){}
	
	Person(Long id, String firstname, String lastname, Integer age, HashSet<Interest> interest, Location location){
		this.id = id;
		this.firstname = firstname;
		this.lastname = lastname;
		this.age = age;
		this.location = location;
		this.interest = interest;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstname() {
		return firstname;
	}

	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

//	public Long getLocationkey() {
//		return locationkey;
//	}
//
//	public void setLocationkey(Long locationkey) {
//		this.locationkey = locationkey;
//	}
	
	public String toString() {
		return id + " " + firstname + " " + lastname + " " + age + " " + interest.toString() + " " + location.toString();
	}

	public Location getLocation() {
		return location;
	}

	public void setLocation(Location location) {
		this.location = location;
	}

	public Set<Interest> getInterest() {
		return interest;
	}

	public void setInterest(HashSet<Interest> interest) {
		this.interest = interest;
	}
	
	
	
}
