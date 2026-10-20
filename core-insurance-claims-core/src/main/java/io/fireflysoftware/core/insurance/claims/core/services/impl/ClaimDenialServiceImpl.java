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
import io.fireflysoftware.core.insurance.claims.core.mappers.ClaimDenialMapper;
import io.fireflysoftware.core.insurance.claims.core.services.ClaimDenialService;
import io.fireflysoftware.core.insurance.claims.interfaces.dtos.ClaimDenialDTO;
import io.fireflysoftware.core.insurance.claims.models.entities.ClaimDenial;
import io.fireflysoftware.core.insurance.claims.models.repositories.ClaimDenialRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class ClaimDenialServiceImpl implements ClaimDenialService {

    @Autowired
    private ClaimDenialRepository repository;

    @Autowired
    private ClaimDenialMapper mapper;

    @Override
    public Mono<PaginationResponse<ClaimDenialDTO>> filterClaimDenials(UUID claimId, FilterRequest<ClaimDenialDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new ClaimDenialDTO());
        }
        filterRequest.getFilters().setClaimId(claimId);
        return FilterUtils.createFilter(ClaimDenial.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<ClaimDenialDTO> createClaimDenial(UUID claimId, ClaimDenialDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setClaimId(claimId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ClaimDenialDTO> updateClaimDenial(UUID claimId, UUID claimDenialId, ClaimDenialDTO dto) {
        return repository.findById(claimDenialId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimDenial", claimDenialId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimDenial with id '" + claimDenialId + "' does not belong to claim '" + claimId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteClaimDenial(UUID claimId, UUID claimDenialId) {
        return repository.findById(claimDenialId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimDenial", claimDenialId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimDenial with id '" + claimDenialId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return repository.deleteById(claimDenialId);
                });
    }

    @Override
    public Mono<ClaimDenialDTO> getClaimDenialById(UUID claimId, UUID claimDenialId) {
        return repository.findById(claimDenialId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimDenial", claimDenialId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimDenial with id '" + claimDenialId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
