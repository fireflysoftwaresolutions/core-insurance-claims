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
import com.firefly.core.insurance.claims.core.mappers.AdjusterAssignmentMapper;
import com.firefly.core.insurance.claims.core.services.AdjusterAssignmentService;
import com.firefly.core.insurance.claims.interfaces.dtos.AdjusterAssignmentDTO;
import com.firefly.core.insurance.claims.models.entities.AdjusterAssignment;
import com.firefly.core.insurance.claims.models.repositories.AdjusterAssignmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class AdjusterAssignmentServiceImpl implements AdjusterAssignmentService {

    @Autowired
    private AdjusterAssignmentRepository repository;

    @Autowired
    private AdjusterAssignmentMapper mapper;

    @Override
    public Mono<PaginationResponse<AdjusterAssignmentDTO>> filterAdjusterAssignments(UUID claimId, FilterRequest<AdjusterAssignmentDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new AdjusterAssignmentDTO());
        }
        filterRequest.getFilters().setClaimId(claimId);
        return FilterUtils.createFilter(AdjusterAssignment.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<AdjusterAssignmentDTO> createAdjusterAssignment(UUID claimId, AdjusterAssignmentDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setClaimId(claimId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<AdjusterAssignmentDTO> updateAdjusterAssignment(UUID claimId, UUID adjusterAssignmentId, AdjusterAssignmentDTO dto) {
        return repository.findById(adjusterAssignmentId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("AdjusterAssignment", adjusterAssignmentId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "AdjusterAssignment with id '" + adjusterAssignmentId + "' does not belong to claim '" + claimId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteAdjusterAssignment(UUID claimId, UUID adjusterAssignmentId) {
        return repository.findById(adjusterAssignmentId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("AdjusterAssignment", adjusterAssignmentId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "AdjusterAssignment with id '" + adjusterAssignmentId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return repository.deleteById(adjusterAssignmentId);
                });
    }

    @Override
    public Mono<AdjusterAssignmentDTO> getAdjusterAssignmentById(UUID claimId, UUID adjusterAssignmentId) {
        return repository.findById(adjusterAssignmentId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("AdjusterAssignment", adjusterAssignmentId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "AdjusterAssignment with id '" + adjusterAssignmentId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
