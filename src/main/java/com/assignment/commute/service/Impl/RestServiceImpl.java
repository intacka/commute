package com.assignment.commute.service.Impl;

import com.assignment.commute.data.dto.MemberDto;
import com.assignment.commute.data.dto.MemberResponseDto;
import com.assignment.commute.data.dto.RestResponseDto;
import com.assignment.commute.data.entity.Commute;
import com.assignment.commute.data.entity.Member;
import com.assignment.commute.data.entity.Rest;
import com.assignment.commute.data.repository.CommuteRepository;
import com.assignment.commute.data.repository.MemberRepository;
import com.assignment.commute.data.repository.RestRepository;
import com.assignment.commute.service.MemberService;
import com.assignment.commute.service.RestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class RestServiceImpl implements RestService {

    private final RestRepository restRepository;
    private final MemberRepository memberRepository;
    private final CommuteRepository commuteRepository;

    @Autowired
    public RestServiceImpl(RestRepository restRepository,
                           MemberRepository memberRepository,
                           CommuteRepository commuteRepository) {
        this.restRepository = restRepository;
        this.memberRepository = memberRepository;
        this.commuteRepository = commuteRepository;
    }


    @Override // 휴식 시작
    public RestResponseDto startRestTime(Long memberInherentId, String restReason) {

        Rest rest = new Rest();
        rest.setRestStartTime(LocalTime.now());
        rest.setRestReason(restReason);
        rest.setMember(memberRepository.findById(memberInherentId).get());
        rest.setRestDate(LocalDate.now());

        restRepository.save(rest);

        RestResponseDto restResponseDto = new RestResponseDto();
        restResponseDto.setId(rest.getId());
        restResponseDto.setRestStartTime(rest.getRestStartTime());
        restResponseDto.setRestStopTime(rest.getRestStopTime());
        restResponseDto.setRestReason(rest.getRestReason());
        restResponseDto.setRestDate(rest.getRestDate());
        restResponseDto.setMemberId(rest.getMember().getId());
        restResponseDto.setCreatedAt(rest.getCreatedAt());
        restResponseDto.setUpdatedAt(rest.getUpdatedAt());

        return restResponseDto;
    }

    @Override // 휴식 종료
    public RestResponseDto stopRestTime(Long memberInherentId) {

        // 오늘날짜와 회원고유id로 같이 찾아야한다.
        List<Rest> restList = restRepository.findByRestDateAndMemberId(LocalDate.now(), memberInherentId); // 오늘날짜, 회원id 에 해당하는 휴식DB찾기
        Rest fixedRest = new Rest();
        RestResponseDto restResponseDto = new RestResponseDto();

        if(restList!=null) { // 오늘날짜로된 List<Rest>가 존재한다면
            // 휴식종료시간 Column이 비어있다면 LocalDateTime.now()를 추가

            for (int i = 0; i < restList.size(); i++) {
                Rest restOfList = restList.get(i);
                if (restOfList.getRestStopTime()==null) { // 종료시간이 비어있는 데이터를 찾아서 LocalTime.now() 넣기
                    fixedRest = restOfList;
                    fixedRest.setRestStopTime(LocalTime.now());

                    restRepository.save(restOfList);

                    // 휴식시간 계산해서 출퇴근 DB에 더해서 저장해주기
                    LocalTime stopTime = restOfList.getRestStopTime();
                    LocalTime startTime = restOfList.getRestStartTime();
                    LocalTime todayRestTime = stopTime.minusHours(startTime.getHour()).minusMinutes(startTime.getMinute()).minusSeconds(startTime.getSecond());
                    Commute todayCommute = commuteRepository.findByMemberIdAndCommuteDate(memberInherentId, LocalDate.now()).get();

                    // 첫휴식일때, getLastRestTime()에서 null값이 들어올것을 대비해줘야한다.
                    LocalTime finalRestTime;
                    try{
                        finalRestTime = todayCommute.getLastRestTime().plusHours(todayRestTime.getHour()).plusMinutes(todayRestTime.getMinute()).plusSeconds(todayRestTime.getSecond());
                        todayCommute.setLastRestTime(finalRestTime);
                    } catch (NullPointerException e){
                        todayCommute.setLastRestTime(todayRestTime);
                    }

                    commuteRepository.save(todayCommute);

                    restResponseDto.setId(fixedRest.getId());
                    restResponseDto.setRestStartTime(fixedRest.getRestStartTime());
                    restResponseDto.setRestStopTime(fixedRest.getRestStopTime());
                    restResponseDto.setRestReason(fixedRest.getRestReason());
                    restResponseDto.setRestDate(fixedRest.getRestDate());
                    restResponseDto.setMemberId(fixedRest.getMember().getId());
                    restResponseDto.setCreatedAt(fixedRest.getCreatedAt());
                    restResponseDto.setUpdatedAt(fixedRest.getUpdatedAt());

                }
            }

        } else { // 오늘날짜로된 휴식DB가 존재하지않는다면....
            return null;
        }


        return restResponseDto;

    }

    @Override
    public void startRestTime2(Long memberId, String restReason) {
        Rest rest = new Rest();
        rest.setRestStartTime(LocalTime.now());
        rest.setRestReason(restReason);
        rest.setMember(memberRepository.findById(memberId).orElse(null));
        rest.setRestDate(LocalDate.now());

        restRepository.save(rest);
    }

    @Override
    public boolean restStopPossibile(Long id) {

        List<Rest> restList = restRepository.findByRestDateAndMemberId(LocalDate.now(), id); // 오늘날짜, 회원id 에 해당하는 휴식DB찾기
        boolean result = false;
        if(restList!=null) { // 오늘날짜로된 List<Rest>가 존재한다면
            // 휴식종료시간 Column이 비어있다면 LocalDateTime.now()를 추가

            for (int i = 0; i < restList.size(); ++i) {
                Rest restOfList = restList.get(i);
                if (restOfList.getRestStopTime()==null) { // 종료시간이 비어있는 데이터를 찾아서 LocalTime.now() 넣기
                    result = true;
                }
            }

        } else { // 오늘날짜로된 휴식DB가 존재하지않는다면....
            result = false;
        }


        return result;
    }

    @Override
    public void stopRestTime2(Long memberInherentId) {

        // 오늘날짜와 회원고유id로 같이 찾아야한다.
        List<Rest> restList = restRepository.findByRestDateAndMemberId(LocalDate.now(), memberInherentId); // 오늘날짜, 회원id 에 해당하는 휴식DB찾기
        Rest fixedRest = new Rest();

        if(restList!=null) { // 오늘날짜로된 List<Rest>가 존재한다면

            // 휴식종료시간 Column이 비어있다면 LocalDateTime.now()를 추가
            for (int i = 0; i < restList.size(); i++) {
                Rest restOfList = restList.get(i);
                if (restOfList.getRestStopTime()==null) { // 종료시간이 비어있는 데이터를 찾아서 LocalTime.now() 넣기
                    fixedRest = restOfList;
                    fixedRest.setRestStopTime(LocalTime.now());

                    restRepository.save(restOfList);

                    // 휴식시간 계산해서 출퇴근 DB에 더해서 저장해주기
                    LocalTime stopTime = restOfList.getRestStopTime();
                    LocalTime startTime = restOfList.getRestStartTime();
                    LocalTime todayRestTime = stopTime.minusHours(startTime.getHour()).minusMinutes(startTime.getMinute()).minusSeconds(startTime.getSecond());
                    Commute todayCommute = commuteRepository.findByMemberIdAndCommuteDate(memberInherentId, LocalDate.now()).get();

                    // 첫휴식일때, getLastRestTime()에서 null값이 들어올것을 대비해줘야한다.
                    LocalTime finalRestTime;
                    try{
                        finalRestTime = todayCommute.getLastRestTime().plusHours(todayRestTime.getHour()).plusMinutes(todayRestTime.getMinute()).plusSeconds(todayRestTime.getSecond());
                        todayCommute.setLastRestTime(finalRestTime);
                    } catch (NullPointerException e){
                        todayCommute.setLastRestTime(todayRestTime);
                    }

                    commuteRepository.save(todayCommute);

                }
            }

        }


    }

    @Override
    public String viewTodayRest(Long id) {
        String restStartTime = "00:00:00";

        List<Rest> restList = restRepository.findAllByMemberIdAndRestDateOrderByCreatedAt(id, LocalDate.now());
        if (restList==null || restList.isEmpty()) {

        } else {
            for (int i = 0; i < restList.size(); i++) {
                LocalTime localTime = restList.get(i).getRestStartTime();
                restStartTime = String.valueOf(localTime.getHour()) + ":" + String.valueOf(localTime.getMinute()) + ":" + String.valueOf(localTime.getSecond());
            }
        }




        return restStartTime;
    }


}
