package com.j.tiimi.service;

import com.j.tiimi.entity.Attribute;
import com.j.tiimi.entity.Reference;
import com.j.tiimi.latex.BibtexGenerator;
import com.j.tiimi.repository.AttributeRepository;
import com.j.tiimi.repository.ReferenceRepository;
import java.io.File;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReferenceService {

    @Autowired
    ReferenceRepository referenceRepository;
    @Autowired
    AttributeRepository attributeRepository;
    @Autowired
    BibtexGenerator bibtexgenerator;

    public Reference createReference(Reference reference) {
        List<Attribute> attributes = reference.getAttributes();
        attributeRepository.save(attributes);

        return referenceRepository.save(reference);
    }

    public ResponseEntity<?> deleteReference(Long id) {
        Reference found = referenceRepository.findOne(id);
        if (found == null) {
            return ResponseEntity.badRequest().body("Reference not found");
        }
        referenceRepository.delete(found.getId());
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Redirect", "/list");
        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }

    public List<Reference> listReferences() {

        return referenceRepository.findAll();
    }
    
    public String getBibtexString() {
        return bibtexgenerator.getBibtex(referenceRepository.findAll());
    }
    
    public File getBibtexFile() {
        return bibtexgenerator.getBibtexFile(referenceRepository.findAll());
    }
}
