package com.assignment.commute.service;

import com.assignment.commute.data.dto.RestResponseDto;

import java.time.LocalTime;

public interface RestService {
    RestResponseDto startRestTime(Long commuteId, String restReason);

    RestResponseDto stopRestTime(Long memberInherentId);

    void startRestTime2(Long memberId, String restReason);

    boolean restStopPossibile(Long id);

    void stopRestTime2(Long memberInherentId);

    String viewTodayRest(Long id);
}
