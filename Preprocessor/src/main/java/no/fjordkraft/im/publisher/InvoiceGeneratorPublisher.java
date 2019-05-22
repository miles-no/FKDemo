package no.fjordkraft.im.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import no.fjordkraft.events.ConsumptionRatingComplete;
import no.fjordkraft.events.Topic;
import no.fjordkraft.im.model.StatementsList;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG;

/**
 * Created by bhavi on 10/10/2017.
 */
//@Component
public class InvoiceGeneratorPublisher {
    private KafkaTemplate<Long,StatementsList> kafkaTemplate;

    @Autowired
    public InvoiceGeneratorPublisher(@Value("${kafka.bootstrap-servers}") String kafkaBootstrapServers,
                                 ObjectMapper objectMapper) {
        if(null != kafkaBootstrapServers && !kafkaBootstrapServers.isEmpty() && kafkaBootstrapServers.length() > 5) {
           // Map<String, Object> config = Collections.singletonMap(BOOTSTRAP_SERVERS_CONFIG, kafkaBootstrapServers);
            //this.kafkaTemplate = new KafkaTemplate<>(new DefaultKafkaProducerFactory<>(config, new LongSerializer(), new JsonSerializer<StatementsList>(objectMapper)));
        }
    }

    @Transactional
    public void publish(StatementsList statementsList) {
        kafkaTemplate.send("invoiceGenerationTopic", statementsList);
    }
}
