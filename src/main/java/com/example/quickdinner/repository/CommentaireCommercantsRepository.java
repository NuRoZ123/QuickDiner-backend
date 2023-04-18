package com.example.quickdinner.repository;

import com.example.quickdinner.model.CommentaireCommercants;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentaireCommercantsRepository extends JpaRepository<CommentaireCommercants, Integer> {

    List<CommentaireCommercants> findByCommercantId(int idCommercant);
}
