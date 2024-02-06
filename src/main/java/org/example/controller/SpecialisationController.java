package org.example.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.tags.Tags;
import lombok.Setter;
import org.example.service.SpecialisationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.example.exceptions.IllegalStateException;

import javax.transaction.Transactional;
import java.util.List;

@RestController
@RequestMapping("/specialisations")
public class SpecialisationController {
    @Setter(onMethod_={@Autowired})
    private SpecialisationService specialisationService;

    //disabled as per task specification
    //@PostMapping
    @Operation(summary = "add training type")
    @Transactional
    public ResponseEntity<Object> createSpecialisation(@RequestParam String request){
        if (specialisationService.create(request))
            return new ResponseEntity<>(HttpStatus.OK);
        throw new IllegalStateException("error - training type creation rejected for unspecified reason");
    }

    @GetMapping
    @Operation(summary = "get all mentor specialisations")
    @Tags({@Tag(name = "student"), @Tag(name = "mentor")})
    public ResponseEntity<Object> getAllSpecialisations(){
        List<String> result = specialisationService.get();
        if (result != null)
            return new ResponseEntity<>(result, HttpStatus.OK);
        throw new IllegalStateException("error - service returned null");
    }
}
