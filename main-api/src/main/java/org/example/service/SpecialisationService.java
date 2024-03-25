package org.example.service;

import java.util.List;

public interface SpecialisationService {
    boolean create (String trainingType);
    List<String> get();
}
