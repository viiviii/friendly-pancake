---- contents 테이블의 url 데이터를 playbacks 테이블로 이동
insert into playbacks (content_id, url)
select id, url from contents;

-- platform 컬럼 값이 없으므로 업데이트
update playbacks
set platform = 'NETFLIX'
where platform is null;

-- contents 테이블에서 url 컬럼 제거
alter table contents drop column url;