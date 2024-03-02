package com.example.snack_backend.core.admin.service;

import com.example.snack_backend.dto.DataMailDTO;
import jakarta.mail.MessagingException;

public interface MailService {
    void sendHtmlMail(DataMailDTO dataMail, String templateName) throws MessagingException;

    void sendHtmlMailOTP(DataMailDTO dataMail, String templateName) throws MessagingException;
}
