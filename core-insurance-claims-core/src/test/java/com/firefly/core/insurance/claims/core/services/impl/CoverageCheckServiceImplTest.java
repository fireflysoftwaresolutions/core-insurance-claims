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

import org.fireflyframework.web.error.exceptions.ResourceNotFoundException;
import com.firefly.core.insurance.claims.core.mappers.CoverageCheckMapper;
import com.firefly.core.insurance.claims.interfaces.dtos.CoverageCheckDTO;
import com.firefly.core.insurance.claims.models.entities.CoverageCheck;
import com.firefly.core.insurance.claims.models.repositories.CoverageCheckRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CoverageCheckServiceImplTest {

    @Mock
    private CoverageCheckRepository repository;

    @Mock
    private CoverageCheckMapper mapper;

    @InjectMocks
    private CoverageCheckServiceImpl service;

    private CoverageCheckDTO dto;
    private CoverageCheck entity;
    private UUID coverageCheckId;
    private UUID claimId;

    @BeforeEach
    void setUp() {
        coverageCheckId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");
        entity = new CoverageCheck();
        entity.setCoverageCheckId(coverageCheckId);
        entity.setCreatedAt(LocalDateTime.parse("2026-01-15T10:15:30"));
        entity.setUpdatedAt(LocalDateTime.parse("2026-01-15T10:15:30"));
        dto = new CoverageCheckDTO();
        dto.setCoverageCheckId(coverageCheckId);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        claimId = UUID.fromString("223e4567-e89b-12d3-a456-426614174000");
        entity.setClaimId(claimId);
        dto.setClaimId(claimId);
    }

    @Test
    void create_ShouldReturnDto_WhenSaveSucceeds() {
        when(mapper.toEntity(dto)).thenReturn(entity);
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        StepVerifier.create(service.createCoverageCheck(claimId, dto))
                .expectNext(dto)
                .verifyComplete();

        verify(repository).save(entity);
    }

    @Test
    void getById_ShouldReturnDto_WhenFound() {
        when(repository.findById(coverageCheckId)).thenReturn(Mono.just(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        StepVerifier.create(service.getCoverageCheckById(claimId, coverageCheckId))
                .expectNext(dto)
                .verifyComplete();
    }

    @Test
    void getById_ShouldError_WhenMissing() {
        when(repository.findById(coverageCheckId)).thenReturn(Mono.empty());

        StepVerifier.create(service.getCoverageCheckById(claimId, coverageCheckId))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }

    @Test
    void update_ShouldSaveExistingRow() {
        when(repository.findById(coverageCheckId)).thenReturn(Mono.just(entity));
        when(repository.save(entity)).thenReturn(Mono.just(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        StepVerifier.create(service.updateCoverageCheck(claimId, coverageCheckId, dto))
                .expectNext(dto)
                .verifyComplete();

        verify(mapper).updateEntityFromDto(dto, entity);
    }

    @Test
    void delete_ShouldDelete_WhenFound() {
        when(repository.findById(coverageCheckId)).thenReturn(Mono.just(entity));
        when(repository.deleteById(coverageCheckId)).thenReturn(Mono.empty());

        StepVerifier.create(service.deleteCoverageCheck(claimId, coverageCheckId))
                .verifyComplete();
    }

    @Test
    void getById_ShouldError_WhenParentDoesNotMatch() {
        entity.setClaimId(UUID.fromString("323e4567-e89b-12d3-a456-426614174000"));
        when(repository.findById(coverageCheckId)).thenReturn(Mono.just(entity));

        StepVerifier.create(service.getCoverageCheckById(claimId, coverageCheckId))
                .expectError(ResourceNotFoundException.class)
                .verify();
    }
}
