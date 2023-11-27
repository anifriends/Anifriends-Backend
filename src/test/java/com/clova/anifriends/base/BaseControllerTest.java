package com.clova.anifriends.base;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.clova.anifriends.base.BaseControllerTest.WebMvcTestConfig;
import com.clova.anifriends.base.config.RestDocsConfig;
import com.clova.anifriends.domain.animal.repository.AnimalCacheRepository;
import com.clova.anifriends.domain.animal.service.AnimalService;
import com.clova.anifriends.domain.applicant.service.ApplicantService;
import com.clova.anifriends.domain.auth.authentication.JwtAuthenticationProvider;
import com.clova.anifriends.domain.auth.jwt.JwtProvider;
import com.clova.anifriends.domain.auth.service.AuthService;
import com.clova.anifriends.domain.auth.support.AuthFixture;
import com.clova.anifriends.domain.chat.service.ChatMessageService;
import com.clova.anifriends.domain.chat.service.ChatRoomService;
import com.clova.anifriends.domain.notification.service.ShelterNotificationService;
import com.clova.anifriends.domain.notification.service.VolunteerNotificationService;
import com.clova.anifriends.domain.recruitment.service.RecruitmentService;
import com.clova.anifriends.domain.review.service.ReviewService;
import com.clova.anifriends.domain.shelter.service.ShelterService;
import com.clova.anifriends.domain.volunteer.service.VolunteerService;
import com.clova.anifriends.global.config.RedisConfig;
import com.clova.anifriends.global.config.SecurityConfig;
import com.clova.anifriends.global.config.WebMvcConfig;
import com.clova.anifriends.global.image.S3Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

@WebMvcTest
@Import({SecurityConfig.class, WebMvcConfig.class, RestDocsConfig.class, WebMvcTestConfig.class,
    RedisConfig.class})
@ExtendWith(RestDocumentationExtension.class)
public abstract class BaseControllerTest {

    protected static final String AUTHORIZATION = "Authorization";

    @TestConfiguration
    static class WebMvcTestConfig {

        @Bean
        public JwtProvider jwtProvider() {
            return AuthFixture.jwtProvider();
        }

        @Bean
        public JwtAuthenticationProvider jwtAuthenticationProvider(JwtProvider jwtProvider) {
            return new JwtAuthenticationProvider(jwtProvider);
        }
    }

    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected RestDocumentationResultHandler restDocs;

    @MockBean
    protected RecruitmentService recruitmentService;

    @MockBean
    protected AuthService authService;

    @MockBean
    protected VolunteerService volunteerService;

    @MockBean
    protected AnimalService animalService;

    @MockBean
    protected AnimalCacheRepository animalCacheRepository;

    @MockBean
    protected ShelterService shelterService;

    @MockBean
    protected ApplicantService applicantService;

    @MockBean
    protected ReviewService reviewService;

    @MockBean
    protected S3Service s3Service;

    @MockBean
    protected ChatRoomService chatRoomService;

    @MockBean
    protected ChatMessageService chatMessageService;

    @MockBean
    protected SimpMessageSendingOperations messagingTemplate;

    @MockBean
    protected ShelterNotificationService shelterNotificationService;

    @MockBean
    protected VolunteerNotificationService volunteerNotificationService;

    protected final String volunteerAccessToken = AuthFixture.volunteerAccessToken();
    protected String shelterAccessToken = AuthFixture.shelterAccessToken();


    @BeforeEach
    void setUp(
        WebApplicationContext applicationContext,
        RestDocumentationContextProvider documentationContextProvider) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(applicationContext)
            .alwaysDo(print())
            .alwaysDo(restDocs)
            .apply(springSecurity())
            .apply(
                MockMvcRestDocumentation.documentationConfiguration(documentationContextProvider))
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .defaultRequest(post("/**").with(csrf().asHeader()))
            .defaultRequest(patch("/**").with(csrf().asHeader()))
            .defaultRequest(delete("/**").with(csrf().asHeader()))
            .build();
    }
}
