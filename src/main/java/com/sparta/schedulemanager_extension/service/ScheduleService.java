package com.sparta.schedulemanager_extension.service;

import com.sparta.schedulemanager_extension.dto.schedule.ScheduleBaseRequestDto;
import com.sparta.schedulemanager_extension.dto.schedule.ScheduleCreateRequestDto;
import com.sparta.schedulemanager_extension.dto.schedule.SchedulePageResponseDto;
import com.sparta.schedulemanager_extension.dto.schedule.ScheduleResponseDto;
import com.sparta.schedulemanager_extension.entity.Schedule;
import com.sparta.schedulemanager_extension.repository.CommentRepository;
import com.sparta.schedulemanager_extension.repository.ScheduleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ScheduleService {

    final ScheduleRepository scheduleRepository;
    final CommentRepository commentRepository;

    public ScheduleService(ScheduleRepository scheduleRepository, CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
        this.scheduleRepository = scheduleRepository;
    }

    /** 스케쥴을 추가하는 함수
     *
     * @param scheduleCreateRequestDto RequestBody로 들어온 스케쥴 정보
     * @return 저장된 내용
     */
    public ScheduleResponseDto createSchedule(ScheduleCreateRequestDto scheduleCreateRequestDto) {
        Schedule schedule = scheduleCreateRequestDto.convertSchedule();
        Schedule saveSchedule = scheduleRepository.save(schedule);

        return new ScheduleResponseDto(saveSchedule);
    }

    /** 스케쥴을 조회하는 함수
     *
     * @param scheduleId 조회하고자 하는 스케쥴 아이디
     * @return 조회된 스케쥴 내용이 담긴 객체
     */
    public ScheduleResponseDto getSchedule(Integer scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId).orElseThrow(() ->
                new RuntimeException("Schedule not found"));

        return new ScheduleResponseDto(schedule);
    }

    /** 스케쥴들을 페이징하여 조회하는 함수
     *
     * @param pageIndex 페이징 할 인덱스
     * @param pageSize 페이징 사이즈
     * @return 조회 & 댓글 수까지 추가된 객체
     */
    public List<SchedulePageResponseDto> getPageSchedules(Integer pageIndex, Integer pageSize) {
        PageRequest pageRequest = PageRequest.of(pageIndex, pageSize, Sort.by("editDateTime").descending());
        List<SchedulePageResponseDto> schedules = scheduleRepository.findAll(pageRequest).stream()
                .map(SchedulePageResponseDto::new)
                .toList();

        return schedules;
    }

    /** 스케쥴을 갱신하는 함수
     *
     * @param scheduleId 변경하고자 하는 스케쥴 아이디
     * @param scheduleBaseRequestDto 변경하고자 하는 내용( 제목, 내용 )
     * @return 업데이트 된 일정 내용
     */
    public ScheduleResponseDto updateSchedule(Integer scheduleId, ScheduleBaseRequestDto scheduleBaseRequestDto) {
        Schedule foundSchedule = scheduleRepository.findById(scheduleId).orElseThrow(
                () -> new RuntimeException("Schedule not found")
        );
        foundSchedule.updateSchedule(scheduleBaseRequestDto.getScheduleTitle(), scheduleBaseRequestDto.getScheduleData());

        Schedule updatedSchedule = scheduleRepository.save(foundSchedule);
        return new ScheduleResponseDto(updatedSchedule);
    }

    /** 스케쥴을 삭제하는 함수
     *
     * @param scheduleId 삭제할 스케쥴 ID
     * @return 삭제된 스케쥴 ID
     */
    public Integer deleteSchedule(Integer scheduleId) {
        scheduleRepository.deleteById(scheduleId);
        return scheduleId;
    }
}
