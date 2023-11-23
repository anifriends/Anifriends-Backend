package com.clova.anifriends.domain.applicant.service;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.dto.FindApplicantsResponse;
import com.clova.anifriends.domain.applicant.dto.response.FindApplicantsApprovedResponse;
import com.clova.anifriends.domain.applicant.dto.response.FindApplyingVolunteersResponse;
import com.clova.anifriends.domain.applicant.exception.ApplicantConflictException;
import com.clova.anifriends.domain.applicant.repository.ApplicantRepository;
import com.clova.anifriends.domain.applicant.service.dto.UpdateApplicantAttendanceCommand;
import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.exception.RecruitmentNotFoundException;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.review.exception.ApplicantNotFoundException;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.exception.VolunteerNotFoundException;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.global.aspect.DataIntegrityHandler;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicantService {

    private final ApplicantRepository applicantRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final VolunteerRepository volunteerRepository;

    @Transactional
    @DataIntegrityHandler(message = "이미 신청한 봉사입니다.", exceptionClass = ApplicantConflictException.class)
    public void registerApplicant(Long recruitmentId, Long volunteerId) {
        Recruitment recruitmentPessimistic = getRecruitmentPessimistic(recruitmentId);
        Volunteer volunteer = getVolunteer(volunteerId);
        Applicant applicant = new Applicant(recruitmentPessimistic, volunteer);
        applicantRepository.save(applicant);
    }

    private Recruitment getRecruitmentPessimistic(Long recruitmentId) {
        return recruitmentRepository.findByIdPessimistic(recruitmentId)
            .orElseThrow(() -> new RecruitmentNotFoundException("존재하지 않는 봉사 모집글입니다."));
    }

    @Transactional(readOnly = true)
    public FindApplyingVolunteersResponse findApplyingVolunteers(
        Long volunteerId
    ) {
        Volunteer foundVolunteer = getVolunteer(volunteerId);

        List<Applicant> applyingVolunteers = applicantRepository.findApplyingVolunteers(
            foundVolunteer);

        return FindApplyingVolunteersResponse.from(applyingVolunteers);
    }

    @Transactional(readOnly = true)
    public FindApplicantsApprovedResponse findApplicantsApproved(Long shelterId,
        Long recruitmentId) {
        List<Applicant> applicantsApproved = applicantRepository
            .findApprovedByRecruitmentIdAndShelterId(recruitmentId, shelterId);
        return FindApplicantsApprovedResponse.from(applicantsApproved);
    }

    @Transactional(readOnly = true)
    public FindApplicantsResponse findApplicants(Long shelterId, Long recruitmentId) {
        Recruitment recruitment = getRecruitment(recruitmentId);
        List<Applicant> applicants = applicantRepository
            .findByRecruitmentIdAndShelterId(recruitmentId, shelterId);
        return FindApplicantsResponse.from(applicants, recruitment);
    }

    @Transactional
    public void updateApplicantAttendance(Long shelterId, Long recruitmentId,
        List<UpdateApplicantAttendanceCommand> applicantsCommand) {
        updateToAttendance(shelterId, recruitmentId, applicantsCommand);
        updateToNoShow(shelterId, recruitmentId, applicantsCommand);
    }

    @Transactional
    public void updateApplicantStatus(Long applicantId, Long recruitmentId, Long shelterId,
        Boolean isApproved) {
        Applicant applicant = getApplicant(applicantId, recruitmentId, shelterId);
        applicant.updateApplicantStatus(isApproved);
    }

    private void updateToNoShow(Long shelterId, Long recruitmentId,
        List<UpdateApplicantAttendanceCommand> applicantsCommand) {
        List<Long> noShowIds = applicantsCommand.stream()
            .filter(applicant -> !applicant.isAttended())
            .map(UpdateApplicantAttendanceCommand::applicantId)
            .toList();

        applicantRepository.updateBulkAttendance(shelterId, recruitmentId, noShowIds,
            ApplicantStatus.NO_SHOW);
    }

    private void updateToAttendance(Long shelterId, Long recruitmentId,
        List<UpdateApplicantAttendanceCommand> applicantsCommand) {
        List<Long> attendedIds = applicantsCommand.stream()
            .filter(UpdateApplicantAttendanceCommand::isAttended)
            .map(UpdateApplicantAttendanceCommand::applicantId)
            .toList();

        applicantRepository.updateBulkAttendance(shelterId, recruitmentId, attendedIds,
            ApplicantStatus.ATTENDANCE);
    }

    private Applicant getApplicant(Long applicantId, Long recruitmentId, Long shelterId) {
        return applicantRepository.findByApplicantIdAndRecruitment_RecruitmentIdAndRecruitment_Shelter_ShelterId(
                applicantId, recruitmentId, shelterId)
            .orElseThrow(() -> new ApplicantNotFoundException("존재하지 않는 봉사 신청입니다."));
    }

    private Recruitment getRecruitment(Long recruitmentId) {
        return recruitmentRepository.findById(recruitmentId)
            .orElseThrow(() -> new RecruitmentNotFoundException("존재하지 않는 봉사입니다."));
    }

    private Volunteer getVolunteer(Long volunteerId) {
        return volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new VolunteerNotFoundException("존재하지 않는 봉사자입니다."));
    }
}
