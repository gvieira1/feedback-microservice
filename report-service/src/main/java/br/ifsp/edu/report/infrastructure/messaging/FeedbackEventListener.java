package br.ifsp.edu.report.infrastructure.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import br.ifsp.edu.report.config.RabbitConfig;
import br.ifsp.edu.report.domain.FeedbackSnapshot;
import br.ifsp.edu.report.infrastructure.repository.FeedbackSnapshotRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class FeedbackEventListener {

    private final FeedbackSnapshotRepository snapshotRepository;

    @RabbitListener(queues = RabbitConfig.QUEUE_REPORT_CREATED)
    public void handleFeedbackEvent(FeedbackEvent event) {  
        try {
            FeedbackSnapshot snapshot = FeedbackSnapshot
            	.builder()
                .sector(event.getSector())
                .type(event.getType())
                .anonymous(event.isAnonymous())
                .createdAt(event.getCreatedAt())
                .build();

            snapshotRepository.save(snapshot);
        } catch (ObjectOptimisticLockingFailureException e) {
            System.out.println("teste" + e.getMessage());
        }
    }
    
    @RabbitListener(queues = RabbitConfig.QUEUE_REPORT_DELETED)
    public void handleFeedbackDeleted(FeedbackDeletedEvent event) {
        snapshotRepository.deleteById(event.getId());
    }

    
}
