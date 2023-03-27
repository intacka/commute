package com.assignment.commute.service;

import com.assignment.commute.data.dto.CommuteResponseDto;
import com.assignment.commute.data.dto.StatisticsResponseDto;

import com.assignment.commute.data.entity.Commute;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface CommuteService {
    CommuteResponseDto saveGoToWorkTime(Long memberInherentId, String tardyReason);

    CommuteResponseDto saveLeaveTime(Long memberInherentId, String leaveNote);

//    StatisticsResult viewStatistics(String team, String date);

    List<CommuteResponseDto> viewCommuteList(Long memberInherentId);

    CommuteResponseDto viewTodayCommute(Long id);

    void saveStartTime(Long id);

    void saveStartTimeAndMessage(Long id, String message);

    void saveLeaveTime2(Long id, String leaveMessage);

    List<StatisticsResponseDto> viewSelectionCommuteList(String selectBox, String calenderData);

    List<CommuteResponseDto> test();

    Page<Commute> searchInfoList(Pageable pageable, Long memberInherentId);

    Page<StatisticsResponseDto> viewSelectionCommuteList2(String selectBox, String calenderData, Pageable pageable);
}
