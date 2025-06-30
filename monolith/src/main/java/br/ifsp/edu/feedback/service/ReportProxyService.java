package br.ifsp.edu.feedback.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import br.ifsp.edu.feedback.dto.feedback.DashboardStatsDTO;
import br.ifsp.edu.feedback.dto.feedback.FeedbackSectorStatsDTO;
import br.ifsp.edu.feedback.dto.feedback.FeedbackStatsDTO;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ReportProxyService {

    private final WebClient reportWebClient;
  
    public Mono<FeedbackStatsDTO> getLastThreeMonthsStats(String token) {
        return reportWebClient.get()
                .uri("/last-three-months")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(FeedbackStatsDTO.class);
    }

    public Mono<List<FeedbackSectorStatsDTO>> getTopSectors(int limit, String token) {
        return reportWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/top-sectors")
                        .queryParam("limit", limit)
                        .build())
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToFlux(FeedbackSectorStatsDTO.class)
                .collectList();
    }

    public Mono<DashboardStatsDTO> getDashboardStats(String token) {
        return reportWebClient.get()
                .uri("/dashboard")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(DashboardStatsDTO.class);
    }

  
}
