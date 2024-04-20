-- 기존 date 타입 컬럼 삭제
ALTER TABLE settings DROP COLUMN disable_at;

-- 새로운 timestamp 타입 컬럼 추가
ALTER TABLE settings ADD COLUMN disable_at timestamp with time zone;