package com.clova.anifriends.domain.donation.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.JsonFieldType.NUMBER;
import static org.springframework.restdocs.payload.JsonFieldType.STRING;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.donation.Donation;
import com.clova.anifriends.domain.donation.dto.request.RegisterDonationRequest;
import com.clova.anifriends.domain.donation.dto.response.PaymentRequestResponse;
import com.clova.anifriends.domain.donation.support.fixture.DonationFixture;
import com.clova.anifriends.domain.payment.Payment;
import com.clova.anifriends.domain.shelter.Shelter;
import com.clova.anifriends.domain.shelter.support.ShelterFixture;
import com.clova.anifriends.domain.volunteer.Volunteer;
import com.clova.anifriends.domain.volunteer.support.VolunteerFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;

class DonationControllerTest extends BaseControllerTest {

    @Value("${payment.toss.success-url}")
    private String successUrl;
    @Value("${payment.toss.fail-url}")
    private String failUrl;

    @Test
    @DisplayName("성공: 후원금을 기부하는 api 호출 시")
    void registerDonation() throws Exception {
        // given
        Shelter shelter = ShelterFixture.shelter();
        ReflectionTestUtils.setField(shelter, "shelterId", 1L);
        Volunteer volunteer = VolunteerFixture.volunteer();
        ReflectionTestUtils.setField(volunteer, "volunteerId", 1L);
        Donation donation = DonationFixture.donation(shelter, volunteer);
        Payment payment = new Payment(donation);

        PaymentRequestResponse response = PaymentRequestResponse.from(payment, successUrl, failUrl);
        RegisterDonationRequest request = new RegisterDonationRequest(shelter.getShelterId(),
            donation.getAmount());

        when(donationService.registerDonation(volunteer.getVolunteerId(), shelter.getShelterId(),
            donation.getAmount())).thenReturn(response);

        // when
        ResultActions result = mockMvc.perform(post("/api/volunteers/donations")
            .header(AUTHORIZATION, volunteerAccessToken)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                requestHeaders(
                    headerWithName(AUTHORIZATION).description("봉사자 액세스 토큰")
                ),
                requestFields(
                    fieldWithPath("shelterId").description("기부할 보호소 ID"),
                    fieldWithPath("amount").description("기부할 금액")
                ),
                responseFields(
                    fieldWithPath("amount").type(NUMBER).description("결제 금액"),
                    fieldWithPath("orderId").type(STRING).description("주문 ID"),
                    fieldWithPath("orderName").type(STRING).description("주문명"),
                    fieldWithPath("customerEmail").type(STRING).description("후원자 이메일"),
                    fieldWithPath("customerName").type(STRING).description("후원자 이름"),
                    fieldWithPath("successUrl").type(STRING).description("결제 성공 시 이동할 url"),
                    fieldWithPath("failUrl").type(STRING).description("결제 실패 시 이동할 url")
                )
            ));

    }
}