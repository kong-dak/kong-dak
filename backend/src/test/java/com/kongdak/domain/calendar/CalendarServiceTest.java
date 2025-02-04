package com.kongdak.domain.calendar;

import com.kongdak.controller.dto.request.ScheduleCreateRequest;
import com.kongdak.controller.dto.request.ScheduleUpdateRequest;
import com.kongdak.controller.dto.response.CalendarResponse;
import com.kongdak.controller.dto.response.MonthlyScheduleResponse;
import com.kongdak.controller.dto.response.ScheduleResponse;
import com.kongdak.domain.couple.Couple;
import com.kongdak.domain.couple.CoupleService;
import com.kongdak.domain.member.Member;
import com.kongdak.domain.member.MemberService;
import com.kongdak.domain.member.OAuthProvider;
import com.kongdak.global.exception.BusinessException;
import com.kongdak.global.exception.ErrorCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CalendarServiceTest {
    @InjectMocks
    private CalendarService calendarService;

    @Mock private CalendarRepository calendarRepository;
    @Mock private ScheduleRepository scheduleRepository;
    @Mock private HolidayRepository holidayRepository;
    @Mock private MemberService memberService;
    @Mock private CoupleService coupleService;

    private Member member;
    private Member partner;
    private Couple couple;
    private Calendar calendar;

    @BeforeEach
    void setUp() {
        // 테스트에서 공통으로 사용할 객체들 초기화
        member = Member.builder()
                .email("test@test.com")
                .nickname("테스트 유저")
                .oAuthProvider(OAuthProvider.KAKAO)
                .build();
        ReflectionTestUtils.setField(member, "id", 1L);

        partner = Member.builder()
                .email("partner@test.com")
                .nickname("파트너")
                .oAuthProvider(OAuthProvider.KAKAO)
                .build();
        ReflectionTestUtils.setField(partner, "id", 2L);

        couple = Couple.builder()
                .anniversaryDate(LocalDateTime.now().minusMonths(1))
                .build();
        ReflectionTestUtils.setField(couple, "id", 1L);


        member.setCouple(couple);
        partner.setCouple(couple);

        calendar = Calendar.builder()
                .couple(couple)
                .build();
        ReflectionTestUtils.setField(calendar, "id", 1L);
    }

    @Nested
    @DisplayName("캘린더 생성 테스트")
    class CreateCalendarTest {
        @Test
        @DisplayName("캘린더 생성 성공")
        void createCalendar_Success() {
            // given
            when(calendarRepository.existsByCouple(couple)).thenReturn(false);
            when(calendarRepository.save(any(Calendar.class))).thenReturn(calendar);

            // when
            CalendarResponse response = calendarService.createCalendar(couple);

            // then
            assertThat(response).isNotNull();
            assertThat(response.coupleId()).isEqualTo(couple.getId());
            verify(calendarRepository).existsByCouple(couple);
            verify(calendarRepository).save(any(Calendar.class));
        }

        @Test
        @DisplayName("이미 존재하는 커플의 캘린더 생성 시 실패")
        void createCalendar_Duplicate_Fail() {
            // given
            when(calendarRepository.existsByCouple(couple)).thenReturn(true);

            // when & then
            assertThatThrownBy(() -> calendarService.createCalendar(couple))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.DUPLICATE_CALENDAR);

            verify(calendarRepository).existsByCouple(couple);
            verify(calendarRepository, never()).save(any(Calendar.class));
        }
    }

    @Nested
    @DisplayName("일정 생성 테스트")
    class CreateScheduleTest {
        @Test
        @DisplayName("일정 생성 성공")
        void createSchedule_Success() {
            // given
            ScheduleCreateRequest request = createScheduleRequest(ScheduleCategory.PERSONAL);
            Schedule schedule = createSchedule(calendar, request);

            when(memberService.getCurrentMember()).thenReturn(member);
            when(calendarRepository.findByCouple(couple)).thenReturn(Optional.of(calendar));
            when(scheduleRepository.save(any(Schedule.class))).thenReturn(schedule);

            // when
            ScheduleResponse response = calendarService.createSchedule(request);

            // then
            assertThat(response)
                    .isNotNull()
                    .satisfies(r -> {
                        assertThat(r.title()).isEqualTo(request.title());
                        assertThat(r.category()).isEqualTo(request.category());
                        assertThat(r.startTime()).isEqualTo(request.startTime());
                        assertThat(r.endTime()).isEqualTo(request.endTime());
                    });

            verify(memberService, atLeastOnce()).getCurrentMember();
            verify(calendarRepository).findByCouple(couple);
            verify(scheduleRepository).save(any(Schedule.class));
        }

        @Test
        @DisplayName("캘린더가 존재하지 않는 경우 실패")
        void createSchedule_CalendarNotFound_Fail() {
            // given
            ScheduleCreateRequest request = createScheduleRequest(ScheduleCategory.PERSONAL);

            when(memberService.getCurrentMember()).thenReturn(member);
            when(calendarRepository.findByCouple(couple)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> calendarService.createSchedule(request))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CALENDAR_NOT_FOUND);

            verify(scheduleRepository, never()).save(any(Schedule.class));
        }

        @Test
        @DisplayName("종료 시간이 시작 시간보다 이전인 경우 실패")
        void createSchedule_InvalidTime_Fail() {
            // given
            ScheduleCreateRequest request = new ScheduleCreateRequest(
                    "테스트 일정",
                    LocalDateTime.now().plusHours(1),
                    LocalDateTime.now(),
                    "설명",
                    ScheduleCategory.PERSONAL,
                    "😊"
            );

            // when & then
            assertThatThrownBy(() -> calendarService.createSchedule(request))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_SCHEDULE_TIME);

            verify(scheduleRepository, never()).save(any(Schedule.class));
        }
    }

    @Nested
    @DisplayName("일정 수정 테스트")
    class UpdateScheduleTest {
        @Test
        @DisplayName("개인 일정 수정 성공")
        void updatePersonalSchedule_Success() {
            // given
            Schedule originalSchedule = createSchedule(calendar,
                    createScheduleRequest(ScheduleCategory.PERSONAL));
            ScheduleUpdateRequest updateRequest = new ScheduleUpdateRequest(
                    "수정된 일정",
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(1).plusHours(2),
                    "수정된 설명",
                    ScheduleCategory.PERSONAL,
                    "🎉"
            );

            when(memberService.getCurrentMember()).thenReturn(member);
            when(calendarRepository.findByCouple(couple)).thenReturn(Optional.of(calendar));  // 추가
            when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(originalSchedule));

            // when
            ScheduleResponse response = calendarService.updateSchedule(1L, updateRequest);

            // then
            assertThat(response).isNotNull()
                    .satisfies(r -> {
                        assertThat(r.title()).isEqualTo(updateRequest.title());
                        assertThat(r.category()).isEqualTo(updateRequest.category());
                        assertThat(r.startTime()).isEqualTo(updateRequest.startTime());
                        assertThat(r.endTime()).isEqualTo(updateRequest.endTime());
                    });
        }

        @Test
        @DisplayName("공유 일정 수정 성공")
        void updateSharedSchedule_Success() {
            // given
            Schedule originalSchedule = createSchedule(calendar,
                    createScheduleRequest(ScheduleCategory.SHARED));
            ScheduleUpdateRequest updateRequest = new ScheduleUpdateRequest(
                    "수정된 공유 일정",
                    LocalDateTime.now().plusDays(1),
                    LocalDateTime.now().plusDays(1).plusHours(2),
                    "수정된 설명",
                    ScheduleCategory.SHARED,
                    "👫"
            );

            when(memberService.getCurrentMember()).thenReturn(partner);
            when(calendarRepository.findByCouple(couple)).thenReturn(Optional.of(calendar));
            when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(originalSchedule));
            when(coupleService.isCoupleMember(eq(partner), eq(couple.getId()))).thenReturn(true);

            // when
            ScheduleResponse response = calendarService.updateSchedule(1L, updateRequest);

            // then
            assertThat(response).isNotNull()
                    .satisfies(r -> {
                        assertThat(r.title()).isEqualTo(updateRequest.title());
                        assertThat(r.category()).isEqualTo(updateRequest.category());
                    });
        }

        @Test
        @DisplayName("다른 사람의 개인 일정 수정 시 실패")
        void updateOthersPrivateSchedule_Fail() {
            // given
            Schedule originalSchedule = createSchedule(calendar,
                    createScheduleRequest(ScheduleCategory.PRIVATE));
            ScheduleUpdateRequest updateRequest = new ScheduleUpdateRequest(
                    "수정 시도",
                    LocalDateTime.now(),
                    LocalDateTime.now().plusHours(1),
                    "설명",
                    ScheduleCategory.PRIVATE,
                    "😊"
            );

            when(memberService.getCurrentMember()).thenReturn(partner);
            when(calendarRepository.findByCouple(couple)).thenReturn(Optional.of(calendar));  // 추가
            when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(originalSchedule));

            // when & then
            assertThatThrownBy(() -> calendarService.updateSchedule(1L, updateRequest))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.SCHEDULE_ACCESS_DENIED);
        }
    }

    @Nested
    @DisplayName("일정 삭제 테스트")
    class DeleteScheduleTest {
        @Test
        @DisplayName("자신의 일정 삭제 성공")
        void deleteOwnSchedule_Success() {
            // given
            Schedule schedule = createSchedule(calendar,
                    createScheduleRequest(ScheduleCategory.PERSONAL));

            when(memberService.getCurrentMember()).thenReturn(member);
            when(calendarRepository.findByCouple(couple)).thenReturn(Optional.of(calendar));  // 추가
            when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

            // when
            calendarService.deleteSchedule(1L);

            // then
            verify(scheduleRepository).delete(schedule);
        }

        @Test
        @DisplayName("타인의 PRIVATE 일정 삭제 시 실패")
        void deleteOthersPrivateSchedule_Fail() {
            // given
            Schedule schedule = createSchedule(calendar,
                    createScheduleRequest(ScheduleCategory.PRIVATE));

            when(memberService.getCurrentMember()).thenReturn(partner);
            when(calendarRepository.findByCouple(couple)).thenReturn(Optional.of(calendar));  // 추가
            when(scheduleRepository.findById(anyLong())).thenReturn(Optional.of(schedule));

            // when & then
            assertThatThrownBy(() -> calendarService.deleteSchedule(1L))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.SCHEDULE_ACCESS_DENIED);
        }
    }

    @Nested
    @DisplayName("권한 검증 테스트")
    class AuthorizationTest {
        @Test
        @DisplayName("커플이 아닌 사용자의 일정 조회 시도 실패")
        void accessScheduleWithNonCoupleUser_Fail() {
            // given
            Member nonCoupleUser = Member.builder()
                    .email("non-couple@test.com")
                    .nickname("커플아님")
                    .oAuthProvider(OAuthProvider.KAKAO)
                    .build();
            ReflectionTestUtils.setField(nonCoupleUser, "id", 3L);

            when(memberService.getCurrentMember()).thenReturn(nonCoupleUser);
            when(calendarRepository.findByCouple(any())).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> calendarService.getMonthlySchedules(YearMonth.now()))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.CALENDAR_NOT_FOUND);
        }

        @Test
        @DisplayName("연결이 끊어진 커플의 일정 접근 실패")
        void accessScheduleWithDisconnectedCouple_Fail() {
            // given
            couple.disconnect();

            when(memberService.getCurrentMember()).thenReturn(member);
            when(calendarRepository.findByCouple(couple)).thenReturn(Optional.of(calendar));

            // when & then
            assertThatThrownBy(() -> calendarService.getMonthlySchedules(YearMonth.now()))
                    .isInstanceOf(BusinessException.class)
                    .hasFieldOrPropertyWithValue("errorCode", ErrorCode.COUPLE_ALREADY_DISCONNECTED);
        }
    }

    @Nested
    @DisplayName("일정 조회 테스트")
    class GetSchedulesTest {
        @Test
        @DisplayName("월별 일정 조회 성공")
        void getMonthlySchedules_Success() {
            // given
            YearMonth yearMonth = YearMonth.now();
            List<Schedule> schedules = List.of(
                    createSchedule(calendar, createScheduleRequest(ScheduleCategory.PERSONAL)),
                    createSchedule(calendar, createScheduleRequest(ScheduleCategory.SHARED))
            );

            when(memberService.getCurrentMember()).thenReturn(member);
            when(calendarRepository.findByCouple(couple)).thenReturn(Optional.of(calendar));
            when(scheduleRepository.findMonthlySchedules(
                    eq(calendar.getId()),
                    eq(yearMonth.getYear()),
                    eq(yearMonth.getMonthValue())
            )).thenReturn(schedules);

            // when
            MonthlyScheduleResponse responses = calendarService.getMonthlySchedules(yearMonth);

            // then
            assertThat(responses.schedules()).hasSize(2)
                    .extracting("category")
                    .containsExactlyInAnyOrder(
                            ScheduleCategory.PERSONAL,
                            ScheduleCategory.SHARED
                    );
        }

        @Test
        @DisplayName("일별 일정 조회 성공 (휴일 포함)")
        void getDailySchedulesWithHoliday_Success() {
            // given
            LocalDate date = LocalDate.now();
            List<Schedule> schedules = List.of(
                    createSchedule(calendar, createScheduleRequest(ScheduleCategory.PERSONAL))
            );
            Holiday holiday = Holiday.builder()
                    .name("테스트 공휴일")
                    .date(date)
                    .build();

            when(memberService.getCurrentMember()).thenReturn(member);
            when(calendarRepository.findByCouple(couple)).thenReturn(Optional.of(calendar));
            when(scheduleRepository.findDailySchedules(eq(calendar.getId()), eq(date)))
                    .thenReturn(schedules);
//            when(holidayRepository.existsByDate(date)).thenReturn(true);
//            when(holidayRepository.findByDate(date)).thenReturn(Optional.of(holiday));

            // when
            List<ScheduleResponse> responses = calendarService.getDailySchedules(date);

            // then
            assertThat(responses).hasSize(1);

            // TODO : Response에 Holiday 추가해야 함
//                    .extracting("isHoliday")
//                    .contains(false, true);
        }
    }
    private ScheduleCreateRequest createScheduleRequest(ScheduleCategory category) {
        LocalDateTime now = LocalDateTime.now();
        return new ScheduleCreateRequest(
                "테스트 일정",
                now,
                now.plusHours(1),
                "테스트 설명",
                category,
                "😊"
        );
    }

    private Schedule createSchedule(Calendar calendar, ScheduleCreateRequest request) {
        return Schedule.builder()
                .calendar(calendar)
                .creator(member)
                .title(request.title())
                .startTime(request.startTime())
                .endTime(request.endTime())
                .description(request.description())
                .category(request.category())
                .emoji(request.emoji())
                .isHoliday(false)
                .isAnniversary(false)
                .build();
    }
}

