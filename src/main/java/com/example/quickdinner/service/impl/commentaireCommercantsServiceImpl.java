package com.example.quickdinner.service.impl;

import com.example.quickdinner.model.CommentaireCommercants;
import com.example.quickdinner.repository.CommentaireCommercantsRepository;
import com.example.quickdinner.service.CommentaireCommercantsService;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Log
public class commentaireCommercantsServiceImpl implements CommentaireCommercantsService {

    private CommentaireCommercantsRepository commentaireCommercantsRepository;

    public commentaireCommercantsServiceImpl(CommentaireCommercantsRepository commentaireCommercantsRepository) {
        this.commentaireCommercantsRepository = commentaireCommercantsRepository;
    }

    @Override
    public Float findNote(int idCommercant) {
        List<CommentaireCommercants> lesNotes =  commentaireCommercantsRepository.findByCommercantId(idCommercant);

        float total = lesNotes.stream().mapToInt(CommentaireCommercants::getNote).sum();

        if(lesNotes.size() <= 0) {
            total = -1;
        } else {
            total = total / lesNotes.size();
        }


        return total;
    }
}

