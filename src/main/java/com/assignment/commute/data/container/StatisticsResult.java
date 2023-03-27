package com.assignment.commute.data.container;

import com.assignment.commute.data.dto.CommuteResponseDto;
import lombok.Data;

import java.util.List;

@Data
public class StatisticsResult {

    private List<CommuteResponseDto> commuteResponseDtos;
    private Long goToWorkNumber;

    public StatisticsResult(List<CommuteResponseDto> commuteResponseDtos, Long goToWorkNumber) {
        this.commuteResponseDtos = commuteResponseDtos;
        this.goToWorkNumber = goToWorkNumber;
    }
}
