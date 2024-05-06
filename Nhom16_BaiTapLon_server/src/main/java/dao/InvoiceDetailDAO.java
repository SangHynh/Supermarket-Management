package dao;


import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import model.InvoiceDetail;

public class InvoiceDetailDAO {

    private final EntityManager entityManager;

    public InvoiceDetailDAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<Object[]> getInvoiceDetailsWithProductInfoByInvoiceId(Long invoiceId) {
        String jpql = "SELECT ivd.invoice.id, ivd.inventory.id, ivd.inventory.name, ivd.inventory.price, " +
                      "ivd.quantity, ivd.total " +
                      "FROM InvoiceDetail ivd " +
                      "WHERE ivd.invoice.id = :invoiceId";
        TypedQuery<Object[]> query = entityManager.createQuery(jpql, Object[].class);
        query.setParameter("invoiceId", invoiceId);
        return query.getResultList();
    }

    //thêm hàng loạt các chi tiết hóa đơn
    public boolean addInvoiceDetails(List<InvoiceDetail> invoiceDetails) {
        EntityTransaction transaction = null;
        try {
            transaction = entityManager.getTransaction();
            transaction.begin();

            for (InvoiceDetail invoiceDetail : invoiceDetails) {
                entityManager.persist(invoiceDetail);
            }

            transaction.commit();
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        }
    }
    
}
