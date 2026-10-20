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
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimDenialDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for denial recorded by the insurer. This service does not decide coverage..
 */
public interface ClaimDenialService {

    /**
     * Filters claimDenials with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of ClaimDenialDTO
     */
    Mono<PaginationResponse<ClaimDenialDTO>> filterClaimDenials(UUID claimId, FilterRequest<ClaimDenialDTO> filterRequest);

    /**
     * Creates a ClaimDenial.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<ClaimDenialDTO> createClaimDenial(UUID claimId, ClaimDenialDTO dto);

    /**
     * Updates a ClaimDenial.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<ClaimDenialDTO> updateClaimDenial(UUID claimId, UUID claimDenialId, ClaimDenialDTO dto);

    /**
     * Deletes a ClaimDenial.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteClaimDenial(UUID claimId, UUID claimDenialId);

    /**
     * Loads a ClaimDenial by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<ClaimDenialDTO> getClaimDenialById(UUID claimId, UUID claimDenialId);
}
