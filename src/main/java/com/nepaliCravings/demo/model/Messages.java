package com.nepaliCravings.demo.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Messages {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private float starsweb;
	
	private String messagefor;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinTable(name = "messages_restaurants", 
	joinColumns = @JoinColumn(name = "messages_id", referencedColumnName = "id"), 
	inverseJoinColumns = @JoinColumn(name = "restaurants_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Restaurant restaurant;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinTable(name = "messages_user", 
	joinColumns = @JoinColumn(name = "messages_id", referencedColumnName = "id"), 
	inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private User user;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMessagefor() {
		return messagefor;
	}

	public void setMessagefor(String messagefor) {
		this.messagefor = messagefor;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public float getStarsweb() {
		return starsweb;
	}

	public void setStarsweb(float starsweb) {
		this.starsweb = starsweb;
	}
	

}
