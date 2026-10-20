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
import io.fireflysoftware.core.insurance.claims.core.services.AdjusterAssignmentService;
import io.fireflysoftware.core.insurance.claims.interfaces.dtos.AdjusterAssignmentDTO;
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
 * REST API for adjuster assigned to a claim. The adjuster is a party..
 */
@RestController
@RequestMapping("/api/v1/claims/{claimId}/adjusters")
@RequiredArgsConstructor
@Validated
@Tag(name = "Adjuster Assignments", description = "Adjuster assigned to a claim. The adjuster is a party.")
public class AdjusterAssignmentController {

    private final AdjusterAssignmentService adjusterAssignmentService;

    @PostMapping("/filter")
    @Operation(summary = "Filter adjusterAssignments", description = "Retrieve a page of adjusterAssignments matching the filter")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Page of matching records"),
        @ApiResponse(responseCode = "400", description = "Invalid filter request", content = @Content)
    })
    public Mono<ResponseEntity<PaginationResponse<AdjusterAssignmentDTO>>> filterAdjusterAssignments(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Filter criteria", required = true)
            @Valid @RequestBody FilterRequest<AdjusterAssignmentDTO> filterRequest) {
        return adjusterAssignmentService.filterAdjusterAssignments(claimId, filterRequest)
                .map(ResponseEntity::ok);
    }

    @PostMapping
    @Operation(summary = "Create AdjusterAssignment", description = "Create a AdjusterAssignment")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "AdjusterAssignment created", content = @Content(schema = @Schema(implementation = AdjusterAssignmentDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content)
    })
    public Mono<ResponseEntity<AdjusterAssignmentDTO>> createAdjusterAssignment(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "AdjusterAssignment to create", required = true)
            @Valid @RequestBody AdjusterAssignmentDTO dto) {
        return adjusterAssignmentService.createAdjusterAssignment(claimId, dto)
                .map(created -> ResponseEntity.status(HttpStatus.CREATED).body(created));
    }

    @GetMapping("/{adjusterAssignmentId}")
    @Operation(summary = "Get AdjusterAssignment by id", description = "Retrieve one AdjusterAssignment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "AdjusterAssignment found", content = @Content(schema = @Schema(implementation = AdjusterAssignmentDTO.class))),
        @ApiResponse(responseCode = "404", description = "AdjusterAssignment not found", content = @Content)
    })
    public Mono<ResponseEntity<AdjusterAssignmentDTO>> getAdjusterAssignmentById(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the AdjusterAssignment", required = true)
            @PathVariable UUID adjusterAssignmentId) {
        return adjusterAssignmentService.getAdjusterAssignmentById(claimId, adjusterAssignmentId)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{adjusterAssignmentId}")
    @Operation(summary = "Update AdjusterAssignment", description = "Update an existing AdjusterAssignment")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "AdjusterAssignment updated", content = @Content(schema = @Schema(implementation = AdjusterAssignmentDTO.class))),
        @ApiResponse(responseCode = "400", description = "Invalid data", content = @Content),
        @ApiResponse(responseCode = "404", description = "AdjusterAssignment not found", content = @Content)
    })
    public Mono<ResponseEntity<AdjusterAssignmentDTO>> updateAdjusterAssignment(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the AdjusterAssignment", required = true)
            @PathVariable UUID adjusterAssignmentId,
            @Parameter(description = "Updated AdjusterAssignment", required = true)
            @Valid @RequestBody AdjusterAssignmentDTO dto) {
        return adjusterAssignmentService.updateAdjusterAssignment(claimId, adjusterAssignmentId, dto)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{adjusterAssignmentId}")
    @Operation(summary = "Delete AdjusterAssignment", description = "Delete a AdjusterAssignment")
    @ApiResponses({
        @ApiResponse(responseCode = "204", description = "AdjusterAssignment deleted"),
        @ApiResponse(responseCode = "404", description = "AdjusterAssignment not found", content = @Content)
    })
    public Mono<ResponseEntity<Void>> deleteAdjusterAssignment(
            @Parameter(description = "Unique identifier of the Claim", required = true)
            @PathVariable UUID claimId,
            @Parameter(description = "Unique identifier of the AdjusterAssignment", required = true)
            @PathVariable UUID adjusterAssignmentId) {
        return adjusterAssignmentService.deleteAdjusterAssignment(claimId, adjusterAssignmentId)
                .then(Mono.just(ResponseEntity.noContent().build()));
    }
}
