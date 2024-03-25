package org.example.service.impl;

import org.example.service.IdentityProviderService;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class IdentityProviderServiceImpl implements IdentityProviderService {
    private final HashMap<Object, AtomicLong> incrementsMap = new HashMap<>();

    @Override
    public long provideIdentity(Object type) {
        if (!incrementsMap.containsKey(type))
            incrementsMap.put(type, new AtomicLong(0L));
        AtomicLong increment = incrementsMap.get(type);
        return increment.getAndIncrement();
    }
}
