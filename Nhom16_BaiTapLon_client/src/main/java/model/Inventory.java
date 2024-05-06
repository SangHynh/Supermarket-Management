package model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inventory")
public class Inventory implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private double price;
    private int quantity;
    private String image;

    public Inventory() {
    }

    public Inventory(String name, double price, int quantity, String image) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }
    
    public Inventory(int id, String name, double price, int quantity, String image) {
    	this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.image = image;
    }
    
    public Inventory(int id) {
    	this.id = id;
    }

    // Phương thức để tạo inventoryId
    public String generateInventoryId(int id) {
        // Đây chỉ là một ví dụ đơn giản, bạn có thể thay đổi cách tạo ID tùy thuộc vào yêu cầu của bạn
        return "IN" + String.format("%06d", id);
    }

    // Các phương thức getter và setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

}
