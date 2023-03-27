package com.assignment.commute.service.Impl;

import com.assignment.commute.data.dto.CommuteResponseDto;
import com.assignment.commute.data.dto.StatisticsResponseDto;
import com.assignment.commute.data.entity.Commute;
import com.assignment.commute.data.entity.Member;
import com.assignment.commute.data.repository.CommuteRepository;
import com.assignment.commute.data.repository.MemberRepository;
import com.assignment.commute.service.CommuteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;


@Service
public class CommuteServiceImpl implements CommuteService {

    private final CommuteRepository commuteRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public CommuteServiceImpl(CommuteRepository commuteRepository, MemberRepository memberRepository) {
        this.commuteRepository = commuteRepository;
        this.memberRepository = memberRepository;
    }


    // 출근 API
    @Override
    public CommuteResponseDto saveGoToWorkTime(Long memberInherentId, String tardyReason) {
        Optional<Member> memberOptional = memberRepository.findById(memberInherentId);
        Member searchMember = new Member();
        if (memberOptional.isPresent()) {
            searchMember = memberOptional.get();
        }

        // 회원고유id로 연관관계 매핑하면서 save.
        Commute commute = new Commute();
        commute.setCommuteDate(LocalDate.now()); // 날짜
        commute.setStartTime(LocalTime.now()); // 출근시간

        // 지각시간
        if (commute.getStartTime().isAfter(LocalTime.of(10,0,0))) {
            commute.setTardyTime(commute.getStartTime().minusHours(10));
        }

        // 지각사유
        if (tardyReason!=null) {
            commute.setTardyReason(tardyReason);
        }

        // 퇴근시간
        // 근무시간
        // 휴식시간
        // 퇴근비고

        // 멤버
        commute.setMember(searchMember);

        commuteRepository.save(commute);

        CommuteResponseDto commuteResponseDto = new CommuteResponseDto();
        commuteResponseDto.setId(commute.getId());
        commuteResponseDto.setCommuteDate(commute.getCommuteDate());
        commuteResponseDto.setStartTime(commute.getStartTime());
        commuteResponseDto.setLeaveTime(commute.getLeaveTime());
        commuteResponseDto.setLastRestTime(commute.getLastRestTime());
        commuteResponseDto.setWorkingTime(commute.getWorkingTime());
        commuteResponseDto.setTardyTime(commute.getTardyTime());
        commuteResponseDto.setTardyReason(commute.getTardyReason());
        commuteResponseDto.setLeaveNote(commute.getLeaveNote());
        commuteResponseDto.setMemberId(commute.getMember().getId());
        commuteResponseDto.setCreatedAt(commute.getCreatedAt());
        commuteResponseDto.setUpdatedAt(commute.getUpdatedAt());

        return commuteResponseDto;

    }

    // 퇴근 API
    @Override
    public CommuteResponseDto saveLeaveTime(Long memberInherentId, String leaveNote) {
                                                                // 회원고유id와 날짜(오늘)로 Commute(출퇴근) 객체를 찾습니다.
        Commute searchCommute = commuteRepository.findByMemberIdAndCommuteDate(memberInherentId, LocalDate.now()).get();

        // 퇴근시간 -> 퇴근버튼누른시간을 퇴근시간으로 DB에 저장
        searchCommute.setLeaveTime(LocalTime.now());

        // 퇴근비고 -> 퇴근비고를 써넣은것이 있다면 DB에 저장
        if (leaveNote!=null) {
            searchCommute.setLeaveNote(leaveNote);
        }

        // 근무시간 : (버튼을 누른 현재시간) - (출근했던시간) - (휴식했던시간)
        LocalTime startTime = searchCommute.getStartTime(); // 출근했던시간
        LocalTime lastWorkingTime = LocalTime.now().minusHours(startTime.getHour()).minusMinutes(startTime.getMinute()).minusSeconds(startTime.getSecond()); // 근무시간(근무+휴식) = 지금시간 - 출근시간
        LocalTime lastRestTime = searchCommute.getLastRestTime(); // 휴식했던시간
        lastWorkingTime = lastWorkingTime.minusHours(lastRestTime.getHour()).minusMinutes(lastRestTime.getMinute()).minusSeconds(lastRestTime.getSecond()); // 최종근무시간 = 근무시간(근무+휴식) - 휴식했던시간

        searchCommute.setWorkingTime(lastWorkingTime);      // 최종 근무시간을 반영





        // (최종휴식시간) -> 이미 저장된 휴식시간으로...
        // (날짜)
        // (출근시간)
        // (지각시간)
        // (지각사유)
        // (멤버객체)

        commuteRepository.save(searchCommute);

        CommuteResponseDto commuteResponseDto = new CommuteResponseDto();

        commuteResponseDto.setId(searchCommute.getId());
        commuteResponseDto.setCommuteDate(searchCommute.getCommuteDate());
        commuteResponseDto.setStartTime(searchCommute.getStartTime());
        commuteResponseDto.setLeaveTime(searchCommute.getLeaveTime());
        commuteResponseDto.setLastRestTime(searchCommute.getLastRestTime());
        commuteResponseDto.setWorkingTime(searchCommute.getWorkingTime());
        commuteResponseDto.setTardyTime(searchCommute.getTardyTime());
        commuteResponseDto.setTardyReason(searchCommute.getTardyReason());
        commuteResponseDto.setLeaveNote(searchCommute.getLeaveNote());
        commuteResponseDto.setMemberId(searchCommute.getMember().getId());
        commuteResponseDto.setCreatedAt(searchCommute.getCreatedAt());
        commuteResponseDto.setUpdatedAt(searchCommute.getUpdatedAt());

        return commuteResponseDto;
    }





