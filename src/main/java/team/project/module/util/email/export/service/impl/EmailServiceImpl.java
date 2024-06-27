package team.project.module.util.email.export.service.impl;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import team.project.module.util.email.export.model.query.SendEmailQO;
import team.project.module.util.email.export.service.EmailServiceI;

@Service
@Slf4j
public class EmailServiceImpl implements EmailServiceI {

    @Value("${spring.mail.username}")
    String username;

    @Autowired
    JavaMailSender mailSender;

    @Override
    public boolean sendEmail(SendEmailQO sendEmailQO) {
        assert sendEmailQO != null;

        if (sendEmailQO.isHtml()) {
            return sendHtmlEmail(sendEmailQO);
        } else {
            return sendSimpleEmail(sendEmailQO);
        }
    }

    /* --------- */

    private boolean sendSimpleEmail(SendEmailQO sendEmailQO) {
        try {
            SimpleMailMessage mail = new SimpleMailMessage();
            mail.setFrom(username);
            mail.setTo(sendEmailQO.getSendTo());
            mail.setSubject(sendEmailQO.getSubject());
            mail.setText(sendEmailQO.getContent());

            mailSender.send(mail);
            return true;
        }
        catch (Exception e) {
            log.error("邮件发送失败", e);
            return false;
        }
    }

    private boolean sendHtmlEmail(SendEmailQO sendEmailQO) {
        try {
            MimeMessage mail = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mail, true);
            helper.setFrom(username);
            helper.setTo(sendEmailQO.getSendTo());
            helper.setSubject(sendEmailQO.getSubject());
            helper.setText(sendEmailQO.getContent(), true);

            mailSender.send(mail);
            return true;
        }
        catch (Exception e) {
            log.error("邮件发送失败", e);
            return false;
        }
    }
}
