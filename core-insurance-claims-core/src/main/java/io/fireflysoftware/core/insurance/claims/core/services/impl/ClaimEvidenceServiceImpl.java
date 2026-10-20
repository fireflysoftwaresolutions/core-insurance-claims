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
import io.fireflysoftware.core.insurance.claims.core.mappers.ClaimEvidenceMapper;
import io.fireflysoftware.core.insurance.claims.core.services.ClaimEvidenceService;
import io.fireflysoftware.core.insurance.claims.interfaces.dtos.ClaimEvidenceDTO;
import io.fireflysoftware.core.insurance.claims.models.entities.ClaimEvidence;
import io.fireflysoftware.core.insurance.claims.models.repositories.ClaimEvidenceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Service
@Transactional
public class ClaimEvidenceServiceImpl implements ClaimEvidenceService {

    @Autowired
    private ClaimEvidenceRepository repository;

    @Autowired
    private ClaimEvidenceMapper mapper;

    @Override
    public Mono<PaginationResponse<ClaimEvidenceDTO>> filterClaimEvidenceItems(UUID claimId, FilterRequest<ClaimEvidenceDTO> filterRequest) {
        if (filterRequest.getFilters() == null) {
            filterRequest.setFilters(new ClaimEvidenceDTO());
        }
        filterRequest.getFilters().setClaimId(claimId);
        return FilterUtils.createFilter(ClaimEvidence.class, mapper::toDTO).filter(filterRequest);
    }

    @Override
    public Mono<ClaimEvidenceDTO> createClaimEvidence(UUID claimId, ClaimEvidenceDTO dto) {
        return Mono.just(dto)
                .doOnNext(value -> value.setClaimId(claimId))
                .map(mapper::toEntity)
                .flatMap(repository::save)
                .map(mapper::toDTO);
    }

    @Override
    public Mono<ClaimEvidenceDTO> updateClaimEvidence(UUID claimId, UUID claimEvidenceId, ClaimEvidenceDTO dto) {
        return repository.findById(claimEvidenceId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimEvidence", claimEvidenceId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimEvidence with id '" + claimEvidenceId + "' does not belong to claim '" + claimId + "'"));
                    }
                    mapper.updateEntityFromDto(dto, existing);
                    return repository.save(existing);
                })
                .map(mapper::toDTO);
    }

    @Override
    public Mono<Void> deleteClaimEvidence(UUID claimId, UUID claimEvidenceId) {
        return repository.findById(claimEvidenceId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimEvidence", claimEvidenceId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimEvidence with id '" + claimEvidenceId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return repository.deleteById(claimEvidenceId);
                });
    }

    @Override
    public Mono<ClaimEvidenceDTO> getClaimEvidenceById(UUID claimId, UUID claimEvidenceId) {
        return repository.findById(claimEvidenceId)
                .switchIfEmpty(Mono.error(ResourceNotFoundException.forResource("ClaimEvidence", claimEvidenceId.toString())))
                .flatMap(existing -> {
                    if (!claimId.equals(existing.getClaimId())) {
                        return Mono.error(ResourceNotFoundException.withReason(
                                "RESOURCE_SCOPE_MISMATCH",
                                "ClaimEvidence with id '" + claimEvidenceId + "' does not belong to claim '" + claimId + "'"));
                    }
                    return Mono.just(mapper.toDTO(existing));
                });
    }
}
