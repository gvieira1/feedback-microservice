package br.ifsp.edu.report.infrastructure.messaging;

import java.time.LocalDateTime;

import br.ifsp.edu.report.domain.enums.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackEvent {
    private Long id;
    private String sector;
    private FeedbackType type;
    private boolean anonymous;
    private LocalDateTime createdAt;
    
}
