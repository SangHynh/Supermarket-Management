package model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "employee")
public class Employee implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String phone;

    @Column(nullable = false)
    private boolean role;

    private String image;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private boolean gender;

    @Column(nullable = false)
    private String password;

    public Employee() {
    }
    
    public Employee(String name, String phone, boolean role, String image, String email, boolean gender, String password) {
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.image = image;
        this.email = email;
        this.gender = gender;
        this.password = password;
    }
    
    public Employee(int id, String name, String phone, boolean role, String image, String email, boolean gender, String password) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.role = role;
        this.image = image;
        this.email = email;
        this.gender = gender;
        this.password = password;
    }

	public int getId() {
		return id;
	}
	
    // Phương thức để tạo employeeId
    public String generateEmployeeId(int id) {
        // Đây chỉ là một ví dụ đơn giản, bạn có thể thay đổi cách tạo ID tùy thuộc vào yêu cầu của bạn
        return "NV" + String.format("%06d", id);
    }

	public void setId(int id) {
		this.id = id;
	}

//	public String getEmployeeId() {
//		return employeeId;
//	}
//
//	public void setEmployeeId(String employeeId) {
//		this.employeeId = employeeId;
//	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public boolean isRole() {
		return role;
	}

	public void setRole(boolean role) {
		this.role = role;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isGender() {
		return gender;
	}

	public void setGender(boolean gender) {
		this.gender = gender;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
    
}
