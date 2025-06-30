package br.ifsp.edu.report.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FeedbackSectorStatsDTO {
    private String sector;
    private long count;
}
