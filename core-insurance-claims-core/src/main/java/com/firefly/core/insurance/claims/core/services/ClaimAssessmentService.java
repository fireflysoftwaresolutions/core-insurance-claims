/*
 * Copyright 2026 Firefly Software Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


package com.firefly.core.insurance.claims.core.services;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimAssessmentDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for assessment recorded against a claim.
 */
public interface ClaimAssessmentService {

    /**
     * Filters claimAssessments with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of ClaimAssessmentDTO
     */
    Mono<PaginationResponse<ClaimAssessmentDTO>> filterClaimAssessments(UUID claimId, FilterRequest<ClaimAssessmentDTO> filterRequest);

    /**
     * Creates a ClaimAssessment.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<ClaimAssessmentDTO> createClaimAssessment(UUID claimId, ClaimAssessmentDTO dto);

    /**
     * Updates a ClaimAssessment.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<ClaimAssessmentDTO> updateClaimAssessment(UUID claimId, UUID claimAssessmentId, ClaimAssessmentDTO dto);

    /**
     * Deletes a ClaimAssessment.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteClaimAssessment(UUID claimId, UUID claimAssessmentId);

    /**
     * Loads a ClaimAssessment by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<ClaimAssessmentDTO> getClaimAssessmentById(UUID claimId, UUID claimAssessmentId);
}
