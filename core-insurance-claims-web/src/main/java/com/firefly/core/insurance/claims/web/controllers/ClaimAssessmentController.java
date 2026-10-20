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


package com.firefly.core.insurance.claims.web.controllers;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import com.firefly.core.insurance.claims.core.services.ClaimAssessmentService;
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimAssessmentDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.UUID;

/**
 * REST API for assessment recorded against a claim.
 */
@RestController
@RequestMapping("/api/v1/claims/{claimId}/assessments")
@RequiredArgsConstructor
@Validated
@Tag(name = "Claim Assessments", description = "Assessment recorded against a claim")
public class ClaimAssessmentController {

    private final ClaimAssessmentService claimAssessmentService;

    @PostMapping("/filter")
    @Operation(summary = "Filter claimAssessments", description = "Retrieve a page of claimAssessments matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<ClaimAssessmentDTO>>> filterClaimAssessments(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<ClaimAssessmentDTO> filterRequest) {
        return claimAssessmentService.filterClaimAssessments(claimId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create ClaimAssessment", description = "Create a ClaimAssessment")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "ClaimAssessment created", content = @Content(schema = @Schema(implementation = ClaimAssessmentDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<ClaimAssessmentDTO>> createClaimAssessment(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "ClaimAssessment to create", required = true)
            @Valid @RequestBody ClaimAssessmentDTO dto) {
        return claimAssessmentService.createClaimAssessment(claimId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{claimAssessmentId}")
    @Operation(summary = "Get ClaimAssessment by id", description = "Retrieve one ClaimAssessment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimAssessment found", content = @Content(schema = @Schema(implementation = ClaimAssessmentDTO.class))),
        @ApiResponse(responseCode = "404", description = "ClaimAssessment not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimAssessmentDTO>> getClaimAssessmentById(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimAssessment", required = true)
            @PathVariable UUID claimAssessmentId) {
        return claimAssessmentService.getClaimAssessmentById(claimId, claimAssessmentId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{claimAssessmentId}")
    @Operation(summary = "Update ClaimAssessment", description = "Update an existing ClaimAssessment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimAssessment updated", content = @Content(schema = @Schema(implementation = ClaimAssessmentDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "ClaimAssessment not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimAssessmentDTO>> updateClaimAssessment(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimAssessment", required = true)
            @PathVariable UUID claimAssessmentId,
            @Parameter(description = "Updated ClaimAssessment", required = true)
            @Valid @RequestBody ClaimAssessmentDTO dto) {
        return claimAssessmentService.updateClaimAssessment(claimId, claimAssessmentId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{claimAssessmentId}")
    @Operation(summary = "Delete ClaimAssessment", description = "Delete a ClaimAssessment")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "ClaimAssessment deleted"),
        @ApiResponse(responseCode = "404", description = "ClaimAssessment not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteClaimAssessment(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimAssessment", required = true)
            @PathVariable UUID claimAssessmentId) {
        return claimAssessmentService.deleteClaimAssessment(claimId, claimAssessmentId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
