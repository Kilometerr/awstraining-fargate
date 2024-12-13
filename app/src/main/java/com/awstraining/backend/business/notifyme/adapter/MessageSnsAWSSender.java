package com.awstraining.backend.business.notifyme.adapter;

import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.model.PublishRequest;
import com.amazonaws.services.sns.model.PublishResult;
import com.awstraining.backend.business.notifyme.MessageSender;
import org.springframework.beans.factory.annotation.Value;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

@Component
public class MessageSnsAWSSender implements MessageSender {

    private static final Logger LOGGER = LogManager.getLogger(MessageSnsAWSSender.class);

    private final AmazonSNS amazonSNS;
    private final String snsTopicArn;

    // Constructor injection for AmazonSNS and SNS Topic ARN
    public MessageSnsAWSSender(AmazonSNS amazonSNS, @Value("${notification.topicrn}") String snsTopicArn) {
        this.amazonSNS = amazonSNS;
        this.snsTopicArn = snsTopicArn;
    }

    @Override
    public void send(String text) {
        try {
            // Create a PublishRequest
            PublishRequest publishRequest = new PublishRequest()
                    .withTopicArn(snsTopicArn)
                    .withMessage(text);

            // Publish the message
            PublishResult publishResult = amazonSNS.publish(publishRequest);

            // Log successful message publishing
            LOGGER.info("Message sent to topic ARN: {}. Message ID: {}", snsTopicArn, publishResult.getMessageId());
        } catch (Exception e) {
            LOGGER.error("Failed to send message to SNS topic: {}", snsTopicArn, e);
        }
    }
}
