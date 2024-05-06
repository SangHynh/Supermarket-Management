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

    //lấy ra hết chi tiết hóa đơn
    public List<InvoiceDetail> getInvoiceDetailsByInvoiceId(Long invoiceId) {
        String jpql = "SELECT ivd FROM InvoiceDetail ivd WHERE ivd.invoice.id = :invoiceId";
        TypedQuery<InvoiceDetail> query = entityManager.createQuery(jpql, InvoiceDetail.class);
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
