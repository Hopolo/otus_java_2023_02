package ru.otus.services;

import org.hibernate.Session;

public interface ClientAuthService {
    boolean authenticate(
        Session session,
        String login,
        String password
    );
}
