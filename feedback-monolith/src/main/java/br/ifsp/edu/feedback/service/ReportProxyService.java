package br.ifsp.edu.feedback.service;

import java.time.Duration;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.server.ResponseStatusException;

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
                .bodyToMono(FeedbackStatsDTO.class)
                .timeout(Duration.ofSeconds(4)) 
                .onErrorMap(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED || ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                        return new ResponseStatusException(ex.getStatusCode(), "Falha de autorização no serviço de relatórios");
                    }
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Serviço de relatórios indisponível", ex);
                })
                .onErrorMap(java.util.concurrent.TimeoutException.class, ex -> 
                    new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Tempo limite excedido ao consultar o serviço de relatórios", ex)
                )
                .onErrorMap(ex -> !(ex instanceof ResponseStatusException), ex ->
                    new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Serviço de relatórios indisponível", ex)
                );
    }

    public Mono<List<FeedbackSectorStatsDTO>> getTopSectors(int limit, String token) {
        return reportWebClient.get()
                .uri(uriBuilder -> uriBuilder.path("/top-sectors")
                        .queryParam("limit", limit)
                        .build())
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToFlux(FeedbackSectorStatsDTO.class)
                .collectList()
                .timeout(Duration.ofSeconds(4)) 
                .onErrorMap(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED || ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                        return new ResponseStatusException(ex.getStatusCode(), "Falha de autorização no serviço de relatórios");
                    }
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Serviço de relatórios indisponível", ex);
                })
                .onErrorMap(java.util.concurrent.TimeoutException.class, ex -> 
                    new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Tempo limite excedido ao consultar o serviço de relatórios", ex)
                )
                .onErrorMap(ex -> !(ex instanceof ResponseStatusException), ex ->
                    new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Serviço de relatórios indisponível", ex)
                );
    }

    public Mono<DashboardStatsDTO> getDashboardStats(String token) {
        return reportWebClient.get()
                .uri("/dashboard")
                .headers(headers -> headers.setBearerAuth(token))
                .retrieve()
                .bodyToMono(DashboardStatsDTO.class)
                .timeout(Duration.ofSeconds(4)) 
                .onErrorMap(WebClientResponseException.class, ex -> {
                    if (ex.getStatusCode() == HttpStatus.UNAUTHORIZED || ex.getStatusCode() == HttpStatus.FORBIDDEN) {
                        return new ResponseStatusException(ex.getStatusCode(), "Falha de autorização no serviço de relatórios");
                    }
                    return new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Serviço de relatórios indisponível", ex);
                })
                .onErrorMap(java.util.concurrent.TimeoutException.class, ex -> 
                    new ResponseStatusException(HttpStatus.GATEWAY_TIMEOUT, "Tempo limite excedido ao consultar o serviço de relatórios", ex)
                )
                .onErrorMap(ex -> !(ex instanceof ResponseStatusException), ex ->
                    new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Serviço de relatórios indisponível", ex)
                );
    }

  
}
