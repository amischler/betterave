package org.amap.lafeedeschamps.service;

import io.github.jhipster.config.JHipsterProperties;
import org.amap.lafeedeschamps.domain.Comment;
import org.amap.lafeedeschamps.domain.Distribution;
import org.amap.lafeedeschamps.domain.DistributionPlace;
import org.amap.lafeedeschamps.domain.User;
import org.amap.lafeedeschamps.service.util.UserUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Service for sending emails.
 * <p>
 * We use the @Async annotation to send emails asynchronously.
 */
@Service
public class MailService {

    private final Logger log = LoggerFactory.getLogger(MailService.class);

    private static final String USER = "user";

    private static final String USER_NAME = "userName";

    private static final String DISTRIBUTION = "distribution";

    private static final String DISTRIBUTION_PLACE = "distributionPlace";

    private static final String COMMENT = "comment";

    private static final String BASE_URL = "baseUrl";

    private static final String TIME_FORMAT = "timeFormat";

    private final JHipsterProperties jHipsterProperties;

    private final JavaMailSender javaMailSender;

    private final MessageSource messageSource;

    private final SpringTemplateEngine templateEngine;

    public MailService(JHipsterProperties jHipsterProperties, JavaMailSender javaMailSender,
                       MessageSource messageSource, SpringTemplateEngine templateEngine) {

        this.jHipsterProperties = jHipsterProperties;
        this.javaMailSender = javaMailSender;
        this.messageSource = messageSource;
        this.templateEngine = templateEngine;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        log.debug("Send email[multipart '{}' and html '{}'] to '{}' with subject '{}' and content={}",
            isMultipart, isHtml, to, subject, content);

        // Prepare message using a Spring helper
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e) {
            if (log.isDebugEnabled()) {
                log.warn("Email could not be sent to user '{}'", to, e);
            } else {
                log.warn("Email could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey) {
        sendEmailFromTemplate(user, templateName, titleKey, createDefaultContext(user));
    }

    @Async
    public void sendEmailFromTemplate(User user, String templateName, String titleKey, Context context) {
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        String content = templateEngine.process(templateName, context);
        String subject = messageSource.getMessage(titleKey, null, locale);
        sendEmail(user.getEmail(), subject, content, false, true);
    }

    protected Context createDefaultContext(User user) {
        Locale locale = Locale.forLanguageTag(user.getLangKey());
        Context context = new Context(locale);
        context.setVariable(USER, user);
        context.setVariable(USER_NAME, UserUtil.formatFirstName(user));
        context.setVariable(BASE_URL, jHipsterProperties.getMail().getBaseUrl());
        return context;
    }

    @Async
    public void sendActivationEmail(User user) {
        log.debug("Sending activation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/activationEmail", "email.activation.title");
    }

    @Async
    public void sendCreationEmail(User user) {
        log.debug("Sending creation email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/creationEmail", "email.activation.title");
    }

    @Async
    public void sendPasswordResetMail(User user) {
        log.debug("Sending password reset email to '{}'", user.getEmail());
        sendEmailFromTemplate(user, "mail/passwordResetEmail", "email.reset.title");
    }

    @Async
    public void sendReminderEmail(User user, Distribution distribution, DistributionPlace distributionPlace) {
        log.debug("Sending reminder email to {}", user.getEmail());
        Context context = createDefaultContext(user);
        context.setVariable(DISTRIBUTION, distribution);
        context.setVariable(DISTRIBUTION_PLACE, distributionPlace);
        context.setVariable(TIME_FORMAT, DateTimeFormatter.ofPattern("HH'h'mm").withZone(ZoneId.of("Europe/Paris")).withLocale(Locale.FRENCH));
        sendEmailFromTemplate(user, "mail/distributionReminderEmail", "email.reminder.title", context);
    }

    /**
     * Publish a new comment by email to users.
     *
     * @param commentUser the user who commented the distribution
     * @param comment the comment added to the distribution
     * @param distribution the commented distribution
     */
    @Async
    public void sendCommentEmail(User commentUser, Comment comment, Distribution distribution) {
        for (User distributionUser : distribution.getUsers()) {
            if (!commentUser.getId().equals(distributionUser.getId())) {
                log.debug("Sending comment email to {}", distributionUser);
                Context context = createDefaultContext(distributionUser);
                context.setVariable(COMMENT, comment);
                context.setVariable(DISTRIBUTION, distribution);
                context.setVariable("commentUserName", UserUtil.formatFirstName(commentUser));
                sendEmailFromTemplate(distributionUser, "mail/commentEmail", "email.comment.title", context);
            }
        }
    }

}
