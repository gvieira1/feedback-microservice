package br.ifsp.edu.report.infrastructure.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import br.ifsp.edu.report.domain.FeedbackSnapshot;
import java.time.LocalDateTime;
import java.util.List;

public interface FeedbackSnapshotRepository extends JpaRepository<FeedbackSnapshot, Long> {
    
    List<FeedbackSnapshot> findByCreatedAtBetween(LocalDateTime start, LocalDateTime end);
    
}
