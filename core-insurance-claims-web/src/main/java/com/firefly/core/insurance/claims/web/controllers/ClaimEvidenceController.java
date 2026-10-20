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
import com.firefly.core.insurance.claims.core.services.ClaimEvidenceService;
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimEvidenceDTO;
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
 * REST API for evidence reference on a claim. The document bytes stay in document management..
 */
@RestController
@RequestMapping("/api/v1/claims/{claimId}/evidence")
@RequiredArgsConstructor
@Validated
@Tag(name = "Claim Evidence", description = "Evidence reference on a claim. The document bytes stay in document management.")
public class ClaimEvidenceController {

    private final ClaimEvidenceService claimEvidenceService;

    @PostMapping("/filter")
    @Operation(summary = "Filter claimEvidenceItems", description = "Retrieve a page of claimEvidenceItems matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<ClaimEvidenceDTO>>> filterClaimEvidenceItems(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<ClaimEvidenceDTO> filterRequest) {
        return claimEvidenceService.filterClaimEvidenceItems(claimId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create ClaimEvidence", description = "Create a ClaimEvidence")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "ClaimEvidence created", content = @Content(schema = @Schema(implementation = ClaimEvidenceDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<ClaimEvidenceDTO>> createClaimEvidence(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "ClaimEvidence to create", required = true)
            @Valid @RequestBody ClaimEvidenceDTO dto) {
        return claimEvidenceService.createClaimEvidence(claimId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{claimEvidenceId}")
    @Operation(summary = "Get ClaimEvidence by id", description = "Retrieve one ClaimEvidence")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimEvidence found", content = @Content(schema = @Schema(implementation = ClaimEvidenceDTO.class))),
        @ApiResponse(responseCode = "404", description = "ClaimEvidence not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimEvidenceDTO>> getClaimEvidenceById(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimEvidence", required = true)
            @PathVariable UUID claimEvidenceId) {
        return claimEvidenceService.getClaimEvidenceById(claimId, claimEvidenceId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{claimEvidenceId}")
    @Operation(summary = "Update ClaimEvidence", description = "Update an existing ClaimEvidence")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimEvidence updated", content = @Content(schema = @Schema(implementation = ClaimEvidenceDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "ClaimEvidence not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimEvidenceDTO>> updateClaimEvidence(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimEvidence", required = true)
            @PathVariable UUID claimEvidenceId,
            @Parameter(description = "Updated ClaimEvidence", required = true)
            @Valid @RequestBody ClaimEvidenceDTO dto) {
        return claimEvidenceService.updateClaimEvidence(claimId, claimEvidenceId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{claimEvidenceId}")
    @Operation(summary = "Delete ClaimEvidence", description = "Delete a ClaimEvidence")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "ClaimEvidence deleted"),
        @ApiResponse(responseCode = "404", description = "ClaimEvidence not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteClaimEvidence(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimEvidence", required = true)
            @PathVariable UUID claimEvidenceId) {
        return claimEvidenceService.deleteClaimEvidence(claimId, claimEvidenceId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
