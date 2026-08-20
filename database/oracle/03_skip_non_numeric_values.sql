-- Use this predicate in the source cursor so Select/alpha values are skipped.
WHERE REGEXP_LIKE(TRIM(REAL_ESTATE_LABLE_CODE), '^[0-9]+([.][0-9]+)?$')
  AND REGEXP_LIKE(TRIM(TECH_WRITTEN_OFF_STS),  '^[0-9]+([.][0-9]+)?$')
  AND REGEXP_LIKE(TRIM(RESTRUC_ACC_STS),       '^[0-9]+([.][0-9]+)?$')

-- If the rows must remain but invalid individual fields should become NULL,
-- use these CASE expressions in the SELECT list instead.
CASE WHEN REGEXP_LIKE(TRIM(REAL_ESTATE_LABLE_CODE), '^[0-9]+([.][0-9]+)?$')
     THEN TO_NUMBER(TRIM(REAL_ESTATE_LABLE_CODE)) END AS REAL_ESTATE_LABLE_CODE,
CASE WHEN REGEXP_LIKE(TRIM(TECH_WRITTEN_OFF_STS), '^[0-9]+([.][0-9]+)?$')
     THEN TO_NUMBER(TRIM(TECH_WRITTEN_OFF_STS)) END AS TECH_WRITTEN_OFF_STS,
CASE WHEN REGEXP_LIKE(TRIM(RESTRUC_ACC_STS), '^[0-9]+([.][0-9]+)?$')
     THEN TO_NUMBER(TRIM(RESTRUC_ACC_STS)) END AS RESTRUC_ACC_STS
