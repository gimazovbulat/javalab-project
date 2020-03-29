package ru.itis.services.interfaces;

import ru.itis.models.User;

public interface MailService {
    void sendEmailConfirmationLink(User user);
    void sendText(String email, String text);
}
