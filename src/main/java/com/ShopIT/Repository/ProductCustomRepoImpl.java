package com.ShopIT.Repository;

import com.ShopIT.Models.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class ProductCustomRepoImpl implements ProductCustomRepo {
    private final EntityManager entityManager;
    @Override
    public List<Product> searchProduct(String keyword) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = builder.createQuery(Product.class);
        Root<Product> root = query.from(Product.class);

// Add the search conditions and weights to the query
        Predicate condition1 = builder.like(root.get("productName"), "%" + keyword + "%");
        Predicate condition2 = builder.like(root.get("Highlights"), "%" + keyword + "%");
        Predicate condition3 = builder.like(root.get("services"), "%" + keyword + "%");
        Predicate condition4 = builder.like(root.get("specification"), "%" + keyword + "%");
        Predicate condition5 = builder.like(root.get("description"), "%" + keyword + "%");
        Predicate conditions = builder.or(condition1, condition2, condition3, condition4, condition5);
        query.where(conditions);

// Add the weights to the query
//        query.multiselect(root, builder.prod(condition1, 10), builder.prod(condition2, 5), builder.prod(condition3, 3), builder.prod(condition4, 2), builder.prod(condition5, 1));

// Execute the query and sort the results by weight in descending order
        List<Product> results = entityManager.createQuery(query).getResultList();
//        results.sort((p1, p2) -> Double.compare(p2.getWeight(), p1.getWeight()));

        return results;
    }
}
