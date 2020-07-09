package cz.fi.muni.pa165.dao;

import cz.fi.muni.pa165.entity.Category;
import cz.fi.muni.pa165.entity.Product;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

@Repository
public class ProductDaoImpl implements ProductDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public void create(Product p) {
        em.persist(p);
    }

    @Override
    public List<Product> findAll() {
        return em.createQuery("select p from Product p", Product.class).getResultList();
    }

    @Override
    public Product findById(Long id) {
        try {
            return em
                    .createQuery("select p from Product p where id = :id",
                            Product.class).setParameter("id", id)
                    .getSingleResult();
        } catch (NoResultException nrf) {
            return null;
        }
    }

    @Override
    public void remove(Product p) {
        em.remove(em.contains(p) ? p : em.merge(p));
    }

    @Override
    public List<Product> findByName(String name) {
        try {
            return em
                    .createQuery("select p from Product p where name = :name",
                            Product.class).setParameter("name", name)
                    .getResultList();
        } catch (NoResultException nrf) {
            return null;
        }
    }
}
