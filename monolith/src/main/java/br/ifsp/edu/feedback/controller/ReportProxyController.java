package br.ifsp.edu.feedback.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.ifsp.edu.feedback.dto.feedback.DashboardStatsDTO;
import br.ifsp.edu.feedback.dto.feedback.FeedbackSectorStatsDTO;
import br.ifsp.edu.feedback.dto.feedback.FeedbackStatsDTO;
import br.ifsp.edu.feedback.service.JwtService;
import br.ifsp.edu.feedback.service.ReportProxyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/proxy/reports")
@RequiredArgsConstructor
public class ReportProxyController {

    private final ReportProxyService proxyService;
    private final JwtService jwtService;

    @Operation(
    	    summary = "Estatísticas dos feedbacks (últimos 3 meses) - Proxy",
    	    description = "Proxy que encaminha a requisição para o microsserviço de relatórios para listar total de feedbacks, setor mais ativo e tipo de feedback mais comum nos últimos 3 meses.",
    	    security = @SecurityRequirement(name = "bearerAuth")
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Estatísticas retornadas com sucesso"),
    	    @ApiResponse(responseCode = "401", description = "Não autorizado"),
    	    @ApiResponse(responseCode = "500", description = "Serviço de relatórios indisponível")
    	})
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/last-three-months")
    public Mono<ResponseEntity<FeedbackStatsDTO>> getStats(HttpServletRequest request) {
        String token = jwtService.extractToken(request);
        return proxyService.getLastThreeMonthsStats(token)
                .map(ResponseEntity::ok);
    }

    @Operation(
    	    summary = "Listar setores com mais feedbacks - Proxy",
    	    description = "Proxy que encaminha a requisição para o microsserviço de relatórios e retorna os setores com maior número de feedbacks, limitado a um número específico.",
    	    security = @SecurityRequirement(name = "bearerAuth")
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Setores retornados com sucesso"),
    	    @ApiResponse(responseCode = "401", description = "Não autorizado"),
    	    @ApiResponse(responseCode = "500", description = "Serviço de relatórios indisponível")
    	})
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/top-sectors")
    public Mono<ResponseEntity<List<FeedbackSectorStatsDTO>>> getTopSectors(
            @RequestParam(defaultValue = "3") int limit,
            HttpServletRequest request) {

        String token = jwtService.extractToken(request);
        return proxyService.getTopSectors(limit, token)
                .map(ResponseEntity::ok);
    }

    @Operation(
    	    summary = "Retorna DTO de Dashboard - Proxy",
    	    description = "Proxy que encaminha a requisição para o microsserviço de relatórios para retornar a contagem de feedbacks por setor, tipo e anonimos vs não anonimos.",
    	    security = @SecurityRequirement(name = "bearerAuth")
    	)
    	@ApiResponses(value = {
    	    @ApiResponse(responseCode = "200", description = "Dados dashboard retornados com sucesso"),
    	    @ApiResponse(responseCode = "401", description = "Não autorizado"),
    	    @ApiResponse(responseCode = "500", description = "Serviço de relatórios indisponível")
    	})
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/dashboard")
    public Mono<ResponseEntity<DashboardStatsDTO>> getDashboardStats(HttpServletRequest request) {
        String token = jwtService.extractToken(request);
        return proxyService.getDashboardStats(token)
                .map(ResponseEntity::ok);
    }

}
