package com.example.quickdinner.repository;

import com.example.quickdinner.model.CommentaireCommercants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentaireCommercantsRepository extends JpaRepository<CommentaireCommercants, Integer> {

    List<CommentaireCommercants> findByCommercantId(int idCommercant);
}
