package com.clova.anifriends.domain.applicant.service;

import com.clova.anifriends.domain.applicant.Applicant;
import com.clova.anifriends.domain.applicant.dto.FindApplicantsResponse;
import com.clova.anifriends.domain.applicant.dto.response.FindApprovedApplicantsResponse;
import com.clova.anifriends.domain.applicant.dto.response.FindApplyingVolunteersResponse;
import com.clova.anifriends.domain.applicant.exception.ApplicantConflictException;
import com.clova.anifriends.domain.applicant.repository.ApplicantRepository;
import com.clova.anifriends.domain.applicant.repository.response.FindApplyingVolunteerResult;
import com.clova.anifriends.domain.applicant.service.dto.UpdateApplicantAttendanceCommand;
import com.clova.anifriends.domain.applicant.vo.ApplicantStatus;
import com.clova.anifriends.domain.notification.ShelterNotification;
import com.clova.anifriends.domain.notification.VolunteerNotification;
import com.clova.anifriends.domain.notification.repository.ShelterNotificationRepository;
import com.clova.anifriends.domain.notification.repository.VolunteerNotificationRepository;
import com.clova.anifriends.domain.notification.vo.NotificationType;
import com.clova.anifriends.domain.recruitment.Recruitment;
import com.clova.anifriends.domain.recruitment.exception.RecruitmentNotFoundException;
import com.clova.anifriends.domain.recruitment.repository.RecruitmentRepository;
import com.clova.anifriends.domain.review.exception.ApplicantNotFoundException;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.exception.VolunteerNotFoundException;
import com.clova.anifriends.domain.volunteer.repository.VolunteerRepository;
import com.clova.anifriends.global.aspect.DataIntegrityHandler;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicantService {

    public static final int NO_SHOW_TEMP_REDUCTION = 10;

    private final ApplicantRepository applicantRepository;
    private final RecruitmentRepository recruitmentRepository;
    private final VolunteerRepository volunteerRepository;
    private final ShelterNotificationRepository shelterNotificationRepository;
    private final VolunteerNotificationRepository volunteerNotificationRepository;

    @Transactional
    @DataIntegrityHandler(message = "이미 신청한 봉사입니다.", exceptionClass = ApplicantConflictException.class)
    public void registerApplicant(Long recruitmentId, Long volunteerId) {
        Recruitment recruitmentPessimistic = getRecruitmentPessimistic(recruitmentId);
        Volunteer volunteer = getVolunteer(volunteerId);
        Applicant applicant = new Applicant(recruitmentPessimistic, volunteer);
        applicantRepository.save(applicant);
        shelterNotificationRepository.save(makeNewApplicantNotification(applicant));
        if (recruitmentPessimistic.isFullApplicants()) {
            shelterNotificationRepository.save(
                makeClosedRecruitmentNotification(recruitmentPessimistic));
        }
    }

    @Transactional(readOnly = true)
    public FindApplyingVolunteersResponse findApplyingVolunteers(
        Long volunteerId
    ) {
        Volunteer foundVolunteer = getVolunteer(volunteerId);
        List<FindApplyingVolunteerResult> applyingVolunteers
            = applicantRepository.findApplyingVolunteers(foundVolunteer);
        return ApplicantMapper.resultToResponse(applyingVolunteers);
    }

    @Transactional(readOnly = true)
    public FindApprovedApplicantsResponse findApprovedApplicants(Long shelterId,
        Long recruitmentId) {
        List<Applicant> applicantsApproved = applicantRepository
            .findApprovedApplicants(recruitmentId, shelterId);
        return FindApprovedApplicantsResponse.from(applicantsApproved);
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
        List<Long> noShowIds = getNoShowIds(applicantsCommand);
        List<Long> attendedIds = getAttendedIds(applicantsCommand);

        updateVolunteersTemperature(shelterId, recruitmentId, noShowIds, attendedIds);
        updateAttendanceStatus(shelterId, recruitmentId, noShowIds, attendedIds);
        volunteerNotificationRepository.saveAll(
            makeNewAttendanceNotifications(shelterId, recruitmentId, attendedIds));
    }

    private void updateVolunteersTemperature(Long shelterId, Long recruitmentId,
        List<Long> noShowIds, List<Long> attendedIds) {
        List<Volunteer> noShowVolunteers = volunteerRepository.findAttendedByNoShowIds(shelterId,
            recruitmentId,
            noShowIds);
        noShowVolunteers.forEach(
            volunteer -> volunteer.decreaseTemperature(NO_SHOW_TEMP_REDUCTION));

        List<Volunteer> attendedVolunteers = volunteerRepository.findNoShowByAttendedIds(
            shelterId, recruitmentId, attendedIds);
        attendedVolunteers
            .forEach(volunteer -> volunteer.increaseTemperature(NO_SHOW_TEMP_REDUCTION));
    }

    private void updateAttendanceStatus(Long shelterId, Long recruitmentId, List<Long> noShowIds,
        List<Long> attendedIds) {
        applicantRepository.updateBulkAttendance(shelterId, recruitmentId, noShowIds,
            ApplicantStatus.NO_SHOW);
        applicantRepository.updateBulkAttendance(shelterId, recruitmentId, attendedIds,
            ApplicantStatus.ATTENDANCE);
    }

    @Transactional
    public void updateApplicantStatus(Long applicantId, Long recruitmentId, Long shelterId,
        Boolean isApproved) {
        Applicant applicant = getApplicant(applicantId, recruitmentId, shelterId);
        applicant.updateApplicantStatus(isApproved);
        volunteerNotificationRepository.save(
            makeNewUpdateApplicantStatusNotification(applicant, isApproved));
    }

    private List<Long> getNoShowIds(List<UpdateApplicantAttendanceCommand> applicantsCommand) {
        return applicantsCommand.stream()
            .filter(applicant -> !applicant.isAttended())
            .map(UpdateApplicantAttendanceCommand::applicantId)
            .toList();
    }

    private List<Long> getAttendedIds(List<UpdateApplicantAttendanceCommand> applicantsCommand) {
        return applicantsCommand.stream()
            .filter(UpdateApplicantAttendanceCommand::isAttended)
            .map(UpdateApplicantAttendanceCommand::applicantId)
            .toList();
    }

    private Applicant getApplicant(Long applicantId, Long recruitmentId, Long shelterId) {
        return applicantRepository.findByApplicantIdAndRecruitment_RecruitmentIdAndRecruitment_Shelter_ShelterId(
                applicantId, recruitmentId, shelterId)
            .orElseThrow(() -> new ApplicantNotFoundException("존재하지 않는 봉사 신청입니다."));
    }

    private Recruitment getRecruitmentPessimistic(Long recruitmentId) {
        return recruitmentRepository.findByIdPessimistic(recruitmentId)
            .orElseThrow(() -> new RecruitmentNotFoundException("존재하지 않는 봉사 모집글입니다."));
    }

    private Recruitment getRecruitment(Long recruitmentId) {
        return recruitmentRepository.findById(recruitmentId)
            .orElseThrow(() -> new RecruitmentNotFoundException("존재하지 않는 봉사입니다."));
    }

    private Volunteer getVolunteer(Long volunteerId) {
        return volunteerRepository.findById(volunteerId)
            .orElseThrow(() -> new VolunteerNotFoundException("존재하지 않는 봉사자입니다."));
    }

    private ShelterNotification makeNewApplicantNotification(Applicant applicant) {
        return new ShelterNotification(
            applicant.getRecruitment().getShelter(),
            applicant.getRecruitment().getTitle(),
            applicant.getVolunteer().getName() + NotificationType.NEW_APPLICANT.getMessage(),
            NotificationType.NEW_APPLICANT.getName()
        );
    }

    private VolunteerNotification makeNewUpdateApplicantStatusNotification(Applicant applicant,
        boolean isApproved) {
        return new VolunteerNotification(
            applicant.getVolunteer(),
            applicant.getRecruitment().getShelter().getName(),
            isApproved ? NotificationType.VOLUNTEER_APPROVED.getMessage()
                : NotificationType.VOLUNTEER_REFUSED.getMessage(),
            isApproved ? NotificationType.VOLUNTEER_APPROVED.getName()
                : NotificationType.VOLUNTEER_REFUSED.getName()
        );
    }

    private List<VolunteerNotification> makeNewAttendanceNotifications(Long shelterId,
        Long recruitmentId, List<Long> attendedIds) {
        return volunteerRepository.findNoShowByAttendedIds(shelterId, recruitmentId, attendedIds)
            .stream()
            .map(this::makeNewAttendanceNotification)
            .collect(Collectors.toList());
    }

    private VolunteerNotification makeNewAttendanceNotification(Volunteer volunteer) {
        return new VolunteerNotification(
            volunteer,
            null,
            NO_SHOW_TEMP_REDUCTION + NotificationType.INCREASE_VOLUNTEER_TEMPERATURE.getMessage(),
            NotificationType.INCREASE_VOLUNTEER_TEMPERATURE.getName()
        );
    }

    private ShelterNotification makeClosedRecruitmentNotification(Recruitment recruitment) {
        return new ShelterNotification(
            recruitment.getShelter(),
            recruitment.getTitle(),
            NotificationType.APPLICANT_FULL.getMessage(),
            NotificationType.APPLICANT_FULL.getName()
        );
    }
}
