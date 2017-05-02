package com.j.tiimi.controller;

import com.j.tiimi.entity.Reference;
import com.j.tiimi.repository.ReferenceRepository;
import com.j.tiimi.service.ReferenceService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletResponse;

import org.h2.util.IOUtils;
import org.springframework.http.MediaType;

@RestController
@RequestMapping("reference")
public class ReferenceController {

    @Autowired
    ReferenceRepository referenceRepository;

    @Autowired
    ReferenceService referenceService;

    @Autowired
    Map<String, Validator> validators;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    public String bibtexText() {
        return referenceService.getBibtexString();
    }

    @RequestMapping(value = "/list")
    @ResponseBody
    public List<Reference> listReferences() {
        return referenceService.listReferences();
    }

    @RequestMapping(value = "/file")
    public void bibtexFile(HttpServletResponse response, @RequestParam(value = "name", required = false) String fileName) throws Exception {
        try {
            File bibtexfile = referenceService.getBibtexFile();
            InputStream inputStream = new FileInputStream(bibtexfile);
            response.setContentType("application/force-download");

            if (fileName == null) fileName = "sigproc";
            response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".bib");

            IOUtils.copy(inputStream, response.getOutputStream());
            response.flushBuffer();
            inputStream.close();
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<?> post(@Valid @RequestBody Reference reference, BindingResult result) {
        String type = reference.getType();
        if (type != null && validators.containsKey(type.toLowerCase())) {
            validators.get(type.toLowerCase()).validate(reference, result);
        } else {
            result.reject(null, type + " isn't a valid reference type.");
        }

        if (result.hasErrors()) {
            // tää on vaan esimerkki.

            List<String> errors = result.getGlobalErrors()
                    .stream()
                    .map(e -> e.getDefaultMessage())
                    .collect(Collectors.toList());
            return ResponseEntity.badRequest().body(errors);
        }
        return ResponseEntity.ok(referenceService.createReference(reference));
    }

/*    @RequestMapping(method = RequestMethod.POST)
    @ResponseBody
    public Reference post(@RequestBody Reference reference) {
        return referenceService.createReference(reference);
    }*/
}
