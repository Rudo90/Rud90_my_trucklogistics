package am.itspace.myTrucklogistics.service.emailService;


import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.FileNotFoundException;

@Service
@RequiredArgsConstructor
public class EmailSenderService {

    private final JavaMailSender sender;

    @Async
    public void sandSimpleMessage(String from, String to, String subject, String text) {

        SimpleMailMessage mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(from);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(text);
        sender.send(mailMessage);
    }

    @Async
    public void sandRegistrationAttachedMessage(String from, String to, String subject, String text, String fileUrl) throws MessagingException, FileNotFoundException {

        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setFrom(from);
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text);

        FileSystemResource file = new FileSystemResource(new File(fileUrl));
        String fileName = file.getFilename();
        if (fileName != null && fileName.length() != 0) {
            helper.addAttachment(fileName, file);
        }

        sender.send(mimeMessage);

    }

}