    @Override
    public CommuteResponseDto viewTodayCommute(Long id) {


//        Commute commute = commuteRepository.findByIdAndCommuteDate(id,LocalDate.now()).orElse(null);
        Commute commute = commuteRepository.findByMemberIdAndCommuteDate(id,LocalDate.now()).orElse(null);
//        System.out.println("commute 서비스 내의 getworkingtime : " + commute.getWorkingTime()); // null로 들어오는 문제가 있음

        CommuteResponseDto commuteResponseDto = new CommuteResponseDto();
        if (commute==null) {
            return commuteResponseDto;
        } else {
            commuteResponseDto.setId(commute.getId());
            commuteResponseDto.setCommuteDate(commute.getCommuteDate());
            commuteResponseDto.setStartTime(commute.getStartTime());
            commuteResponseDto.setLeaveTime(commute.getLeaveTime());
            commuteResponseDto.setLastRestTime(commute.getLastRestTime());
            commuteResponseDto.setWorkingTime(commute.getWorkingTime());
            commuteResponseDto.setTardyTime(commute.getTardyTime());
            commuteResponseDto.setTardyReason(commute.getTardyReason());
            commuteResponseDto.setLeaveNote(commute.getLeaveNote());
            commuteResponseDto.setMemberId(commute.getMember().getId());
            commuteResponseDto.setCreatedAt(commute.getCreatedAt());
            commuteResponseDto.setUpdatedAt(commute.getUpdatedAt());

            return commuteResponseDto;
        }


    }

    @Override
    public void saveStartTime(Long id) {
//        Optional<Member> memberOptional = memberRepository.findById(memberInherentId);
//        Member searchMember = new Member();
//        if (memberOptional.isPresent()) {
//            searchMember = memberOptional.get();
//        }

        // 회원고유id로 연관관계 매핑하면서 save.
        Commute commute = new Commute();
        commute.setCommuteDate(LocalDate.now()); // 날짜
        commute.setStartTime(LocalTime.now()); // 출근시간

//        // 지각시간
//        if (commute.getStartTime().isAfter(LocalTime.of(10,0,0))) {
//            commute.setTardyTime(commute.getStartTime().minusHours(10));
//        }

        // 지각사유
//        if (tardyReason!=null) {
//            commute.setTardyReason(tardyReason);
//        }

        // 퇴근시간
        // 근무시간
        // 휴식시간
        // 퇴근비고

        // 멤버
        commute.setMember(memberRepository.findById(id).orElse(null));

        commuteRepository.save(commute);
    }

    @Override
    public void saveStartTimeAndMessage(Long id, String message) {
        Commute commute = new Commute();
        commute.setCommuteDate(LocalDate.now()); // 날짜
        commute.setStartTime(LocalTime.now()); // 출근시간

        commute.setTardyReason(message);

        // 지각시간
        commute.setTardyTime(commute.getStartTime().minusHours(10));

        // 멤버
        commute.setMember(memberRepository.findById(id).orElse(null));

        commuteRepository.save(commute);

    }

