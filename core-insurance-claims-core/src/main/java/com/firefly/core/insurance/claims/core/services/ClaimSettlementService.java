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
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimSettlementDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for authorized settlement. Payment execution is a payment order, not this record..
 */
public interface ClaimSettlementService {

    /**
     * Filters claimSettlements with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of ClaimSettlementDTO
     */
    Mono<PaginationResponse<ClaimSettlementDTO>> filterClaimSettlements(UUID claimId, FilterRequest<ClaimSettlementDTO> filterRequest);

    /**
     * Creates a ClaimSettlement.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<ClaimSettlementDTO> createClaimSettlement(UUID claimId, ClaimSettlementDTO dto);

    /**
     * Updates a ClaimSettlement.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<ClaimSettlementDTO> updateClaimSettlement(UUID claimId, UUID claimSettlementId, ClaimSettlementDTO dto);

    /**
     * Deletes a ClaimSettlement.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteClaimSettlement(UUID claimId, UUID claimSettlementId);

    /**
     * Loads a ClaimSettlement by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<ClaimSettlementDTO> getClaimSettlementById(UUID claimId, UUID claimSettlementId);
}
