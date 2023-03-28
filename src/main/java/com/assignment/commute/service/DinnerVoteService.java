package com.assignment.commute.service;

import com.assignment.commute.data.dto.DinnerVoteDto;
import com.assignment.commute.data.entity.DinnerVote;

import java.util.List;

public interface DinnerVoteService {
    List<DinnerVoteDto> getDinnerVoteList();

    void saveDinnerVote(String menuTime, Long id);

    void addMember(Long id, int dinnerVoteId);

    String getMemberNames(Integer dinnerVoteId);

    void deleteAll();
}