    @Override
    public void saveLeaveTime2(Long memberInherentId, String leaveMessage) {
        // 회원고유id와 날짜(오늘)로 Commute(출퇴근) 객체를 찾습니다.
        Commute searchCommute = commuteRepository.findByMemberIdAndCommuteDate(memberInherentId, LocalDate.now()).orElse(null);

        // 퇴근시간 -> 퇴근버튼누른시간을 퇴근시간으로 DB에 저장
        searchCommute.setLeaveTime(LocalTime.now());

        // 퇴근비고 -> 퇴근비고를 써넣은것이 있다면 DB에 저장
        if (leaveMessage!=null || !leaveMessage.equals("")) {
            searchCommute.setLeaveNote(leaveMessage);
        }

        // 근무시간 : (버튼을 누른 현재시간) - (출근했던시간) - (휴식했던시간)
        LocalTime startTime = searchCommute.getStartTime(); // 출근했던시간
        LocalTime lastWorkingTime = LocalTime.now().minusHours(startTime.getHour()).minusMinutes(startTime.getMinute()).minusSeconds(startTime.getSecond()); // 근무시간(근무+휴식) = 지금시간 - 출근시간

        // if () // 휴식했던시간이 비어있다면????


        LocalTime lastRestTime; // 휴식했던시간

        try {
            lastRestTime = searchCommute.getLastRestTime();
            lastWorkingTime = lastWorkingTime.minusHours(lastRestTime.getHour()).minusMinutes(lastRestTime.getMinute()).minusSeconds(lastRestTime.getSecond()); // 최종근무시간 = 근무시간(근무+휴식) - 휴식했던시간
        } catch (Exception e){

        }

        searchCommute.setWorkingTime(lastWorkingTime);      // 최종 근무시간을 반영





        // (최종휴식시간) -> 이미 저장된 휴식시간으로...
        // (날짜)
        // (출근시간)
        // (지각시간)
        // (지각사유)
        // (멤버객체)

        commuteRepository.save(searchCommute);

    }
    @Override ///////////////////////////////////////////////////////////////////////////// 페이징처리 수정해야함 //////////////
    public List<CommuteResponseDto> viewCommuteList(Long memberInherentId) {

        List<Commute> commuteList = commuteRepository.findAllByMemberIdOrderByCreatedAtDesc(memberInherentId);
        List<CommuteResponseDto> commuteResponseDtoList = new ArrayList<>();

        for (int i = 0; i < commuteList.size();i++) {
            Commute commute = commuteList.get(i);
            CommuteResponseDto commuteResponseDto = new CommuteResponseDto();
            commuteResponseDto.setCommuteDate(commute.getCommuteDate());
            commuteResponseDto.setStartTime(commute.getStartTime());
            commuteResponseDto.setLeaveTime(commute.getLeaveTime());
            commuteResponseDto.setLastRestTime(commute.getLastRestTime());
            commuteResponseDto.setWorkingTime(commute.getWorkingTime());
            commuteResponseDto.setTardyTime(commute.getTardyTime());
            commuteResponseDto.setTardyReason(commute.getTardyReason());
            commuteResponseDto.setLeaveNote(commute.getLeaveNote());
            commuteResponseDto.setMemberId(commute.getMember().getId());
            commuteResponseDto.setCreatedAt(commute.getCreatedAt());
            commuteResponseDto.setUpdatedAt(commute.getUpdatedAt());
            commuteResponseDto.setId(commute.getId());

            commuteResponseDtoList.add(commuteResponseDto);
        }

        return commuteResponseDtoList;
    }
    @Override
    public List<StatisticsResponseDto> viewSelectionCommuteList(String selectBox, String calenderData) {
        List<StatisticsResponseDto> statisticsResponseDtoList = new ArrayList<>(); // 반환해줄 출퇴근 LIST

        LocalDate seletedDate;
        if (calenderData==null) {
            seletedDate = LocalDate.now();
        } else {
            String[] splitCalenderData = calenderData.split("-");
            seletedDate = LocalDate.of(Integer.parseInt(splitCalenderData[0]),Integer.parseInt(splitCalenderData[1]),Integer.parseInt(splitCalenderData[2]));
        }

        // 팀이름에 해당하는 회원id List를 찾는다.
        if (selectBox==null || selectBox.equals("") || selectBox.equals("팀선택")) { // 팀이름을 선택안했다면
            // 오늘날짜에만 해당하는 데이터들을 싹다 찾는다.
            List<Commute> commuteList = commuteRepository.findAllByCommuteDateOrderByCreatedAtDesc(seletedDate);
            for (int i = 0; i < commuteList.size(); ++i) {
                Commute commute = commuteList.get(i);
                StatisticsResponseDto statisticsResponseDto = new StatisticsResponseDto();
                statisticsResponseDto.setId(commute.getId());
                statisticsResponseDto.setStartTime(commute.getStartTime());
                statisticsResponseDto.setLeaveTime(commute.getLeaveTime());
                statisticsResponseDto.setLastRestTime(commute.getLastRestTime());
                ////
                statisticsResponseDto.setWorkingTime(commute.getWorkingTime());
                ////
                statisticsResponseDto.setTardyTime(commute.getTardyTime());
                statisticsResponseDto.setTardyReason(commute.getTardyReason());
                statisticsResponseDto.setLeaveNote(commute.getLeaveNote());
                // memberId로 Name찾아서 해주자
                Member member = memberRepository.findById(commute.getMember().getId()).orElse(null);
                statisticsResponseDto.setName(member.getName());

                statisticsResponseDtoList.add(statisticsResponseDto);
            }


//////// 날짜로 같이 찾아야함 //////////


        } else { // 팀이름을 선택한게 있으면 -> selected
            // 팀이름에 해당하는 회원id List를 찾는다.
            // 그 회원id에 해당하고, 날짜는 해당날짜(임시로 오늘날짜)로 출퇴근목록들을 꺼내서 commuteResponseDtoList에 담는다



            List<Member> memberList = memberRepository.findAllByTeam(selectBox);
            if (memberList==null || memberList.isEmpty() || memberList.size()==0 ) {
//                List<Commute> commuteList = commuteRepository.findAllByCommuteDateOrderByCreatedAtDesc(seletedDate);
//                for (int i = 0; i < commuteList.size(); ++i) {
//                    Commute commute = commuteList.get(i);
//                    StatisticsResponseDto statisticsResponseDto = new StatisticsResponseDto();
//                    statisticsResponseDto.setId(commute.getId());
//                    statisticsResponseDto.setStartTime(commute.getStartTime());
//                    statisticsResponseDto.setLeaveTime(commute.getLeaveTime());
//                    statisticsResponseDto.setLastRestTime(commute.getLastRestTime());
//                    ////
//                    statisticsResponseDto.setWorkingTime(commute.getWorkingTime());
//                    ////
//                    statisticsResponseDto.setTardyTime(commute.getTardyTime());
//                    statisticsResponseDto.setTardyReason(commute.getTardyReason());
//                    statisticsResponseDto.setLeaveNote(commute.getLeaveNote());
//                    // memberId로 Name찾아서 해주자
//                    Member member = memberRepository.findById(commute.getMember().getId()).orElse(null);
//                    statisticsResponseDto.setName(member.getName());
//
//                    statisticsResponseDtoList.add(statisticsResponseDto);
//                }
            } else {
                List<Commute> commuteList = new ArrayList<>();
                for (int i = 0; i < memberList.size(); ++i) {
                    Commute commute = commuteRepository.findByMemberIdAndCommuteDate(memberList.get(i).getId(), seletedDate).orElse(null);
                    if (commute!=null) {

                        StatisticsResponseDto statisticsResponseDto = new StatisticsResponseDto();
                        statisticsResponseDto.setId(commute.getId());
                        statisticsResponseDto.setStartTime(commute.getStartTime());
                        statisticsResponseDto.setLeaveTime(commute.getLeaveTime());
                        statisticsResponseDto.setLastRestTime(commute.getLastRestTime());
                        ////
                        statisticsResponseDto.setWorkingTime(commute.getWorkingTime());
                        ////
                        statisticsResponseDto.setTardyTime(commute.getTardyTime());
                        statisticsResponseDto.setTardyReason(commute.getTardyReason());
                        statisticsResponseDto.setLeaveNote(commute.getLeaveNote());
                        // memberId로 Name찾아서 해주자
                        Member member = memberRepository.findById(commute.getMember().getId()).orElse(null);
                        statisticsResponseDto.setName(member.getName());

                        statisticsResponseDtoList.add(statisticsResponseDto);
                    }



                }


            }




        }



        statisticsResponseDtoList = statisticsResponseDtoList.stream().sorted(Comparator.comparing(StatisticsResponseDto::getStartTime).reversed()).collect(Collectors.toList());

//        Collections.sort(statisticsResponseDtoList, Collections.reverseOrder());

        return statisticsResponseDtoList;
    }

