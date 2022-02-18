package com.nepaliCravings.demo.model;

import java.util.Arrays;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
public class Foods {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;

	private String foodName;

	private float price;

	private String description;
	
	@Lob
	private byte[] image;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "restaurant_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Restaurant restaurant;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "catagory_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private Catagory catagory;
	
	@OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "subCatagory_id")
	@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
	private SubCatagory subCatagory;
	
	public Foods() {
	}

	public Foods(String foodName, float price, String description,byte[] image) {
		this.foodName = foodName;
		this.price = price;
		this.description = description;
		this.image =image;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public Restaurant getRestaurant() {
		return restaurant;
	}

	public void setRestaurant(Restaurant restaurant) {
		this.restaurant = restaurant;
	}

	
	public String getFoodName() {
		return foodName;
	}

	public void setFoodName(String foodName) {
		this.foodName = foodName;
	}

	public Catagory getCatagory() {
		return catagory;
	}

	public void setCatagory(Catagory catagory) {
		this.catagory = catagory;
	}

	public SubCatagory getSubCatagory() {
		return subCatagory;
	}

	public void setSubCatagory(SubCatagory subCatagory) {
		this.subCatagory = subCatagory;
	}

	@Override
	public String toString() {
		return "Foods [id=" + id + ", foodName=" + foodName + ", price=" + price + ", description=" + description
				+ ", image=" + Arrays.toString(image) + ", restaurant=" + restaurant + ", catagory=" + catagory
				+ ", subCatagory=" + subCatagory + "]";
	}

	

}
