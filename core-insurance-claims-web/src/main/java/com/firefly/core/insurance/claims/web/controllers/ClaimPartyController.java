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
import com.firefly.core.insurance.claims.core.services.ClaimPartyService;
import com.firefly.core.insurance.claims.interfaces.dtos.ClaimPartyDTO;
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
 * REST API for party role on a claim. The party itself is owned by customer management..
 */
@RestController
@RequestMapping("/api/v1/claims/{claimId}/parties")
@RequiredArgsConstructor
@Validated
@Tag(name = "Claim Parties", description = "Party role on a claim. The party itself is owned by customer management.")
public class ClaimPartyController {

    private final ClaimPartyService claimPartyService;

    @PostMapping("/filter")
    @Operation(summary = "Filter claimParties", description = "Retrieve a page of claimParties matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<ClaimPartyDTO>>> filterClaimParties(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<ClaimPartyDTO> filterRequest) {
        return claimPartyService.filterClaimParties(claimId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create ClaimParty", description = "Create a ClaimParty")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "ClaimParty created", content = @Content(schema = @Schema(implementation = ClaimPartyDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<ClaimPartyDTO>> createClaimParty(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "ClaimParty to create", required = true)
            @Valid @RequestBody ClaimPartyDTO dto) {
        return claimPartyService.createClaimParty(claimId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{claimPartyId}")
    @Operation(summary = "Get ClaimParty by id", description = "Retrieve one ClaimParty")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimParty found", content = @Content(schema = @Schema(implementation = ClaimPartyDTO.class))),
        @ApiResponse(responseCode = "404", description = "ClaimParty not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimPartyDTO>> getClaimPartyById(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimParty", required = true)
            @PathVariable UUID claimPartyId) {
        return claimPartyService.getClaimPartyById(claimId, claimPartyId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{claimPartyId}")
    @Operation(summary = "Update ClaimParty", description = "Update an existing ClaimParty")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "ClaimParty updated", content = @Content(schema = @Schema(implementation = ClaimPartyDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "ClaimParty not found", content = @Content)
    })
    public Mono<ResponseEntity<ClaimPartyDTO>> updateClaimParty(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimParty", required = true)
            @PathVariable UUID claimPartyId,
            @Parameter(description = "Updated ClaimParty", required = true)
            @Valid @RequestBody ClaimPartyDTO dto) {
        return claimPartyService.updateClaimParty(claimId, claimPartyId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{claimPartyId}")
    @Operation(summary = "Delete ClaimParty", description = "Delete a ClaimParty")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "ClaimParty deleted"),
        @ApiResponse(responseCode = "404", description = "ClaimParty not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteClaimParty(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the ClaimParty", required = true)
            @PathVariable UUID claimPartyId) {
        return claimPartyService.deleteClaimParty(claimId, claimPartyId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
