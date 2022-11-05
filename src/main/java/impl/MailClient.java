package impl;

import impl.json.ConfigJson;
import jakarta.mail.Authenticator;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;

import java.util.Properties;

public class MailClient {
    private final ConfigJson config;
    private final Properties mailConfig;

    public boolean running = false;

    private Session session;

    public MailClient(ConfigJson config) {
        this.config = config;


        Properties mailConfig = new Properties();
        mailConfig.setProperty("mail.smtp.host", config.getMailHost());
        mailConfig.setProperty("mail.smtp.port", String.valueOf(config.getMailPort()));
        mailConfig.setProperty("mail.smtp.starttls.enable", String.valueOf(config.isMailTls()));
        mailConfig.setProperty("mail.smtp.auth", String.valueOf(config.isMailAuth()));




        this.mailConfig = mailConfig;
    }

    public void connect() {
        this.session = Session.getInstance(mailConfig, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(config.getMailUsername(), config.getMailPassword());
            }
        });
        this.running = true;
    }

}
