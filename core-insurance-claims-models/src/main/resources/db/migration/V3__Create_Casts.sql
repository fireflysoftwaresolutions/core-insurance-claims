-- Flyway V3: implicit varchar casts for enum parameters

CREATE CAST (varchar AS claim_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS claim_party_role_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS reserve_type_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS assessment_outcome_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS claim_evidence_type_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS settlement_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS coverage_check_outcome_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS triage_path_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS assignment_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS indicator_severity_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS siu_referral_status_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS recovery_type_enum) WITH INOUT AS IMPLICIT;
CREATE CAST (varchar AS recovery_status_enum) WITH INOUT AS IMPLICIT;
