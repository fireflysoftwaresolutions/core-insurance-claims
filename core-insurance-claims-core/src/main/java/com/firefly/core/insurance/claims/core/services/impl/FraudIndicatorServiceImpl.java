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


package com.firefly.core.insurance.claims.core.services.impl;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.web.error.exceptions.ResourceNotFoundException;
import com.firefly.core.insurance.claims.core.mappers.FraudIndicatorMapper;
import com.firefly.core.insurance.claims.core.services.FraudIndicatorService;
import com.firefly.core.insurance.claims.interfaces.dtos.FraudIndicatorDTO;
import com.firefly.core.insurance.claims.models.entities.FraudIndicator;
import com.firefly.core.insurance.claims.models.repositories.FraudIndicatorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class FraudIndicatorServiceImpl implements FraudIndicatorService {

    @Autowired
    private FraudIndicatorRepository repository;

    @Autowired
    private FraudIndicatorMapper mapper;

    @Override
    public Mono<PaginationResponse<FraudIndicatorDTO>> filterFraudIndicators(UUID claimId, FilterRequest<FraudIndicatorDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new FraudIndicatorDTO());
        }
        filterRequest.getFilters().setClaimId(claimId);
        return FilterUtils.createFilter(FraudIndicator.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<FraudIndicatorDTO> createFraudIndicator(UUID claimId, FraudIndicatorDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setClaimId(claimId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<FraudIndicatorDTO> updateFraudIndicator(UUID claimId, UUID fraudIndicatorId, FraudIndicatorDTO dto) {
        return repository.findById(fraudIndicatorId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("FraudIndicator", fraudIndicatorId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "FraudIndicator with id '" + fraudIndicatorId + "' does not belong to claim '" + claimId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteFraudIndicator(UUID claimId, UUID fraudIndicatorId) {
        return repository.findById(fraudIndicatorId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("FraudIndicator", fraudIndicatorId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "FraudIndicator with id '" + fraudIndicatorId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return repository.deleteById(fraudIndicatorId);
                });
    }

    @Override
    public Mono<FraudIndicatorDTO> getFraudIndicatorById(UUID claimId, UUID fraudIndicatorId) {
        return repository.findById(fraudIndicatorId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("FraudIndicator", fraudIndicatorId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "FraudIndicator with id '" + fraudIndicatorId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
