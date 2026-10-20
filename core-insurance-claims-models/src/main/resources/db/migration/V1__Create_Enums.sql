-- Flyway V1: enum types for core-insurance-claims

CREATE TYPE claim_status_enum AS ENUM (
    'NOTIFIED',
    'OPEN',
    'UNDER_ASSESSMENT',
    'RESERVED',
    'SETTLED',
    'CLOSED',
    'REOPENED',
    'DENIED'
);

CREATE TYPE claim_party_role_enum AS ENUM (
    'CLAIMANT',
    'INSURED',
    'PAYEE',
    'WITNESS',
    'BROKER'
);

CREATE TYPE reserve_type_enum AS ENUM (
    'INDEMNITY',
    'EXPENSE',
    'RECOVERY'
);

CREATE TYPE assessment_outcome_enum AS ENUM (
    'COVERED',
    'PARTIAL',
    'NOT_COVERED',
    'PENDING'
);

CREATE TYPE claim_evidence_type_enum AS ENUM (
    'FNOL',
    'PHOTO',
    'INVOICE',
    'REPORT',
    'CORRESPONDENCE',
    'OTHER'
);

CREATE TYPE settlement_status_enum AS ENUM (
    'PROPOSED',
    'AUTHORIZED',
    'PAID',
    'REJECTED'
);

CREATE TYPE coverage_check_outcome_enum AS ENUM (
    'CONFIRMED',
    'NOT_IN_FORCE',
    'NOT_TRIGGERED',
    'EXCLUDED',
    'PENDING'
);

CREATE TYPE triage_path_enum AS ENUM (
    'FAST_TRACK',
    'COMPLEX',
    'SIU'
);

CREATE TYPE assignment_status_enum AS ENUM (
    'ASSIGNED',
    'COMPLETED',
    'CANCELLED'
);

CREATE TYPE indicator_severity_enum AS ENUM (
    'LOW',
    'MEDIUM',
    'HIGH'
);

CREATE TYPE siu_referral_status_enum AS ENUM (
    'OPEN',
    'RETURNED',
    'CONFIRMED',
    'CLEARED'
);

CREATE TYPE recovery_type_enum AS ENUM (
    'SUBROGATION',
    'SALVAGE',
    'CONTRIBUTION'
);

CREATE TYPE recovery_status_enum AS ENUM (
    'OPEN',
    'PURSUING',
    'RECOVERED',
    'CLOSED',
    'WRITTEN_OFF'
);

