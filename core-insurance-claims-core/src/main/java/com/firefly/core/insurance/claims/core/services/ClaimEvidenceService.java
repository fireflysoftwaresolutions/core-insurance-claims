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
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimEvidenceDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for evidence reference on a claim. The document bytes stay in document management..
 */
public interface ClaimEvidenceService {

    /**
     * Filters claimEvidenceItems with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of ClaimEvidenceDTO
     */
    Mono<PaginationResponse<ClaimEvidenceDTO>> filterClaimEvidenceItems(UUID claimId, FilterRequest<ClaimEvidenceDTO> filterRequest);

    /**
     * Creates a ClaimEvidence.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<ClaimEvidenceDTO> createClaimEvidence(UUID claimId, ClaimEvidenceDTO dto);

    /**
     * Updates a ClaimEvidence.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<ClaimEvidenceDTO> updateClaimEvidence(UUID claimId, UUID claimEvidenceId, ClaimEvidenceDTO dto);

    /**
     * Deletes a ClaimEvidence.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteClaimEvidence(UUID claimId, UUID claimEvidenceId);

    /**
     * Loads a ClaimEvidence by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<ClaimEvidenceDTO> getClaimEvidenceById(UUID claimId, UUID claimEvidenceId);
}
