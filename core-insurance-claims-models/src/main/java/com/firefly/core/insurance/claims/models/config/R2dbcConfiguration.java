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


package com.firefly.core.insurance.claims.models.config;

import com.firefly.core.insurance.claims.interfaces.enums.ClaimStatus;
import com.firefly.core.insurance.claims.interfaces.enums.ClaimPartyRole;
import com.firefly.core.insurance.claims.interfaces.enums.ReserveType;
import com.firefly.core.insurance.claims.interfaces.enums.AssessmentOutcome;
import com.firefly.core.insurance.claims.interfaces.enums.ClaimEvidenceType;
import com.firefly.core.insurance.claims.interfaces.enums.SettlementStatus;
import com.firefly.core.insurance.claims.interfaces.enums.CoverageCheckOutcome;
import com.firefly.core.insurance.claims.interfaces.enums.TriagePath;
import com.firefly.core.insurance.claims.interfaces.enums.AssignmentStatus;
import com.firefly.core.insurance.claims.interfaces.enums.IndicatorSeverity;
import com.firefly.core.insurance.claims.interfaces.enums.SiuReferralStatus;
import com.firefly.core.insurance.claims.interfaces.enums.RecoveryType;
import com.firefly.core.insurance.claims.interfaces.enums.RecoveryStatus;
import io.r2dbc.postgresql.PostgresqlConnectionConfiguration;
import io.r2dbc.postgresql.PostgresqlConnectionFactory;
import io.r2dbc.postgresql.client.SSLMode;
import io.r2dbc.postgresql.codec.EnumCodec;
import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.r2dbc.config.AbstractR2dbcConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * Registers every PostgreSQL enum used by this service with the R2DBC codec.
 * Without this, the driver binds enum parameters as varchar and Postgres rejects the cast.
 */
@Configuration
public class R2dbcConfiguration extends AbstractR2dbcConfiguration {

    @Value("${DB_HOST:localhost}")
    private String host;

    @Value("${DB_PORT:5432}")
    private int port;

    @Value("${DB_NAME:postgres}")
    private String database;

    @Value("${DB_USERNAME:postgres}")
    private String username;

    @Value("${DB_PASSWORD:postgres}")
    private String password;

    @Value("${DB_SSL_MODE:disable}")
    private String sslMode;

    @WritingConverter
    static class ClaimStatusConverter implements Converter<ClaimStatus, ClaimStatus> {
        @Override
        public ClaimStatus convert(ClaimStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class ClaimPartyRoleConverter implements Converter<ClaimPartyRole, ClaimPartyRole> {
        @Override
        public ClaimPartyRole convert(ClaimPartyRole source) {
            return source;
        }
    }

    @WritingConverter
    static class ReserveTypeConverter implements Converter<ReserveType, ReserveType> {
        @Override
        public ReserveType convert(ReserveType source) {
            return source;
        }
    }

    @WritingConverter
    static class AssessmentOutcomeConverter implements Converter<AssessmentOutcome, AssessmentOutcome> {
        @Override
        public AssessmentOutcome convert(AssessmentOutcome source) {
            return source;
        }
    }

    @WritingConverter
    static class ClaimEvidenceTypeConverter implements Converter<ClaimEvidenceType, ClaimEvidenceType> {
        @Override
        public ClaimEvidenceType convert(ClaimEvidenceType source) {
            return source;
        }
    }

    @WritingConverter
    static class SettlementStatusConverter implements Converter<SettlementStatus, SettlementStatus> {
        @Override
        public SettlementStatus convert(SettlementStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class CoverageCheckOutcomeConverter implements Converter<CoverageCheckOutcome, CoverageCheckOutcome> {
        @Override
        public CoverageCheckOutcome convert(CoverageCheckOutcome source) {
            return source;
        }
    }

    @WritingConverter
    static class TriagePathConverter implements Converter<TriagePath, TriagePath> {
        @Override
        public TriagePath convert(TriagePath source) {
            return source;
        }
    }

    @WritingConverter
    static class AssignmentStatusConverter implements Converter<AssignmentStatus, AssignmentStatus> {
        @Override
        public AssignmentStatus convert(AssignmentStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class IndicatorSeverityConverter implements Converter<IndicatorSeverity, IndicatorSeverity> {
        @Override
        public IndicatorSeverity convert(IndicatorSeverity source) {
            return source;
        }
    }

    @WritingConverter
    static class SiuReferralStatusConverter implements Converter<SiuReferralStatus, SiuReferralStatus> {
        @Override
        public SiuReferralStatus convert(SiuReferralStatus source) {
            return source;
        }
    }

    @WritingConverter
    static class RecoveryTypeConverter implements Converter<RecoveryType, RecoveryType> {
        @Override
        public RecoveryType convert(RecoveryType source) {
            return source;
        }
    }

    @WritingConverter
    static class RecoveryStatusConverter implements Converter<RecoveryStatus, RecoveryStatus> {
        @Override
        public RecoveryStatus convert(RecoveryStatus source) {
            return source;
        }
    }

    @Override
    protected List<Object> getCustomConverters() {
        List<Object> converters = new ArrayList<>();
        converters.add(new ClaimStatusConverter());
        converters.add(new ClaimPartyRoleConverter());
        converters.add(new ReserveTypeConverter());
        converters.add(new AssessmentOutcomeConverter());
        converters.add(new ClaimEvidenceTypeConverter());
        converters.add(new SettlementStatusConverter());
        converters.add(new CoverageCheckOutcomeConverter());
        converters.add(new TriagePathConverter());
        converters.add(new AssignmentStatusConverter());
        converters.add(new IndicatorSeverityConverter());
        converters.add(new SiuReferralStatusConverter());
        converters.add(new RecoveryTypeConverter());
        converters.add(new RecoveryStatusConverter());
        return converters;
    }

    @Bean
    @Primary
    @Override
    public ConnectionFactory connectionFactory() {
        return new PostgresqlConnectionFactory(
                PostgresqlConnectionConfiguration.builder()
                        .host(host)
                        .port(port)
                        .username(username)
                        .password(password)
                        .database(database)
                        .sslMode(SSLMode.valueOf(sslMode.toUpperCase()))
                        .codecRegistrar(EnumCodec.builder()
                                .withEnum("claim_status_enum", ClaimStatus.class)
                                .withEnum("claim_party_role_enum", ClaimPartyRole.class)
                                .withEnum("reserve_type_enum", ReserveType.class)
                                .withEnum("assessment_outcome_enum", AssessmentOutcome.class)
                                .withEnum("claim_evidence_type_enum", ClaimEvidenceType.class)
                                .withEnum("settlement_status_enum", SettlementStatus.class)
                                .withEnum("coverage_check_outcome_enum", CoverageCheckOutcome.class)
                                .withEnum("triage_path_enum", TriagePath.class)
                                .withEnum("assignment_status_enum", AssignmentStatus.class)
                                .withEnum("indicator_severity_enum", IndicatorSeverity.class)
                                .withEnum("siu_referral_status_enum", SiuReferralStatus.class)
                                .withEnum("recovery_type_enum", RecoveryType.class)
                                .withEnum("recovery_status_enum", RecoveryStatus.class)
                                .build())
                        .build()
        );
    }
}
