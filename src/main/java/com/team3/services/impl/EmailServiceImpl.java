package com.team3.services.impl;

import com.team3.dtos.email.EmailDTO;
import com.team3.entities.Offer;
import com.team3.services.EmailService;
import com.team3.services.OfferService;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.List;
import java.util.Map;

@Service
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final OfferService offerService;

    public EmailServiceImpl(JavaMailSender javaMailSender, TemplateEngine templateEngine, OfferService offerService) {
        this.javaMailSender = javaMailSender;
        this.templateEngine = templateEngine;
        this.offerService = offerService;
    }


    @Override
    public String sendEmail(EmailDTO emailDTO, String templateName) {

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true, "UTF-8");
            messageHelper.setTo(emailDTO.getTo());
            messageHelper.setSubject(emailDTO.getSubject());
            messageHelper.setFrom(emailDTO.getFrom());

            Context context = new Context();
            context.setVariables((Map<String, Object>) emailDTO.getData());

            String htmlContent = templateEngine.process(templateName, context);
            messageHelper.setText(htmlContent, true);

            javaMailSender.send(mimeMessage);
            return "Email sent successfully!";
        } catch (MessagingException e) {
            e.printStackTrace();
            return "Error sending email: " + e.getMessage();
        }
    }

    @Scheduled(cron = "00 00 08 * * ?")
    public void sendReminderEmails() {
        List<Offer> offersToRemind = offerService.getOffersDueToday();

        for (Offer offer : offersToRemind) {
            EmailDTO emailDTO = new EmailDTO();
            emailDTO.setTo(offer.getCandidate().getEmail());
            emailDTO.setSubject("Reminder: Action Required on Offer");
            emailDTO.setFrom("chuyendizz@gmail.com");
            emailDTO.setData(Map.of("offer", offer));

            sendEmail(emailDTO, "offerReminderTemplate");
        }
    }
}
