package com.adnan.catalog.repositories;

import com.adnan.catalog.entities.Category;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Optional<Category> findCategoryByName(String categoryName);
    
}
