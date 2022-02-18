package com.nepaliCravings.demo.dto;

import javax.persistence.Lob;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.nepaliCravings.demo.constraints.FieldMatch;

@FieldMatch.List({
	@FieldMatch(first = "password", second = "confirmPassword", message = "The password fields must match"),
	@FieldMatch(first = "email", second = "confirmEmail", message = "The email fields must match") })
public class RestaurantRegistrationDto {
	
	@NotEmpty
private String rest_name;
	
	@NotEmpty
	private String userName;
	
	@NotNull
	private long phone;
	
	@NotEmpty
	private String address;
	
	
	
	@Email
	@NotEmpty
	private String email;
	
	@NotEmpty
	private String password;
	
	@NotEmpty
	@Lob
	private byte[] image;
	
	@NotEmpty
	private String confirmPassword;
	
	@Email
	@NotEmpty
	private String confirmEmail;

	@AssertTrue
	private Boolean terms;
	
	@AssertTrue
	private Boolean veg;
	
	@AssertTrue
	private Boolean nonveg;
	
	@AssertTrue
	private Boolean drinks;
	
	

	public Boolean getVeg() {
		return veg;
	}

	public void setVeg(Boolean veg) {
		this.veg = veg;
	}

	public Boolean getNonveg() {
		return nonveg;
	}

	public void setNonveg(Boolean nonveg) {
		this.nonveg = nonveg;
	}

	public Boolean getDrinks() {
		return drinks;
	}

	public void setDrinks(Boolean drinks) {
		this.drinks = drinks;
	}

	public String getRest_name() {
		return rest_name;
	}

	public void setRest_name(String rest_name) {
		this.rest_name = rest_name;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public long getPhone() {
		return phone;
	}

	public void setPhone(long phone) {
		this.phone = phone;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}


	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public byte[] getImage() {
		return image;
	}

	public void setImage(byte[] image) {
		this.image = image;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public String getConfirmEmail() {
		return confirmEmail;
	}

	public void setConfirmEmail(String confirmEmail) {
		this.confirmEmail = confirmEmail;
	}

	public Boolean getTerms() {
		return terms;
	}

	public void setTerms(Boolean terms) {
		this.terms = terms;
	}
	
	
	
}
