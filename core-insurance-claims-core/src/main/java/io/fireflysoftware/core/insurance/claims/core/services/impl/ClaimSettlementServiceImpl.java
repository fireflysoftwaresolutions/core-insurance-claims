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


package io.fireflysoftware.core.insurance.claims.core.services.impl;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.filters.FilterUtils;
import org.fireflyframework.core.queries.PaginationResponse;
import org.fireflyframework.web.error.exceptions.ResourceNotFoundException;
import io.fireflysoftware.core.insurance.claims.core.mappers.ClaimSettlementMapper;
import io.fireflysoftware.core.insurance.claims.core.services.ClaimSettlementService;
import io.fireflysoftware.core.insurance.claims.interfaces.dtos.ClaimSettlementDTO;
import io.fireflysoftware.core.insurance.claims.models.entities.ClaimSettlement;
import io.fireflysoftware.core.insurance.claims.models.repositories.ClaimSettlementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class ClaimSettlementServiceImpl implements ClaimSettlementService {

    @Autowired
    private ClaimSettlementRepository repository;

    @Autowired
    private ClaimSettlementMapper mapper;

    @Override
    public Mono<PaginationResponse<ClaimSettlementDTO>> filterClaimSettlements(UUID claimId, FilterRequest<ClaimSettlementDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new ClaimSettlementDTO());
        }
        filterRequest.getFilters().setClaimId(claimId);
        return FilterUtils.createFilter(ClaimSettlement.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<ClaimSettlementDTO> createClaimSettlement(UUID claimId, ClaimSettlementDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setClaimId(claimId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ClaimSettlementDTO> updateClaimSettlement(UUID claimId, UUID claimSettlementId, ClaimSettlementDTO dto) {
        return repository.findById(claimSettlementId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimSettlement", claimSettlementId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimSettlement with id '" + claimSettlementId + "' does not belong to claim '" + claimId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteClaimSettlement(UUID claimId, UUID claimSettlementId) {
        return repository.findById(claimSettlementId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimSettlement", claimSettlementId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimSettlement with id '" + claimSettlementId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return repository.deleteById(claimSettlementId);
                });
    }

    @Override
    public Mono<ClaimSettlementDTO> getClaimSettlementById(UUID claimId, UUID claimSettlementId) {
        return repository.findById(claimSettlementId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimSettlement", claimSettlementId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimSettlement with id '" + claimSettlementId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
