package com.team3.services;

import com.team3.dtos.user.EmailDTO;

public interface EmailService {

    String sendEmail(EmailDTO emailDTO, String templateName);
}
