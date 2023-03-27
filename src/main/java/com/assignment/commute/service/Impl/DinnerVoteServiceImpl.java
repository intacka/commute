package com.assignment.commute.service.Impl;

import com.assignment.commute.data.dto.DinnerVoteDto;
import com.assignment.commute.data.entity.DinnerVote;
import com.assignment.commute.data.entity.Member;
import com.assignment.commute.data.repository.DinnerVoteRepository;
import com.assignment.commute.data.repository.MemberRepository;
import com.assignment.commute.service.DinnerVoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class DinnerVoteServiceImpl implements DinnerVoteService {

    private final DinnerVoteRepository dinnerVoteRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public DinnerVoteServiceImpl(DinnerVoteRepository dinnerVoteRepository,
                                 MemberRepository memberRepository) {
        this.dinnerVoteRepository = dinnerVoteRepository;
        this.memberRepository = memberRepository;
    }

    public static String[] removeElement(String[] arr, String item) { // 배열값 빼기 메서드
        return Arrays.stream(arr)
                .filter(s -> !s.equals(item))
                .toArray(String[]::new);
    }



    @Override
    public List<DinnerVoteDto> getDinnerVoteList() {
        LocalDate today = LocalDate.now();

        List<DinnerVote> dinnerVoteList = dinnerVoteRepository.findAllByDinnerDateOrderByCreatedAt(today);
        List<DinnerVoteDto> dinnerVoteDtoList = new ArrayList<>();

        if (!dinnerVoteList.isEmpty()) { // 데이터가 있다면
            for (int i = 0; i < dinnerVoteList.size(); i++) {
                DinnerVote dinnerVote = dinnerVoteList.get(i);
                DinnerVoteDto dinnerVoteDto = DinnerVoteDto.builder()
                        .dinnerVoteId(dinnerVote.getDinnerVoteId())
                        .menuTime(dinnerVote.getMenuTime())
                        .dinnerDate(dinnerVote.getDinnerDate())
                        .cntMembers(dinnerVote.getCntMembers())
                        .members(dinnerVote.getMembers())
                        .build();

                dinnerVoteDtoList.add(dinnerVoteDto);
            }
        }
        return dinnerVoteDtoList;
    }

    @Override
    @Transactional
    public void saveDinnerVote(String menuTime, Long id) { // 밥투표 메뉴 추가해주기

        DinnerVote dinnerVote = DinnerVote.builder()
                .dinnerDate(LocalDate.now())
                .menuTime(menuTime)
                .members("")
                .build();

        dinnerVoteRepository.save(dinnerVote);

    }

    @Override
    @Transactional
    public void addMember(Long id, int dinnerVoteId) { // 밥투표 메뉴에 투표하기

        // findByMembers Containg으로 찾기(id + ",")
        // 기존 투표한 이력이 있다면 지워주고 cnt -= 1 해주고
        DinnerVote searchedDinnerVote = dinnerVoteRepository.findByMembersContaining((id + ",")).orElse(null);
        boolean votedResult = false;
        // members를 배열에 담아서, 정말 있는지 확인

        if (searchedDinnerVote!=null) {
            String[] arrayedMemberIds = searchedDinnerVote.getMembers().split(",");
            for (int i = 0; i < arrayedMemberIds.length; i++) {
                if (arrayedMemberIds[i].equals(String.valueOf(id))) { // members에 담겨있는게 맞다면
                    votedResult = true;
                }
            }

        }

        if (votedResult) { // 투표한 이력이 있다면
            searchedDinnerVote.setCntMembers(searchedDinnerVote.getCntMembers() - 1);

            String[] arrayedMemberIds = searchedDinnerVote.getMembers().split(",");
            for (int i = 0; i < arrayedMemberIds.length; i++) {
                if (arrayedMemberIds[i].equals(String.valueOf(id))) { // 만약 똑같은게 있다면, arrayedMemberIds[i]를 배열에서 빼야한다.
                    arrayedMemberIds = removeElement(arrayedMemberIds, String.valueOf(id));
                }
            }

            String str = "";
            for (int j = 0; j < arrayedMemberIds.length; j++) {
                str = str + arrayedMemberIds[j] + ",";
            }
            searchedDinnerVote.setMembers(str);
            dinnerVoteRepository.save(searchedDinnerVote);
        }



        // 새로운 투표이력에 추가해주고 cntMembers += 1 해줘야한다.

        // dinnerVote를 찾고, 거기에 member를 추가해주자
        DinnerVote newSearchedDinnerVote = dinnerVoteRepository.findById(dinnerVoteId).orElse(null); // 프론트에서 찍기 때문에 오류X
        newSearchedDinnerVote.setMembers(newSearchedDinnerVote.getMembers() + (id + ",")); // 기존 목록 + 추가목록
        newSearchedDinnerVote.setCntMembers(newSearchedDinnerVote.getCntMembers() + 1);
        dinnerVoteRepository.save(newSearchedDinnerVote);
        // dinnerVote를 save 할때 회원테이블의 memberId가 바뀌는 오류가 있음 (memberId가 dinnerVoteId로 바뀜)

    }

    @Override
    public String getMemberNames(Integer dinnerVoteId) {
        String finalNames = "";
        DinnerVote dinnerVote = dinnerVoteRepository.findById(dinnerVoteId).orElse(null); // null이 나올 수 없음
        String memberIds = dinnerVote.getMembers();
        String[] arrayedMemberIds = memberIds.split(",");
        if (!(arrayedMemberIds==null || arrayedMemberIds[0].equals(""))) {
            for (int i = 0; i < arrayedMemberIds.length; i++) {
                Member member = memberRepository.findById(Long.parseLong(arrayedMemberIds[i])).orElse(null);
                finalNames = finalNames + member.getName() + ",";
            }
            finalNames = finalNames.substring(0, finalNames.length()-1);
        }

        return finalNames;
    }
}
