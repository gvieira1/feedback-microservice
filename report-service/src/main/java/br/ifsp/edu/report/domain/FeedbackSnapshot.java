package br.ifsp.edu.report.domain;

import java.time.LocalDateTime;

import br.ifsp.edu.report.domain.enums.FeedbackType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackSnapshot {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String sector;
    
    @Enumerated(EnumType.STRING)
    private FeedbackType type; 
    
    private boolean anonymous;
    
    private LocalDateTime createdAt;


}
