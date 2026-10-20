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


package io.fireflysoftware.core.insurance.claims.core.services;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.insurance.claims.interfaces.dtos.FraudIndicatorDTO;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * Repository operations for fraud indicator recorded against a claim. Investigation stays with the insurer..
 */
public interface FraudIndicatorService {

    /**
     * Filters fraudIndicators with the given criteria.
     *
     * @param parentId and filterRequest filter criteria
     * @return a page of FraudIndicatorDTO
     */
    Mono<PaginationResponse<FraudIndicatorDTO>> filterFraudIndicators(UUID claimId, FilterRequest<FraudIndicatorDTO> filterRequest);

    /**
     * Creates a FraudIndicator.
     *
     * @param parentId and dto values to store
     * @return the stored DTO
     */
    Mono<FraudIndicatorDTO> createFraudIndicator(UUID claimId, FraudIndicatorDTO dto);

    /**
     * Updates a FraudIndicator.
     *
     * @param parentId, id and dto
     * @return the updated DTO
     */
    Mono<FraudIndicatorDTO> updateFraudIndicator(UUID claimId, UUID fraudIndicatorId, FraudIndicatorDTO dto);

    /**
     * Deletes a FraudIndicator.
     *
     * @param parentId and id identifier
     * @return completion signal
     */
    Mono<Void> deleteFraudIndicator(UUID claimId, UUID fraudIndicatorId);

    /**
     * Loads a FraudIndicator by id.
     *
     * @param parentId and id identifier
     * @return the DTO
     */
    Mono<FraudIndicatorDTO> getFraudIndicatorById(UUID claimId, UUID fraudIndicatorId);
}
