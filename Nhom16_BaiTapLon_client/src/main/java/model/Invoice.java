package model;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "invoice")

public class Invoice implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @Temporal(TemporalType.DATE)
    private Date date;
    
    @Column(name ="customer_name")
    private String customerName;
    
    private double total;

	public Invoice(Long id, Employee employee, Date date, String customerName, double total) {
		this.id = id;
		this.employee = employee;
		this.date = date;
		this.customerName = customerName;
		this.setTotal(total);
	}
	
	public Invoice(Employee employee, Date date, String customerName, double total) {
		this.employee = employee;
		this.date = date;
		this.customerName = customerName;
		this.setTotal(total);
	}

	public Invoice() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Employee getEmployee() {
		return employee;
	}

	public void setEmployee(Employee employee) {
		this.employee = employee;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}
    
    

}

