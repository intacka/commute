package com.assignment.commute.service.Impl;

import com.assignment.commute.config.JwtManager;
import com.assignment.commute.data.LunarCalendar;
import com.assignment.commute.data.dto.MemberCalendarDto;
import com.assignment.commute.data.dto.MemberDto;
import com.assignment.commute.data.dto.MemberResponseDto;
import com.assignment.commute.data.entity.Member;
import com.assignment.commute.data.entity.MemberCalendar;
import com.assignment.commute.data.repository.MemberCalendarRepository;
import com.assignment.commute.data.repository.MemberRepository;
import com.assignment.commute.service.MemberService;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Random;
import java.util.*;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder bCryptPasswordEncoder;
    private final JwtManager jwtManager;
    private final MemberCalendarRepository memberCalendarRepository;
    private final JPAQueryFactory queryFactory;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository,
                             PasswordEncoder bCryptPasswordEncoder,
                             JwtManager jwtManager,
                             MemberCalendarRepository memberCalendarRepository,
                             JPAQueryFactory queryFactory) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtManager = jwtManager;
        this.memberCalendarRepository = memberCalendarRepository;
        this.queryFactory = queryFactory;
    }

    // 로그인
    @Override
    public boolean login(String id, String pwd) {
        boolean result = false;
        Optional<Member> optionalMember = memberRepository.findByMemberId(id);

        if (optionalMember.isPresent()) { // id가 DB에서 조회되는 id라면,
//            if (pwd.equals(optionalMember.get().getMemberPw())) { // 그리고 그 id에 해당하는 pw가, 입력한 pwd와 같다면-> 로그인 성공상황
            if (bCryptPasswordEncoder.matches(pwd, optionalMember.get().getMemberPw())) { // 그리고 그 id에 해당하는 pw가, 입력한 pwd와 같다면-> 로그인 성공상황
                result = true;
            } else if (!pwd.equals(optionalMember.get().getMemberPw())) {   // pw가 일치하지 않는다면,
                result = false;
            }
        } else if (optionalMember.isEmpty()) { // id가 DB에서 조회되지 않는다면,
            result = false;
        }
        return result;
    }

    @Override
    public MemberResponseDto saveMember(MemberDto memberDto) {


        Member member = new Member();
        member.setMemberId(memberDto.getMemberId());
        member.setTeam(memberDto.getTeam());
        member.setName(memberDto.getName());
        member.setRole(memberDto.getRole());

//      member.setMemberPw(memberDto.getMemberPw());
        String encodedPassword = bCryptPasswordEncoder.encode(memberDto.getMemberPw());
        member.setMemberPw(encodedPassword);

        memberRepository.save(member);

        MemberResponseDto memberResponseDto = new MemberResponseDto();
        memberResponseDto.setId(member.getId());
        memberResponseDto.setMemberId(member.getMemberId());
        memberResponseDto.setMemberPw(member.getMemberPw());
        memberResponseDto.setTeam(member.getTeam());
        memberResponseDto.setName(member.getName());
        memberResponseDto.setRole(member.getRole());
        memberResponseDto.setCreatedAt(member.getCreatedAt());
        memberResponseDto.setUpdatedAt(member.getUpdatedAt());

        return memberResponseDto;

    }

    @Override
    public String test() {

        Member member = new Member();
        member.setMemberId("testid");
        member.setTeam("testteam");
        member.setName("testname");

//      member.setMemberPw(memberDto.getMemberPw());
        String encodedPassword = bCryptPasswordEncoder.encode("testpw");
        member.setMemberPw(encodedPassword);

        memberRepository.save(member);


        final String token = jwtManager.generateJwtToken(member); // 토큰 생성
        String usernameFromToken = jwtManager.getUsernameFromToken(token); // 토큰으로부터 username 가져오기







        return token;
    }

    @Override
    public String searchName(Long id) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        String name = optionalMember.get().getName();


        return name;
    }

    @Override
    public String searchName(String memberId) {
        Optional<Member> optionalMember = memberRepository.findByMemberId(memberId);
        String name = optionalMember.get().getName();


        return name;
    }

    @Override
    public Long searchId(String memberId) {

        Optional<Member> optionalMember = memberRepository.findByMemberId(memberId);
        Long id = optionalMember.get().getId();

        return id;
    }

    @Override
    public boolean searchMember(Long id) {

        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public List<MemberCalendarDto> findTest() {
        Random random = new Random();
        // 반환할 데이터
        List<MemberCalendarDto> memberCalendarDtoList = new ArrayList<>();

        LocalDate localToday = LocalDate.now();  // 월요일:1 , 일요일:7            즉, 5,6,7은 빼야한다.
        // 기존 데이터 가져오기
        List<MemberCalendar> prevMemberCalendarList = memberCalendarRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
        if (!(prevMemberCalendarList==null||prevMemberCalendarList.isEmpty())) {
            for (int i = 0; i < prevMemberCalendarList.size();i++) {
                MemberCalendar memberCalendar = prevMemberCalendarList.get(i);
                MemberCalendarDto memberCalendarDto = new MemberCalendarDto();
                memberCalendarDto.setId(memberCalendar.getId());
                memberCalendarDto.setStart(memberCalendar.getStart());
                memberCalendarDto.setTitle(memberCalendar.getTitle());
                memberCalendarDto.setMemberId(memberCalendar.getMember().getId());

                memberCalendarDtoList.add(memberCalendarDto);
            }
        }


        List<Object[]> list = memberCalendarRepository.countGruopByStart();

        if (!(list==null || list.isEmpty())) { // 값이 있으면
            String searchedLastDate = list.get(0)[0].toString(); // 마지막날짜.
            String[] splittedDate = searchedLastDate.split("-");
            LocalDate aaaaa = LocalDate.of(Integer.parseInt(splittedDate[0]),Integer.parseInt(splittedDate[1]),Integer.parseInt(splittedDate[2]));
            localToday = aaaaa.plusDays(1);
        }
        // 날짜설정 완료
//////////////////////////////////////

        // 금토일 조건부터 빼주고, 공휴일 조건도 넘겨줘야 한다.
        while(localToday.getDayOfWeek().getValue() >= 5) {
            localToday = localToday.plusDays(1);
        }


        // 공휴일 정보가져오기
        int viewYear = localToday.getYear();
        LunarCalendar lunarCalendar = new LunarCalendar();
        Set<String> stringSet = lunarCalendar.holidayArray(String.valueOf(viewYear)); // 기준 날짜의 년도에 해당하는 공휴일


        // 공휴일 Set을 정렬된 List로
        List<String> sortedSetList  = new ArrayList<>();
        Iterator iter = stringSet.iterator();
        while(iter.hasNext()){
            sortedSetList.add((String) iter.next());
        }
        Collections.sort(sortedSetList);

        // todayT와 stringSet (공휴일) 이 같은게 있다면 하루씩 증가시켜주기
        // todayT는 기준날짜String.
        for (int i = 0; i < sortedSetList.size(); i++) {
            String yearT = String.valueOf(localToday.getYear());
            String monthT = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
            String dayT = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());
            String todayT = yearT + monthT + dayT;
            if (todayT.equals(sortedSetList.get(i))) { // 공휴일과 같은게 있다면
                localToday = localToday.plusDays(1); // 1일증가
                while(localToday.getDayOfWeek().getValue() >= 5) {
                    localToday = localToday.plusDays(1);
                }
            }

        }




        String year = String.valueOf(localToday.getYear());
        String month = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
        String day = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());
        String today = year + "-" + month + "-" + day;


        List<Member> memberList = memberRepository.findAll();
        List<Member> newMemberList = new ArrayList<>();
        if(!(memberList==null || memberList.isEmpty() || memberList.equals(""))) { // 값존재하면 // 4명씩 묶기
//            Collections.shuffle(memberList); // 순서 섞어주기 ////////////////////////////////////////////////////////////////
            // 팀 목록을 가져오자 -> GroupBy
            List<String> teamList = new ArrayList<>();
            List<Object[]> objects = memberRepository.findTeamGroupBy();
            for (int s = 0; s < objects.size(); s++) { // 팀이 null일때 오류처리 해줘야한다.
                String searchedTeam = objects.get(s)[0].toString(); // 팀
                teamList.add(searchedTeam);
            }
            // 팀 리스트도 섞어야 한다.
            Collections.shuffle(teamList); ///// ************************************************************************************************************************ /////
            System.out.println("랜덤으로 섞은후의 팀리스트 : " + teamList);
            List<List<Member>> memberListTempList = new ArrayList<>();
            for (int j = 0; j < teamList.size(); j++) {
                memberListTempList.add(memberRepository.findAllByTeam(teamList.get(j)));
            }

            while(true) {
                for (int i = 0; i < teamList.size(); i++) { //  수정해야함
//                    List<Member> memberListTemp = memberRepository.findAllByTeam(teamList.get(i)); // 팀에서 한명씩 뽑아서 memberList에다가 주기 // 뽑을 팀이 없으면다음팀으로
                    // memberListTemp에서 랜덤으로 한명 뽑는다.

                    List<Member> memberListTemp = memberListTempList.get(i);

                    if (!(memberListTemp==null || memberListTemp.isEmpty())) { // 있다면 뽑고 없다면 뽑지말자
//                        int ranInt = (int)Math.random() * memberListTemp.size();
                        System.out.println("memberListTemp : " + memberListTemp.size());
                        int ranInt = random.nextInt(memberListTemp.size());
                        Member searchedMember = memberListTemp.get(ranInt);
                        newMemberList.add(searchedMember); // new List에 추가해주고
                        memberListTempList.get(i).remove(searchedMember); // old List에 삭제해준다. // DB를 삭제해주는게 아니니까 계속 무한루프가 나타날 수 밖에 없다.?? // memberListTemp는 계속 DB에서 찾으니까!!!!
                        System.out.println("old member list : " + memberListTempList.get(i).size());

                    }
                }

//                이차원list까지 모두 null (isEmpty) 일때!!!
//                        ----->>>>>> break;
                int[] consume = new int[teamList.size()];
                int breakResult = 1;
                for (int j = 0; j < teamList.size(); j++) {
                    if (memberListTempList.get(j).isEmpty()) {
                        breakResult *= 1;
                    } else {
                        breakResult *= 0;
                    }
                }

                if (breakResult==1) {
                    break;
                }

            }

            // newMemberList대로 뽑으면 된다.



            System.out.println("수정전 list : " + newMemberList);






            int size = newMemberList.size();
            int quotient = size / 4; // 2가 나온다.
            int remainder = size % 4; // 3이 나온다.

            if (remainder==1) { // 만약에 나머지가 1명이남는다면?
                if (quotient==0) { // -> 근데 몫이 0이라면? (총인원=1) : 1명 그대로진행
                    for (int i = 0; i < size;i++) {
                        Member member = newMemberList.get(i);
                        MemberCalendarDto memberCalendarDto = new MemberCalendarDto();
                        MemberCalendar memberCalendar = new MemberCalendar();

                        memberCalendar.setTitle(member.getName());
                        memberCalendar.setStart(today);
                        memberCalendar.setMember(member);
                        memberCalendarRepository.save(memberCalendar);
                        memberCalendarDto.setId(memberCalendar.getId());
                        memberCalendarDto.setTitle(memberCalendar.getTitle());
                        memberCalendarDto.setStart(memberCalendar.getStart());
                        memberCalendarDto.setMemberId(memberCalendar.getMember().getId());

                        memberCalendarDtoList.add(memberCalendarDto);
                    }
                } else if (quotient==1) { // -> 근데 몫이 1이라면? (총인원=5) : 3명, 2명
                    for (int i = 0; i < size;i++) {
                        if ((i == size-2)) {
                            localToday = localToday.plusDays(1);
                            //////////////// 플러스 1일 했는데 -> 금토일공휴일이면 제끼기

                            // 금토일 조건부터 빼주고, 공휴일 조건도 넘겨준날짜가 진짜날짜
                            while(localToday.getDayOfWeek().getValue() >= 5) {
                                localToday = localToday.plusDays(1);
                            }

                            // todayT와 stringSet (공휴일) 이 같은게 있다면 하루씩 증가시켜주기
                            for (int k = 0; k < sortedSetList.size(); k++) {
                                String yearT = String.valueOf(localToday.getYear());
                                String monthT = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                                String dayT = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());
                                String todayT = yearT + monthT + dayT;
                                if (todayT.equals(sortedSetList.get(k))) { // 공휴일과 같은게 있다면
                                    localToday = localToday.plusDays(1); // 1일증가
                                    while(localToday.getDayOfWeek().getValue() >= 5) {
                                        localToday = localToday.plusDays(1);
                                    }
                                }

                            }

                            ////////////////
                            year = String.valueOf(localToday.getYear());
                            month = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                            day = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());

                            today = year + "-" + month + "-" + day;

                        }
                        Member member = newMemberList.get(i);
                        MemberCalendarDto memberCalendarDto = new MemberCalendarDto();
                        MemberCalendar memberCalendar = new MemberCalendar();

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        // 만약 today 날짜에 같은 팀이 있다면?
                        // 지금i번쨰와 i+1번쨰를 바꿔준다.
                        // 허나 i번째가 끝번호라면 ( i == size-1 ) 이라면
                        // 진행하지 않는다.
                        boolean duplicated = false;
                        List<MemberCalendar> todayMemberCalendarList = memberCalendarRepository.findAllByStart(today);
                        if (todayMemberCalendarList.size() != 0) {

                            for (int j = 0; j < todayMemberCalendarList.size(); j++) { // 오늘날짜로 찾은 MemberCalendar 중에서
                                Long longId = todayMemberCalendarList.get(j).getMember().getId(); // 그 Membercaslendar에 해당하는 MemberId를 찾고
                                Member memberTemp = memberRepository.findById(longId).orElse(null); // member를 찾는다.
                                // memberTemp.getTeam(); // 그 member에 해당하는 팀이름
                                // newMemberList.get(i).getTeam() 과 그 2개를 비교해서 맞다면 true

                                if (newMemberList.get(i).getTeam().equals(memberTemp.getTeam())) {
                                    duplicated = true;
                                }
                            }

                            if (duplicated == true) {

                                if (i != size-1) {
                                    Member tempI = newMemberList.get(i);
                                    Member tempII = newMemberList.get(i+1);
                                    newMemberList.set(i+1,tempI);
                                    newMemberList.set(i,tempII);
                                }
                            }

                        }
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        member = newMemberList.get(i);
                        memberCalendar.setTitle(member.getName());
                        memberCalendar.setStart(today);
                        memberCalendar.setMember(member);
                        memberCalendarRepository.save(memberCalendar);
                        memberCalendarDto.setId(memberCalendar.getId());
                        memberCalendarDto.setTitle(memberCalendar.getTitle());
                        memberCalendarDto.setStart(memberCalendar.getStart());
                        memberCalendarDto.setMemberId(memberCalendar.getMember().getId());

                        memberCalendarDtoList.add(memberCalendarDto);
                    }
                } else { // 나머지 1인 일반적인 경우 -> 마지막 그룹을 3명, 3명, 3명 으로 묶는다.
                    for (int i = 0; i < size;i++) {
                        if ((i == size-6) || (i == size-3)) { /////////////////////////////////////////////////////////////수정요///////
                            localToday = localToday.plusDays(1);
                            //////////////// 플러스 1일 했는데 -> 금토일공휴일이면 제끼기

                            // 금토일 조건부터 빼주고, 공휴일 조건도 넘겨준날짜가 진짜날짜
                            while(localToday.getDayOfWeek().getValue() >= 5) {
                                localToday = localToday.plusDays(1);
                            }

                            // todayT와 stringSet (공휴일) 이 같은게 있다면 하루씩 증가시켜주기
                            for (int k = 0; k < sortedSetList.size(); k++) {
                                String yearT = String.valueOf(localToday.getYear());
                                String monthT = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                                String dayT = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());
                                String todayT = yearT + monthT + dayT;
                                if (todayT.equals(sortedSetList.get(k))) { // 공휴일과 같은게 있다면
                                    localToday = localToday.plusDays(1); // 1일증가
                                    while(localToday.getDayOfWeek().getValue() >= 5) {
                                        localToday = localToday.plusDays(1);
                                    }
                                }

                            }

                            ////////////////
                            year = String.valueOf(localToday.getYear());
                            month = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                            day = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());

                            today = year + "-" + month + "-" + day;

                        } else if (i > size-6) {

                        } else if ((i != 0) && (i % 4) == 0) {
                            localToday = localToday.plusDays(1);
                            //////////////// 플러스 1일 했는데 -> 금토일공휴일이면 제끼기

                            // 금토일 조건부터 빼주고, 공휴일 조건도 넘겨준날짜가 진짜날짜
                            while(localToday.getDayOfWeek().getValue() >= 5) {
                                localToday = localToday.plusDays(1);
                            }

                            // todayT와 stringSet (공휴일) 이 같은게 있다면 하루씩 증가시켜주기
                            for (int k = 0; k < sortedSetList.size(); k++) {
                                String yearT = String.valueOf(localToday.getYear());
                                String monthT = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                                String dayT = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());
                                String todayT = yearT + monthT + dayT;
                                if (todayT.equals(sortedSetList.get(k))) { // 공휴일과 같은게 있다면
                                    localToday = localToday.plusDays(1); // 1일증가
                                    while(localToday.getDayOfWeek().getValue() >= 5) {
                                        localToday = localToday.plusDays(1);
                                    }
                                }

                            }

                            ////////////////
                            year = String.valueOf(localToday.getYear());
                            month = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                            day = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());

                            today = year + "-" + month + "-" + day;

                        }
                        Member member = newMemberList.get(i);
                        MemberCalendarDto memberCalendarDto = new MemberCalendarDto();
                        MemberCalendar memberCalendar = new MemberCalendar();
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        // 만약 today 날짜에 같은 팀이 있다면?
                        // 지금i번쨰와 i+1번쨰를 바꿔준다.
                        // 허나 i번째가 끝번호라면 ( i == size-1 ) 이라면
                        // 진행하지 않는다.
                        boolean duplicated = false;
                        List<MemberCalendar> todayMemberCalendarList = memberCalendarRepository.findAllByStart(today);
                        if (todayMemberCalendarList.size() != 0) {

                                for (int j = 0; j < todayMemberCalendarList.size(); j++) { // 오늘날짜로 찾은 MemberCalendar 중에서
                                    Long longId = todayMemberCalendarList.get(j).getMember().getId(); // 그 Membercaslendar에 해당하는 MemberId를 찾고
                                    Member memberTemp = memberRepository.findById(longId).orElse(null); // member를 찾는다.
                                    // memberTemp.getTeam(); // 그 member에 해당하는 팀이름
                                    // newMemberList.get(i).getTeam() 과 그 2개를 비교해서 맞다면 true

                                    if (newMemberList.get(i).getTeam().equals(memberTemp.getTeam())) {
                                        duplicated = true;
                                    }
                                }

                            if (duplicated == true) {

                                if (i != size-1) {
                                    Member tempI = newMemberList.get(i);
                                    Member tempII = newMemberList.get(i+1);
                                    newMemberList.set(i+1,tempI);
                                    newMemberList.set(i,tempII);
                                }
                            }

                        }
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        member = newMemberList.get(i);
                        memberCalendar.setTitle(member.getName());
                        memberCalendar.setStart(today);
                        memberCalendar.setMember(member);
                        memberCalendarRepository.save(memberCalendar);
                        memberCalendarDto.setId(memberCalendar.getId());
                        memberCalendarDto.setTitle(memberCalendar.getTitle());
                        memberCalendarDto.setStart(memberCalendar.getStart());
                        memberCalendarDto.setMemberId(memberCalendar.getMember().getId());

                        memberCalendarDtoList.add(memberCalendarDto);
                    }
                }
                System.out.println("수정된 list" + newMemberList);
            } else if (remainder==2) { // 만약에 나머지가 2명이남는다면?
                if (quotient==0) { // -> 근데 몫이 0이라면? (총인원=2) : 2명 그대로진행
                    for (int i = 0; i < size;i++) {
                        Member member = newMemberList.get(i);
                        MemberCalendarDto memberCalendarDto = new MemberCalendarDto();
                        MemberCalendar memberCalendar = new MemberCalendar();

                        memberCalendar.setTitle(member.getName());
                        memberCalendar.setStart(today);
                        memberCalendar.setMember(member);
                        memberCalendarRepository.save(memberCalendar);
                        memberCalendarDto.setId(memberCalendar.getId());
                        memberCalendarDto.setTitle(memberCalendar.getTitle());
                        memberCalendarDto.setStart(memberCalendar.getStart());
                        memberCalendarDto.setMemberId(memberCalendar.getMember().getId());

                        memberCalendarDtoList.add(memberCalendarDto);
                    }
                } else if (quotient==1) { // -> 근데 몫이 1이라면? (총인원=6) : 3명, 3명으로 묶는다.
                    for (int i = 0; i < size;i++) {
                        if (i == 3) {
                            localToday = localToday.plusDays(1);
                            //////////////// 플러스 1일 했는데 -> 금토일공휴일이면 제끼기

                            // 금토일 조건부터 빼주고, 공휴일 조건도 넘겨준날짜가 진짜날짜
                            while(localToday.getDayOfWeek().getValue() >= 5) {
                                localToday = localToday.plusDays(1);
                            }

                            // todayT와 stringSet (공휴일) 이 같은게 있다면 하루씩 증가시켜주기
                            for (int k = 0; k < sortedSetList.size(); k++) {
                                String yearT = String.valueOf(localToday.getYear());
                                String monthT = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                                String dayT = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());
                                String todayT = yearT + monthT + dayT;
                                if (todayT.equals(sortedSetList.get(k))) { // 공휴일과 같은게 있다면
                                    localToday = localToday.plusDays(1); // 1일증가
                                    while(localToday.getDayOfWeek().getValue() >= 5) {
                                        localToday = localToday.plusDays(1);
                                    }
                                }

                            }

                            ////////////////
                            year = String.valueOf(localToday.getYear());
                            month = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                            day = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());

                            today = year + "-" + month + "-" + day;

                        }
                        Member member = newMemberList.get(i);
                        MemberCalendarDto memberCalendarDto = new MemberCalendarDto();
                        MemberCalendar memberCalendar = new MemberCalendar();

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        // 만약 today 날짜에 같은 팀이 있다면?
                        // 지금i번쨰와 i+1번쨰를 바꿔준다.
                        // 허나 i번째가 끝번호라면 ( i == size-1 ) 이라면
                        // 진행하지 않는다.
                        boolean duplicated = false;
                        List<MemberCalendar> todayMemberCalendarList = memberCalendarRepository.findAllByStart(today);
                        if (todayMemberCalendarList.size() != 0) {

                            for (int j = 0; j < todayMemberCalendarList.size(); j++) { // 오늘날짜로 찾은 MemberCalendar 중에서
                                Long longId = todayMemberCalendarList.get(j).getMember().getId(); // 그 Membercaslendar에 해당하는 MemberId를 찾고
                                Member memberTemp = memberRepository.findById(longId).orElse(null); // member를 찾는다.
                                // memberTemp.getTeam(); // 그 member에 해당하는 팀이름
                                // newMemberList.get(i).getTeam() 과 그 2개를 비교해서 맞다면 true

                                if (newMemberList.get(i).getTeam().equals(memberTemp.getTeam())) {
                                    duplicated = true;
                                }
                            }

                            if (duplicated == true) {
// 잘 바뀌는지?
                                if (i != size-1) {
                                    Member tempI = newMemberList.get(i);
                                    Member tempII = newMemberList.get(i+1);
                                    newMemberList.set(i+1,tempI);
                                    newMemberList.set(i,tempII);
                                }
                            }

                        }
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        member = newMemberList.get(i);
                        memberCalendar.setTitle(member.getName());
                        memberCalendar.setStart(today);
                        memberCalendar.setMember(member);
                        memberCalendarRepository.save(memberCalendar);
                        memberCalendarDto.setId(memberCalendar.getId());
                        memberCalendarDto.setTitle(memberCalendar.getTitle());
                        memberCalendarDto.setStart(memberCalendar.getStart());
                        memberCalendarDto.setMemberId(memberCalendar.getMember().getId());

                        memberCalendarDtoList.add(memberCalendarDto);
                    }
                } else { // 나머지 2인 일반적인 경우 -> 마지막 그룹을 3명, 3명 으로 묶는다. //////////////////
                    for (int i = 0; i < size;i++) {
                        if ((i == size-3) || (i == size-6)) {
                            localToday = localToday.plusDays(1);
                            //////////////// 플러스 1일 했는데 -> 금토일공휴일이면 제끼기

                            // 금토일 조건부터 빼주고, 공휴일 조건도 넘겨준날짜가 진짜날짜
                            while(localToday.getDayOfWeek().getValue() >= 5) {
                                localToday = localToday.plusDays(1);
                            }

                            // todayT와 stringSet (공휴일) 이 같은게 있다면 하루씩 증가시켜주기
                            for (int k = 0; k < sortedSetList.size(); k++) {
                                String yearT = String.valueOf(localToday.getYear());
                                String monthT = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                                String dayT = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());
                                String todayT = yearT + monthT + dayT;
                                if (todayT.equals(sortedSetList.get(k))) { // 공휴일과 같은게 있다면
                                    localToday = localToday.plusDays(1); // 1일증가
                                    while(localToday.getDayOfWeek().getValue() >= 5) {
                                        localToday = localToday.plusDays(1);
                                    }
                                }

                            }

                            ////////////////
                            year = String.valueOf(localToday.getYear());
                            month = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                            day = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());

                            today = year + "-" + month + "-" + day;

                        } else if (i > size-3) {

                        } else if ((i != 0) && (i % 4) == 0) {
                            localToday = localToday.plusDays(1);
                            //////////////// 플러스 1일 했는데 -> 금토일공휴일이면 제끼기

                            // 금토일 조건부터 빼주고, 공휴일 조건도 넘겨준날짜가 진짜날짜
                            while(localToday.getDayOfWeek().getValue() >= 5) {
                                localToday = localToday.plusDays(1);
                            }

                            // todayT와 stringSet (공휴일) 이 같은게 있다면 하루씩 증가시켜주기
                            for (int k = 0; k < sortedSetList.size(); k++) {
                                String yearT = String.valueOf(localToday.getYear());
                                String monthT = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                                String dayT = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());
                                String todayT = yearT + monthT + dayT;
                                if (todayT.equals(sortedSetList.get(k))) { // 공휴일과 같은게 있다면
                                    localToday = localToday.plusDays(1); // 1일증가
                                    while(localToday.getDayOfWeek().getValue() >= 5) {
                                        localToday = localToday.plusDays(1);
                                    }
                                }

                            }

                            ////////////////
                            year = String.valueOf(localToday.getYear());
                            month = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                            day = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());

                            today = year + "-" + month + "-" + day;

                        }
                        Member member = newMemberList.get(i);
                        MemberCalendarDto memberCalendarDto = new MemberCalendarDto();
                        MemberCalendar memberCalendar = new MemberCalendar();

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        // 만약 today 날짜에 같은 팀이 있다면?
                        // 지금i번쨰와 i+1번쨰를 바꿔준다.
                        // 허나 i번째가 끝번호라면 ( i == size-1 ) 이라면
                        // 진행하지 않는다.
                        boolean duplicated = false;
                        List<MemberCalendar> todayMemberCalendarList = memberCalendarRepository.findAllByStart(today);
                        if (todayMemberCalendarList.size() != 0) {

                            for (int j = 0; j < todayMemberCalendarList.size(); j++) { // 오늘날짜로 찾은 MemberCalendar 중에서
                                Long longId = todayMemberCalendarList.get(j).getMember().getId(); // 그 Membercaslendar에 해당하는 MemberId를 찾고
                                Member memberTemp = memberRepository.findById(longId).orElse(null); // member를 찾는다.
                                // memberTemp.getTeam(); // 그 member에 해당하는 팀이름
                                // newMemberList.get(i).getTeam() 과 그 2개를 비교해서 맞다면 true

                                if (newMemberList.get(i).getTeam().equals(memberTemp.getTeam())) {
                                    duplicated = true;
                                }
                            }

                            if (duplicated == true) {

                                if (i != size-1) {
                                    Member tempI = newMemberList.get(i);
                                    Member tempII = newMemberList.get(i+1);
                                    newMemberList.set(i+1,tempI);
                                    newMemberList.set(i,tempII);
                                }
                            }

                        }
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        member = newMemberList.get(i);
                        memberCalendar.setTitle(member.getName());
                        memberCalendar.setStart(today);
                        memberCalendar.setMember(member);
                        memberCalendarRepository.save(memberCalendar);
                        memberCalendarDto.setId(memberCalendar.getId());
                        memberCalendarDto.setTitle(memberCalendar.getTitle());
                        memberCalendarDto.setStart(memberCalendar.getStart());
                        memberCalendarDto.setMemberId(memberCalendar.getMember().getId());

                        memberCalendarDtoList.add(memberCalendarDto);
                    }
                }
            } else if (remainder==3) { // 나머지 3인 일반적인 -> 마지막을 3명으로 묶는다.
                if (quotient==0) { // -> 근데 몫이 0이라면? (총인원=2) : 2명 그대로진행
                    for (int i = 0; i < size;i++) {
                        Member member = newMemberList.get(i);
                        MemberCalendarDto memberCalendarDto = new MemberCalendarDto();
                        MemberCalendar memberCalendar = new MemberCalendar();

                        memberCalendar.setTitle(member.getName());
                        memberCalendar.setStart(today);
                        memberCalendar.setMember(member);
                        memberCalendarRepository.save(memberCalendar);
                        memberCalendarDto.setId(memberCalendar.getId());
                        memberCalendarDto.setTitle(memberCalendar.getTitle());
                        memberCalendarDto.setStart(memberCalendar.getStart());
                        memberCalendarDto.setMemberId(memberCalendar.getMember().getId());

                        memberCalendarDtoList.add(memberCalendarDto);
                    }
                } else { // 나머지 2인 일반적인 경우 -> 마지막 그룹을 3명, 3명 으로 묶는다.
                    for (int i = 0; i < size;i++) {
                        if (i == size-3) {
                            localToday = localToday.plusDays(1);
                            //////////////// 플러스 1일 했는데 -> 금토일공휴일이면 제끼기

                            // 금토일 조건부터 빼주고, 공휴일 조건도 넘겨준날짜가 진짜날짜
                            while(localToday.getDayOfWeek().getValue() >= 5) {
                                localToday = localToday.plusDays(1);
                            }

                            // todayT와 stringSet (공휴일) 이 같은게 있다면 하루씩 증가시켜주기
                            for (int k = 0; k < sortedSetList.size(); k++) {
                                String yearT = String.valueOf(localToday.getYear());
                                String monthT = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                                String dayT = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());
                                String todayT = yearT + monthT + dayT;
                                if (todayT.equals(sortedSetList.get(k))) { // 공휴일과 같은게 있다면
                                    localToday = localToday.plusDays(1); // 1일증가
                                    while(localToday.getDayOfWeek().getValue() >= 5) {
                                        localToday = localToday.plusDays(1);
                                    }
                                }

                            }

                            ////////////////
                            year = String.valueOf(localToday.getYear());
                            month = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                            day = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());

                            today = year + "-" + month + "-" + day;

                        } else if (i > size-3) {

                        } else if ((i != 0) && (i % 4) == 0) {
                            localToday = localToday.plusDays(1);
                            //////////////// 플러스 1일 했는데 -> 금토일공휴일이면 제끼기

                            // 금토일 조건부터 빼주고, 공휴일 조건도 넘겨준날짜가 진짜날짜
                            while(localToday.getDayOfWeek().getValue() >= 5) {
                                localToday = localToday.plusDays(1);
                            }

                            // todayT와 stringSet (공휴일) 이 같은게 있다면 하루씩 증가시켜주기
                            for (int k = 0; k < sortedSetList.size(); k++) {
                                String yearT = String.valueOf(localToday.getYear());
                                String monthT = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                                String dayT = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());
                                String todayT = yearT + monthT + dayT;
                                if (todayT.equals(sortedSetList.get(k))) { // 공휴일과 같은게 있다면
                                    localToday = localToday.plusDays(1); // 1일증가
                                    while(localToday.getDayOfWeek().getValue() >= 5) {
                                        localToday = localToday.plusDays(1);
                                    }
                                }

                            }

                            ////////////////
                            year = String.valueOf(localToday.getYear());
                            month = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                            day = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());

                            today = year + "-" + month + "-" + day;

                        }

                        Member member = newMemberList.get(i);
                        MemberCalendarDto memberCalendarDto = new MemberCalendarDto();
                        MemberCalendar memberCalendar = new MemberCalendar();

                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                        // 만약 today 날짜에 같은 팀이 있다면?
                        // 지금i번쨰와 i+1번쨰를 바꿔준다.
                        // 허나 i번째가 끝번호라면 ( i == size-1 ) 이라면
                        // 진행하지 않는다.
                        boolean duplicated = false;
                        List<MemberCalendar> todayMemberCalendarList = memberCalendarRepository.findAllByStart(today);
                        if (todayMemberCalendarList.size() != 0) {

                            for (int j = 0; j < todayMemberCalendarList.size(); j++) { // 오늘날짜로 찾은 MemberCalendar 중에서
                                Long longId = todayMemberCalendarList.get(j).getMember().getId(); // 그 Membercaslendar에 해당하는 MemberId를 찾고
                                Member memberTemp = memberRepository.findById(longId).orElse(null); // member를 찾는다.
                                // memberTemp.getTeam(); // 그 member에 해당하는 팀이름
                                // newMemberList.get(i).getTeam() 과 그 2개를 비교해서 맞다면 true

                                if (newMemberList.get(i).getTeam().equals(memberTemp.getTeam())) {
                                    duplicated = true;
                                }
                            }

                            if (duplicated == true) {

                                if (i != size-1) {
                                    Member tempI = newMemberList.get(i);
                                    Member tempII = newMemberList.get(i+1);
                                    newMemberList.set(i+1,tempI);
                                    newMemberList.set(i,tempII);
                                }
                            }

                        }
                        /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        member = newMemberList.get(i);
                        memberCalendar.setTitle(member.getName());
                        memberCalendar.setStart(today);
                        memberCalendar.setMember(member);
                        memberCalendarRepository.save(memberCalendar);
                        memberCalendarDto.setId(memberCalendar.getId());
                        memberCalendarDto.setTitle(memberCalendar.getTitle());
                        memberCalendarDto.setStart(memberCalendar.getStart());
                        memberCalendarDto.setMemberId(memberCalendar.getMember().getId());

                        memberCalendarDtoList.add(memberCalendarDto);
                    }
                }

            } else if (remainder==0) {// 나머지 4인경우

                for (int i = 0; i < size;i++) {

                    if ((i != 0) && (i % 4) == 0) {
                        localToday = localToday.plusDays(1);
                        //////////////// 플러스 1일 했는데 -> 금토일공휴일이면 제끼기

                        // 금토일 조건부터 빼주고, 공휴일 조건도 넘겨준날짜가 진짜날짜
                        while(localToday.getDayOfWeek().getValue() >= 5) {
                            localToday = localToday.plusDays(1);
                        }

                        // todayT와 stringSet (공휴일) 이 같은게 있다면 하루씩 증가시켜주기
                        for (int k = 0; k < sortedSetList.size(); k++) {
                            String yearT = String.valueOf(localToday.getYear());
                            String monthT = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                            String dayT = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());
                            String todayT = yearT + monthT + dayT;
                            if (todayT.equals(sortedSetList.get(k))) { // 공휴일과 같은게 있다면
                                localToday = localToday.plusDays(1); // 1일증가
                                while(localToday.getDayOfWeek().getValue() >= 5) {
                                    localToday = localToday.plusDays(1);
                                }
                            }

                        }

                        ////////////////
                        year = String.valueOf(localToday.getYear());
                        month = String.valueOf((localToday.getMonthValue() < 10) ? "0" + localToday.getMonthValue() : localToday.getMonthValue());
                        day = String.valueOf((localToday.getDayOfMonth() < 10) ? "0" + localToday.getDayOfMonth() : localToday.getDayOfMonth());

                        today = year + "-" + month + "-" + day;

                    }

                    Member member = newMemberList.get(i);
                    MemberCalendarDto memberCalendarDto = new MemberCalendarDto();
                    MemberCalendar memberCalendar = new MemberCalendar();

                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    // 만약 today 날짜에 같은 팀이 있다면?
                    // 지금i번쨰와 i+1번쨰를 바꿔준다.
                    // 허나 i번째가 끝번호라면 ( i == size-1 ) 이라면
                    // 진행하지 않는다.
                    boolean duplicated = false;
                    List<MemberCalendar> todayMemberCalendarList = memberCalendarRepository.findAllByStart(today);
                    if (todayMemberCalendarList.size() != 0) {

                        for (int j = 0; j < todayMemberCalendarList.size(); j++) { // 오늘날짜로 찾은 MemberCalendar 중에서
                            Long longId = todayMemberCalendarList.get(j).getMember().getId(); // 그 Membercaslendar에 해당하는 MemberId를 찾고
                            Member memberTemp = memberRepository.findById(longId).orElse(null); // member를 찾는다.
                            // memberTemp.getTeam(); // 그 member에 해당하는 팀이름
                            // newMemberList.get(i).getTeam() 과 그 2개를 비교해서 맞다면 true

                            if (newMemberList.get(i).getTeam().equals(memberTemp.getTeam())) {
                                duplicated = true;
                            }
                        }

                        if (duplicated == true) {

                            if (i != size-1) {
                                Member tempI = newMemberList.get(i);
                                Member tempII = newMemberList.get(i+1);
                                newMemberList.set(i+1,tempI);
                                newMemberList.set(i,tempII);
                            }
                        }

                    }
                    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                    member = newMemberList.get(i);
                    memberCalendar.setTitle(member.getName());
                    memberCalendar.setStart(today);
                    memberCalendar.setMember(member);
                    memberCalendarRepository.save(memberCalendar);
                    memberCalendarDto.setId(memberCalendar.getId());
                    memberCalendarDto.setTitle(memberCalendar.getTitle());
                    memberCalendarDto.setStart(memberCalendar.getStart());
                    memberCalendarDto.setMemberId(memberCalendar.getMember().getId());

                    memberCalendarDtoList.add(memberCalendarDto);
                }

            }






        }

        return memberCalendarDtoList;
    }

    @Override
    public List<MemberCalendarDto> findMemberCalendarList() {
        List<MemberCalendarDto> memberCalendarDtoList = new ArrayList<>();
        List<MemberCalendar> memberCalendarList = memberCalendarRepository.findAll();

        if (!(memberCalendarList == null || memberCalendarList.isEmpty())) {
            for (int i = 0; i < memberCalendarList.size(); i++) {
                MemberCalendar memberCalendar = memberCalendarList.get(i);
                MemberCalendarDto memberCalendarDto = new MemberCalendarDto();
                memberCalendarDto.setId(memberCalendar.getId());
                memberCalendarDto.setTitle(memberCalendar.getTitle());
                memberCalendarDto.setStart(memberCalendar.getStart());
                memberCalendarDto.setMemberId(memberCalendar.getMember().getId());
                memberCalendarDtoList.add(memberCalendarDto);
            }
        }

        return memberCalendarDtoList;
    }

    @Override
    public void deletefindMemberCalendarList() {
        memberCalendarRepository.deleteAll();
    }
}
