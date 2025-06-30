package br.ifsp.edu.report.application;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import br.ifsp.edu.report.domain.FeedbackSnapshot;
import br.ifsp.edu.report.domain.enums.FeedbackType;
import br.ifsp.edu.report.dto.DashboardStatsDTO;
import br.ifsp.edu.report.dto.FeedbackSectorStatsDTO;
import br.ifsp.edu.report.dto.FeedbackStatsDTO;
import br.ifsp.edu.report.infrastructure.repository.FeedbackSnapshotRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class ReportService {
	
	private final FeedbackSnapshotRepository feedbackRepository;
	
	//ESTATISTICAS DOS ULTIMOS 3 MESES COM TOTAL DE FEEDBACKS, MAIOR QUANTIDADE POR SETOR E TIPO
	 public FeedbackStatsDTO getLastThreeMonthsStats() {
	        List<FeedbackSnapshot> recentFeedbacks = getFeedbacksFromLastMonths(3);

	        Map<String, Long> bySector = countBySector(recentFeedbacks);
	        Map<FeedbackType, Long> byType = countByType(recentFeedbacks);

	        String topSector = getTopKey(bySector);
	        long topSectorCount = bySector.getOrDefault(topSector, 0L);

	        FeedbackType topType = getTopKey(byType);
	        long topTypeCount = byType.getOrDefault(topType, 0L);

	        return new FeedbackStatsDTO(
	            recentFeedbacks.size(),
	            topSector,
	            topSectorCount,
	            topType,
	            topTypeCount
	        );
	    }
	 
		//DEVOLVE CONTAGEM DE FEEDBACKS POR SETOR ORDENADA E COM LIMITE DE RESPOSTA
		public List<FeedbackSectorStatsDTO> getTopSectorsWithMostFeedbacks(int limit) {
		    Map<String, Long> sectorCounts = countBySector(feedbackRepository.findAll());

		    return sectorCounts.entrySet()
		        .stream()
		        .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
		        .limit(limit)
		        .map(entry -> new FeedbackSectorStatsDTO(entry.getKey(), entry.getValue()))
		        .toList();
		}

	    //MAP CHAVE/VALOR DO ULTIMO ANO COM CONTAGEM POR SETOR, TIPO, CONTAGEM DE FEEDBACKS ANONIMOS 
	    public DashboardStatsDTO getDashboardStats() {
	        List<FeedbackSnapshot> recentFeedbacks = getFeedbacksFromLastMonths(12);

	        Map<String, Long> bySector = countBySector(recentFeedbacks);
	        Map<String, Long> byType = countByTypeAsString(recentFeedbacks);

	        long anonymousCount = recentFeedbacks.stream()
	            .filter(FeedbackSnapshot::isAnonymous)
	            .count();

	        long nonAnonymousCount = recentFeedbacks.size() - anonymousCount;

	        return new DashboardStatsDTO(bySector, byType, anonymousCount, nonAnonymousCount);
	    }

	    // BUSCA FEEDBACKS DOS X MESES ANTERIORES NO BANCO 
	    private List<FeedbackSnapshot> getFeedbacksFromLastMonths(int months) {
	        LocalDateTime start = LocalDateTime.now().minusMonths(months);
	        return feedbackRepository.findByCreatedAtBetween(start, LocalDateTime.now());
	    }

	    //FAZ CONTAGEM DE FEEDBACKS POR SETOR
	    private Map<String, Long> countBySector(List<FeedbackSnapshot> feedbacks) {
	        return feedbacks.stream()
	            .collect(Collectors.groupingBy(FeedbackSnapshot::getSector, Collectors.counting()));
	    }

	    //FAZ CONTAGEM POR TIPO DE FEEDBACK
	    private Map<FeedbackType, Long> countByType(List<FeedbackSnapshot> feedbacks) {
	        return feedbacks.stream()
	            .collect(Collectors.groupingBy(FeedbackSnapshot::getType, Collectors.counting()));
	    }	

	    //FAZ CONTAGEM POR NOME DE TIPO
	    private Map<String, Long> countByTypeAsString(List<FeedbackSnapshot> feedbacks) {
	        return feedbacks.stream()
	            .collect(Collectors.groupingBy(f -> f.getType().name(), Collectors.counting()));
	    }

	    //RETORNA TIPO MAIS FREQUENTE
	    private <T> T getTopKey(Map<T, Long> map) {
	        return map.entrySet().stream()
	            .max(Map.Entry.comparingByValue())
	            .map(Map.Entry::getKey)
	            .orElse(null);
	    }
}
