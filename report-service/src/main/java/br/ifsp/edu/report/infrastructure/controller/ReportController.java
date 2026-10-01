package br.ifsp.edu.report.infrastructure.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.edu.report.application.ReportService;
import br.ifsp.edu.report.dto.DashboardStatsDTO;
import br.ifsp.edu.report.dto.FeedbackSectorStatsDTO;
import br.ifsp.edu.report.dto.FeedbackStatsDTO;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/reports")
public class ReportController {

		private final ReportService reportService;
		
		// - gerar relatórios (contagem de feedbacks por setor, top x setores com mais
		// feedbacks)
		@PreAuthorize("hasRole('ADMIN')")
		@GetMapping("/top-sectors")
		public ResponseEntity<List<FeedbackSectorStatsDTO>> getTopSectors(@RequestParam(defaultValue = "3") int limit) {
			List<FeedbackSectorStatsDTO> topSectors = reportService.getTopSectorsWithMostFeedbacks(limit);
			return ResponseEntity.ok(topSectors);
		}
		
		//- gerar relatórios (contagem de feedbacks nos ultimos 3 meses, contagem tipo com mais feedbacks, setor com mais feedbacks + contagem)
		@PreAuthorize("hasRole('ADMIN')")
		@GetMapping("/last-three-months")
		public ResponseEntity<FeedbackStatsDTO> getLastThreeMonthsStats() {
			 FeedbackStatsDTO stats = reportService.getLastThreeMonthsStats();
			 return ResponseEntity.ok(stats);
		}
		
		
		//- Dashboard simples com estatísticas dos feedbacks 
		@PreAuthorize("hasRole('ADMIN')")		
		@GetMapping("/dashboard")
		public ResponseEntity<DashboardStatsDTO> getDashboardStats() {
			return ResponseEntity.ok(reportService.getDashboardStats());
		}
}

