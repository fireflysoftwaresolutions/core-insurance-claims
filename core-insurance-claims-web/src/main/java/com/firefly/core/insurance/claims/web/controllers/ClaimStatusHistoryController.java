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
import com.firefly.core.insurance.claims.core.services.ClaimStatusHistoryService;
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimStatusHistoryDTO;
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
 * REST API for status change recorded against a claim.
 */
@RestController
@RequestMapping("/api/v1/claims/{claimId}/status-history")
@RequiredArgsConstructor
@Validated
@Tag(name = "Claim Status History", description = "Status change recorded against a claim")
public class ClaimStatusHistoryController {

    private final ClaimStatusHistoryService claimStatusHistoryService;

    @PostMapping("/filter")
    @Operation(summary = "Filter claimStatusHistories", description = "Retrieve a page of claimStatusHistories matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<ClaimStatusHistoryDTO>>> filterClaimStatusHistories(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<ClaimStatusHistoryDTO> filterRequest) {
        return claimStatusHistoryService.filterClaimStatusHistories(claimId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create ClaimStatusHistory", description = "Create a ClaimStatusHistory")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "ClaimStatusHistory created", content = @Content(schema = @Schema(implementation = ClaimStatusHistoryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<ClaimStatusHistoryDTO>> createClaimStatusHistory(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "ClaimStatusHistory to create", required = true)
            @Valid @RequestBody ClaimStatusHistoryDTO dto) {
        return claimStatusHistoryService.createClaimStatusHistory(claimId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{claimStatusHistoryId}")
    @Operation(summary = "Get ClaimStatusHistory by id", description = "Retrieve one ClaimStatusHistory")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimStatusHistory found", content = @Content(schema = @Schema(implementation = ClaimStatusHistoryDTO.class))),
        @ApiResponse(responseCode = "404", description = "ClaimStatusHistory not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimStatusHistoryDTO>> getClaimStatusHistoryById(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimStatusHistory", required = true)
            @PathVariable UUID claimStatusHistoryId) {
        return claimStatusHistoryService.getClaimStatusHistoryById(claimId, claimStatusHistoryId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{claimStatusHistoryId}")
    @Operation(summary = "Update ClaimStatusHistory", description = "Update an existing ClaimStatusHistory")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimStatusHistory updated", content = @Content(schema = @Schema(implementation = ClaimStatusHistoryDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "ClaimStatusHistory not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimStatusHistoryDTO>> updateClaimStatusHistory(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimStatusHistory", required = true)
            @PathVariable UUID claimStatusHistoryId,
            @Parameter(description = "Updated ClaimStatusHistory", required = true)
            @Valid @RequestBody ClaimStatusHistoryDTO dto) {
        return claimStatusHistoryService.updateClaimStatusHistory(claimId, claimStatusHistoryId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{claimStatusHistoryId}")
    @Operation(summary = "Delete ClaimStatusHistory", description = "Delete a ClaimStatusHistory")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "ClaimStatusHistory deleted"),
        @ApiResponse(responseCode = "404", description = "ClaimStatusHistory not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteClaimStatusHistory(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimStatusHistory", required = true)
            @PathVariable UUID claimStatusHistoryId) {
        return claimStatusHistoryService.deleteClaimStatusHistory(claimId, claimStatusHistoryId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
