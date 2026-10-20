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


package io.fireflysoftware.core.insurance.claims.web.controllers;

import org.fireflyframework.core.filters.FilterRequest;
import org.fireflyframework.core.queries.PaginationResponse;
import io.fireflysoftware.core.insurance.claims.core.services.ClaimCoverageService;
import io.fireflysoftware.core.insurance.claims.interfaces.dtos.ClaimCoverageDTO;
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
 * REST API for coverage on the policy against which the claim is made.
 */
@RestController
@RequestMapping("/api/v1/claims/{claimId}/coverages")
@RequiredArgsConstructor
@Validated
@Tag(name = "Claim Coverages", description = "Coverage on the policy against which the claim is made")
public class ClaimCoverageController {

    private final ClaimCoverageService claimCoverageService;

    @PostMapping("/filter")
    @Operation(summary = "Filter claimCoverages", description = "Retrieve a page of claimCoverages matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<ClaimCoverageDTO>>> filterClaimCoverages(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<ClaimCoverageDTO> filterRequest) {
        return claimCoverageService.filterClaimCoverages(claimId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create ClaimCoverage", description = "Create a ClaimCoverage")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "ClaimCoverage created", content = @Content(schema = @Schema(implementation = ClaimCoverageDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<ClaimCoverageDTO>> createClaimCoverage(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "ClaimCoverage to create", required = true)
            @Valid @RequestBody ClaimCoverageDTO dto) {
        return claimCoverageService.createClaimCoverage(claimId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{claimCoverageId}")
    @Operation(summary = "Get ClaimCoverage by id", description = "Retrieve one ClaimCoverage")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimCoverage found", content = @Content(schema = @Schema(implementation = ClaimCoverageDTO.class))),
        @ApiResponse(responseCode = "404", description = "ClaimCoverage not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimCoverageDTO>> getClaimCoverageById(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimCoverage", required = true)
            @PathVariable UUID claimCoverageId) {
        return claimCoverageService.getClaimCoverageById(claimId, claimCoverageId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{claimCoverageId}")
    @Operation(summary = "Update ClaimCoverage", description = "Update an existing ClaimCoverage")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimCoverage updated", content = @Content(schema = @Schema(implementation = ClaimCoverageDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "ClaimCoverage not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimCoverageDTO>> updateClaimCoverage(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimCoverage", required = true)
            @PathVariable UUID claimCoverageId,
            @Parameter(description = "Updated ClaimCoverage", required = true)
            @Valid @RequestBody ClaimCoverageDTO dto) {
        return claimCoverageService.updateClaimCoverage(claimId, claimCoverageId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{claimCoverageId}")
    @Operation(summary = "Delete ClaimCoverage", description = "Delete a ClaimCoverage")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "ClaimCoverage deleted"),
        @ApiResponse(responseCode = "404", description = "ClaimCoverage not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteClaimCoverage(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimCoverage", required = true)
            @PathVariable UUID claimCoverageId) {
        return claimCoverageService.deleteClaimCoverage(claimId, claimCoverageId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
