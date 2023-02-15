package th.co.prior.training.spring.component.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JSR310Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ProducerComponent {
    private ObjectMapper mapper;

    private KafkaTemplate<String, String> kafkaTemplate;

    public ProducerComponent(@Qualifier("myappKafkaTemplate") KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
    }

    public void pushMessage(Object o, String topic) throws JsonProcessingException {
        String data = this.mapper.writeValueAsString(o);
        this.kafkaTemplate.send(topic, data)
                .completable()
                .whenComplete((r, throwable) -> {
                    if(null == throwable){
                        log.info("kafka send to {} done {}"
                                , r.getRecordMetadata().topic()
                                , r.getProducerRecord().value());
                    } else {
                        log.info("kafka send exception {}", throwable.getMessage());
                    }
        });
    }
}
