package org.example.service;

public interface IdentityProviderService {
    long provideIdentity(Object type); //provides auto incrementing identity for every type
}
