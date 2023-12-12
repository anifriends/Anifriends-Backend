package com.clova.anifriends.domain.payment.controller;

import static org.mockito.Mockito.when;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.queryParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.clova.anifriends.base.BaseControllerTest;
import com.clova.anifriends.domain.payment.dto.PaymentResponse;
import com.clova.anifriends.domain.payment.vo.PaymentStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

class PaymentControllerTest extends BaseControllerTest {

    @Test
    @DisplayName("성공: paySuccess Api 호출 시")
    void paySuccess() throws Exception {
        // given
        String orderId = "mockOrderId";
        String paymentKey = "mockPaymentKey";
        int amount = 1000;

        when(paymentService.paySuccess(orderId, paymentKey, amount))
            .thenReturn(new PaymentResponse(PaymentStatus.DONE, null));

        // when
        ResultActions result = mockMvc.perform(get("/api/payments/success")
            .param("orderId", orderId)
            .param("paymentKey", paymentKey)
            .param("amount", String.valueOf(amount))
            .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                queryParameters(
                    parameterWithName("orderId").description("주문 번호"),
                    parameterWithName("paymentKey").description("결제 번호"),
                    parameterWithName("amount").description("결제 금액")
                ),
                responseFields(
                    fieldWithPath("status").description("결제 상태"),
                    fieldWithPath("message").description("결제 메시지").optional()
                )
            ));
    }

    @Test
    @DisplayName("성공: payFail Api 호출 시")
    void payFail() throws Exception {
        // given
        String orderId = "mockOrderId";
        String message = "mockMessage";

        when(paymentService.payFail(orderId, message))
            .thenReturn(new PaymentResponse(PaymentStatus.ABORTED, message));

        // when
        ResultActions result = mockMvc.perform(get("/api/payments/fail")
            .param("orderId", orderId)
            .param("message", message)
            .contentType(MediaType.APPLICATION_JSON));

        // then
        result.andExpect(status().isOk())
            .andDo(restDocs.document(
                queryParameters(
                    parameterWithName("orderId").description("주문 번호"),
                    parameterWithName("message").description("결제 실패 메시지")
                ),
                responseFields(
                    fieldWithPath("status").description("결제 상태"),
                    fieldWithPath("message").description("결제 메시지")
                )
            ));
    }

}