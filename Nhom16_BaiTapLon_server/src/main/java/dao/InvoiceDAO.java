package dao;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import model.Invoice;

public class InvoiceDAO {
    private EntityManager entityManager;

    public InvoiceDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Invoice> getAllInvoices() {
        String jpql = "SELECT i FROM Invoice i";
        TypedQuery<Invoice> query = entityManager.createQuery(jpql, Invoice.class);
        return query.getResultList();
    }

    public boolean addInvoice(Invoice invoice) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(invoice);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            ex.printStackTrace();
            return false;
        }
    }

    public boolean deleteInvoiceById(int id) {
        try {
            entityManager.getTransaction().begin();
            Invoice invoice = entityManager.find(Invoice.class, id);
            if (invoice != null) {
                entityManager.remove(invoice);
                entityManager.getTransaction().commit();
                return true;
            } else {
                entityManager.getTransaction().rollback();
                return false;
            }
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            ex.printStackTrace();
            return false;
        }
    }

    public boolean updateInvoice(Invoice updatedInvoice) {
        try {
            entityManager.getTransaction().begin();
            Invoice existingInvoice = entityManager.find(Invoice.class, updatedInvoice.getId());
            if (existingInvoice != null) {
                // Update thông tin của existingInvoice bằng thông tin từ updatedInvoice
                existingInvoice.setEmployee(updatedInvoice.getEmployee());
                existingInvoice.setDate(updatedInvoice.getDate());

                entityManager.getTransaction().commit();
                return true;
            } else {
                entityManager.getTransaction().rollback();
                return false;
            }
        } catch (Exception ex) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            ex.printStackTrace();
            return false;
        }
    }
}