    @Override
    public List<CommuteResponseDto> test() {
        Page<Commute> commutePage = commuteRepository.findAll(PageRequest.of(0,12, Sort.by(Sort.Direction.DESC, "CreatedAt")));
        System.out.println("commutePage : " + commutePage);

        return null;
    }

    @Override
    public Page<Commute> searchInfoList(Pageable pageable, Long memberInherentId) {
        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable= PageRequest.of(page,10);


        Page<Commute> commuteList = commuteRepository.findAllByMemberIdOrderByCreatedAtDesc(pageable, memberInherentId);

        return commuteList;



    }

    @Override
    public Page<StatisticsResponseDto> viewSelectionCommuteList2(String selectBox, String calenderData, Pageable pageable) {
//        int page = (pageable.getPageNumber() == 0) ? 0 : (pageable.getPageNumber() - 1); // page는 index 처럼 0부터 시작
        pageable= PageRequest.of(pageable.getPageNumber(),20);


            List<StatisticsResponseDto> statisticsResponseDtoList = new ArrayList<>(); // 반환해줄 출퇴근 LIST

            LocalDate seletedDate;
            if (calenderData==null || calenderData.equals("")) {
                seletedDate = LocalDate.now();
            } else {
                String[] splitCalenderData = calenderData.split("-");
                seletedDate = LocalDate.of(Integer.parseInt(splitCalenderData[0]),Integer.parseInt(splitCalenderData[1]),Integer.parseInt(splitCalenderData[2]));
            }

            // 팀이름에 해당하는 회원id List를 찾는다.
            if (selectBox==null || selectBox.equals("") || selectBox.equals("팀선택")) { // 팀이름을 선택안했다면
                // 오늘날짜에만 해당하는 데이터들을 싹다 찾는다.
                List<Commute> commuteList = commuteRepository.findAllByCommuteDateOrderByCreatedAtDesc(seletedDate);
                for (int i = 0; i < commuteList.size(); ++i) {
                    Commute commute = commuteList.get(i);
                    StatisticsResponseDto statisticsResponseDto = new StatisticsResponseDto();
                    statisticsResponseDto.setId(commute.getId());
                    statisticsResponseDto.setStartTime(commute.getStartTime());
                    statisticsResponseDto.setLeaveTime(commute.getLeaveTime());
                    statisticsResponseDto.setLastRestTime(commute.getLastRestTime());
                    ////
                    statisticsResponseDto.setWorkingTime(commute.getWorkingTime());
                    ////
                    statisticsResponseDto.setTardyTime(commute.getTardyTime());
                    statisticsResponseDto.setTardyReason(commute.getTardyReason());
                    statisticsResponseDto.setLeaveNote(commute.getLeaveNote());
                    // memberId로 Name찾아서 해주자
                    Member member = memberRepository.findById(commute.getMember().getId()).orElse(null);
                    statisticsResponseDto.setName(member.getName());

                    statisticsResponseDtoList.add(statisticsResponseDto);
                }
            } else { // 팀이름을 선택한게 있으면 -> selected

                // 팀이름에 해당하는 회원id List를 찾는다.
                // 그 회원id에 해당하고, 날짜는 해당날짜(임시로 오늘날짜)로 출퇴근목록들을 꺼내서 commuteResponseDtoList에 담는다

                List<Member> memberList = memberRepository.findAllByTeam(selectBox);
                if (memberList!=null || !memberList.isEmpty() || memberList.size()!=0) {

                    List<Commute> commuteList = new ArrayList<>();
                    for (int i = 0; i < memberList.size(); i++) {
                        Commute commute = commuteRepository.findByMemberIdAndCommuteDate(memberList.get(i).getId(), seletedDate).orElse(null);
                        if (commute!=null) {

                            StatisticsResponseDto statisticsResponseDto = new StatisticsResponseDto();
                            statisticsResponseDto.setId(commute.getId());
                            statisticsResponseDto.setStartTime(commute.getStartTime());
                            statisticsResponseDto.setLeaveTime(commute.getLeaveTime());
                            statisticsResponseDto.setLastRestTime(commute.getLastRestTime());
                            ////
                            statisticsResponseDto.setWorkingTime(commute.getWorkingTime());
                            ////
                            statisticsResponseDto.setTardyTime(commute.getTardyTime());
                            statisticsResponseDto.setTardyReason(commute.getTardyReason());
                            statisticsResponseDto.setLeaveNote(commute.getLeaveNote());
                            // memberId로 Name찾아서 해주자
                            Member member = memberRepository.findById(commute.getMember().getId()).orElse(null);
                            statisticsResponseDto.setName(member.getName());

                            statisticsResponseDtoList.add(statisticsResponseDto);
                        }
                    }
                }
            }

            //정렬
            statisticsResponseDtoList = statisticsResponseDtoList.stream().sorted(Comparator.comparing(StatisticsResponseDto::getStartTime).reversed()).collect(Collectors.toList());
            int start = (int)pageable.getOffset();
            int end = Math.min((start + pageable.getPageSize()), statisticsResponseDtoList.size());
            Page<StatisticsResponseDto> result = new PageImpl<>(statisticsResponseDtoList.subList(start, end), pageable, statisticsResponseDtoList.size());
        return result;
    }


}


























