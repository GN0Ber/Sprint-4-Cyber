package com.wise.buddy.wiseBuddy.service.impl;

import com.wise.buddy.wiseBuddy.dto.RecommendationRequestDTO;
import com.wise.buddy.wiseBuddy.dto.RecommendationResponseDTO;
import com.wise.buddy.wiseBuddy.model.RecommendationModel;
import com.wise.buddy.wiseBuddy.model.SessionModel;
import com.wise.buddy.wiseBuddy.model.SuitabilityModel;
import com.wise.buddy.wiseBuddy.model.UserModel;
import com.wise.buddy.wiseBuddy.repository.RecommendationRepository;
import com.wise.buddy.wiseBuddy.repository.SessionsRepository;
import com.wise.buddy.wiseBuddy.repository.SuitabilityRepository;
import com.wise.buddy.wiseBuddy.repository.UserRepository;
import com.wise.buddy.wiseBuddy.service.RecommendationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecommendationServiceImpl implements RecommendationService {

    private final RecommendationRepository recommendationRepository;
    private final UserRepository userRepository;
    private final SessionsRepository sessionRepository;
    private final SuitabilityRepository suitabilityRepository;

    public RecommendationServiceImpl(RecommendationRepository recommendationRepository,
                                     UserRepository userRepository,
                                     SessionsRepository sessionRepository,
                                     SuitabilityRepository suitabilityRepository) {
        this.recommendationRepository = recommendationRepository;
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
        this.suitabilityRepository = suitabilityRepository;
    }

    @Override
    public RecommendationResponseDTO createRecommendation(RecommendationRequestDTO dto) {
        UserModel user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado: " + dto.getUserId()));

        SessionModel session = sessionRepository.findById(dto.getSessionId())
                .orElseThrow(() -> new IllegalArgumentException("Sessão não encontrada: " + dto.getSessionId()));

        SuitabilityModel suitability = suitabilityRepository.findById(dto.getSuitabilityId())
                .orElseThrow(() -> new IllegalArgumentException("Suitability não encontrado: " + dto.getSuitabilityId()));

        RecommendationModel rec = new RecommendationModel();
        rec.setUser(user);
        rec.setSession(session);
        rec.setSuitability(suitability);
        rec.setInputJson(dto.getInputJson());
        rec.setOutputJson(dto.getOutputJson());
        rec.setRequestDate(dto.getRequestDate());

        RecommendationModel saved = recommendationRepository.save(rec);

        return new RecommendationResponseDTO(
                saved.getRecommendationId(),
                saved.getUser().getId(),
                saved.getSession().getSessionId(),
                saved.getSuitability().getSuitabilityId(),
                saved.getInputJson(),
                saved.getOutputJson(),
                saved.getRequestDate()
        );
    }

    @Override
    public List<RecommendationResponseDTO> getRecommendationsByUserId(Long userId) {
        List<RecommendationModel> recs = recommendationRepository.findAllByUser_Id(userId);
        return recs.stream().map(
                r -> new RecommendationResponseDTO(
                        r.getRecommendationId(),
                        r.getUser().getId(),
                        r.getSession().getSessionId(),
                        r.getSuitability().getSuitabilityId(),
                        r.getInputJson(),
                        r.getOutputJson(),
                        r.getRequestDate()
                )
        ).collect(Collectors.toList());
    }

    @Override
    public RecommendationResponseDTO getRecommendationById(Long recommendationId) {
        RecommendationModel r = recommendationRepository.findById(recommendationId)
                .orElseThrow(() -> new IllegalArgumentException("Recomendação não encontrada: " + recommendationId));
        return new RecommendationResponseDTO(
                r.getRecommendationId(),
                r.getUser().getId(),
                r.getSession().getSessionId(),
                r.getSuitability().getSuitabilityId(),
                r.getInputJson(),
                r.getOutputJson(),
                r.getRequestDate()
        );
    }
}