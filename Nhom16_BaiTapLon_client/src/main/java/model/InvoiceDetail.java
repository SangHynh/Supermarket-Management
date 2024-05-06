package model;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "invoice_detail")

public class InvoiceDetail implements Serializable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne
    @JoinColumn(name = "inventory_id")
    private Inventory inventory;
    
    @Column(name = "total")
    private double total;

    private int quantity;

	public InvoiceDetail(Long id, Invoice invoice, Inventory inventory, int quantity, double total) {
		this.id = id;
		this.invoice = invoice;
		this.inventory = inventory;
		this.quantity = quantity;
		this.total = total;
	}
	
	public InvoiceDetail(Invoice invoice, Inventory inventory, int quantity, double total) {
		this.invoice = invoice;
		this.inventory = inventory;
		this.quantity = quantity;
		this.total = total;
	}

	public InvoiceDetail() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Invoice getInvoice() {
		return invoice;
	}

	public void setInvoice(Invoice invoice) {
		this.invoice = invoice;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public void setInventory(Inventory inventory) {
		this.inventory = inventory;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getTotal() {
		return total;
	}

	public void setTotal(double total) {
		this.total = total;
	}


    
}

