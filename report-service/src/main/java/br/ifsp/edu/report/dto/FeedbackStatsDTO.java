package br.ifsp.edu.report.dto;

import br.ifsp.edu.report.domain.enums.FeedbackType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FeedbackStatsDTO {
	private long totalFeedbacks;
	private String topSector;
	private long topSectorCount;
	private FeedbackType mostFrequentType;
	private long mostFrequentTypeCount;
}