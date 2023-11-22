package com.github.cxt.springboot2.entities;

import java.beans.Transient;
import java.util.Date;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;
import com.fasterxml.jackson.annotation.JsonView;
import com.github.cxt.springboot2.validator.Phone;
import com.github.cxt.springboot2.validator.Year;


@XmlRootElement
//@JsonIgnoreProperties(value={"email"})
public class Subscriber {
	
	public interface TestView {};  
	
	@JsonView(TestView.class) 
	private String id;

	@Size(min=2, max=30) 
	@JsonView(TestView.class) 
	private String name;
	
	@NotEmpty @Email
	private String email;
	
	@NotNull @Min(13) @Max(110)
	private Integer age;
	
	@Size(min=10) @Phone
	private String phone;
	
	@NotNull
	private Gender gender;
	
	@DateTimeFormat(pattern="MM/dd/yyyy")
	@NotNull @Past @Year(1989)
	private Date birthday;
	
	private Boolean receiveNewsletter;
	
	public enum Gender {
		MALE, FEMALE
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {
		this.gender = gender;
	}

	public Date getBirthday() {
		return birthday;
	}

	public void setBirthday(Date birthday) {
		this.birthday = birthday;
	}

	public Boolean getReceiveNewsletter() {
		return receiveNewsletter;
	}

	public void setReceiveNewsletter(Boolean receiveNewsletter) {
		this.receiveNewsletter = receiveNewsletter;
	}

	public String getId() {
		return id;
	}

	@Transient
	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Subscriber [id=" + id + ", name=" + name + ", email=" + email + ", age=" + age + ", phone=" + phone
				+ ", gender=" + gender + ", birthday=" + birthday + ", receiveNewsletter=" + receiveNewsletter + "]";
	}
	
}
