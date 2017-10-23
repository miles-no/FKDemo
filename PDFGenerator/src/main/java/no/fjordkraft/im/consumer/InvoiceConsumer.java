package no.fjordkraft.im.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fjordkraft.common.kafka.Consumer;
import no.fjordkraft.events.PriceSourceChanged;
import no.fjordkraft.events.Topic;
import no.fjordkraft.im.model.StatementsList;

import no.fjordkraft.im.services.PDFGenerator;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.KafkaMessageListenerContainer;
import org.springframework.kafka.listener.MessageListener;
import org.springframework.kafka.listener.config.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.util.HashMap;


/**
 * Created by bhavi on 10/10/2017.
 */
//@Component
public class InvoiceConsumer implements MessageListener<Long, StatementsList> {

    private static final Logger logger = LoggerFactory.getLogger(InvoiceConsumer.class);

    protected KafkaMessageListenerContainer<Long, StatementsList> messageListenerContainer;

    @Autowired
    PDFGenerator pdfGenerator;

    public InvoiceConsumer(String kafkaBootstrapServers, String topic, String group, Deserializer<Long> keyDeserializer, Deserializer<StatementsList> valueDeserializer) {
        HashMap config = new HashMap();
        config.put("bootstrap.servers", kafkaBootstrapServers);
        config.put("group.id", group);
        ContainerProperties containerProperties = new ContainerProperties(new String[]{topic});
        containerProperties.setMessageListener(this);
        this.messageListenerContainer = new KafkaMessageListenerContainer(new DefaultKafkaConsumerFactory(config, keyDeserializer, valueDeserializer), containerProperties);
        this.messageListenerContainer.start();
    }

    @Autowired
    public InvoiceConsumer(ObjectMapper objectMapper,
                           @Value("${kafka.bootstrap-servers}") String kafkaBootstrapServers) throws Exception {
        this(kafkaBootstrapServers,
                "invoiceGenerationTopic",
                "invoiceprocessorconsumer",
                new LongDeserializer(),
                new JsonDeserializer<>(StatementsList.class,objectMapper));
    }

    @Transactional
    public void onMessage(ConsumerRecord<Long, StatementsList> data) {
        StatementsList statementsList = data.value();
        for(Long statementId : statementsList.getStatementIds()) {
            logger.debug("Queuing for PDF generation "+ statementId);
            pdfGenerator.processStatement(statementId);
        }
    }
}
