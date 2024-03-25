package org.example.service.impl;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

class IdentityProviderServiceImplTest {

    IdentityProviderServiceImpl service = new IdentityProviderServiceImpl();
    @Test
    void provideIdentity() {
        class Class1 {}
        class Class2 {}

        assertEquals(0L, service.provideIdentity(Class1.class));
        assertEquals(1L, service.provideIdentity(Class1.class));
        assertEquals(0L, service.provideIdentity(Class2.class));
        assertEquals(1L, service.provideIdentity(Class2.class));
        assertEquals(2L, service.provideIdentity(Class1.class));
        assertEquals(3L, service.provideIdentity(Class1.class));
    }
}