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
import io.fireflysoftware.core.insurance.claims.core.services.ClaimDenialService;
import io.fireflysoftware.core.insurance.claims.interfaces.dtos.ClaimDenialDTO;
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
 * REST API for denial recorded by the insurer. This service does not decide coverage..
 */
@RestController
@RequestMapping("/api/v1/claims/{claimId}/denials")
@RequiredArgsConstructor
@Validated
@Tag(name = "Claim Denials", description = "Denial recorded by the insurer. This service does not decide coverage.")
public class ClaimDenialController {

    private final ClaimDenialService claimDenialService;

    @PostMapping("/filter")
    @Operation(summary = "Filter claimDenials", description = "Retrieve a page of claimDenials matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<ClaimDenialDTO>>> filterClaimDenials(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<ClaimDenialDTO> filterRequest) {
        return claimDenialService.filterClaimDenials(claimId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create ClaimDenial", description = "Create a ClaimDenial")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "ClaimDenial created", content = @Content(schema = @Schema(implementation = ClaimDenialDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<ClaimDenialDTO>> createClaimDenial(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "ClaimDenial to create", required = true)
            @Valid @RequestBody ClaimDenialDTO dto) {
        return claimDenialService.createClaimDenial(claimId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{claimDenialId}")
    @Operation(summary = "Get ClaimDenial by id", description = "Retrieve one ClaimDenial")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimDenial found", content = @Content(schema = @Schema(implementation = ClaimDenialDTO.class))),
        @ApiResponse(responseCode = "404", description = "ClaimDenial not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimDenialDTO>> getClaimDenialById(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimDenial", required = true)
            @PathVariable UUID claimDenialId) {
        return claimDenialService.getClaimDenialById(claimId, claimDenialId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{claimDenialId}")
    @Operation(summary = "Update ClaimDenial", description = "Update an existing ClaimDenial")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimDenial updated", content = @Content(schema = @Schema(implementation = ClaimDenialDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "ClaimDenial not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimDenialDTO>> updateClaimDenial(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimDenial", required = true)
            @PathVariable UUID claimDenialId,
            @Parameter(description = "Updated ClaimDenial", required = true)
            @Valid @RequestBody ClaimDenialDTO dto) {
        return claimDenialService.updateClaimDenial(claimId, claimDenialId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{claimDenialId}")
    @Operation(summary = "Delete ClaimDenial", description = "Delete a ClaimDenial")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "ClaimDenial deleted"),
        @ApiResponse(responseCode = "404", description = "ClaimDenial not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteClaimDenial(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimDenial", required = true)
            @PathVariable UUID claimDenialId) {
        return claimDenialService.deleteClaimDenial(claimId, claimDenialId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
