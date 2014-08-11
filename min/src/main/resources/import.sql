insert into Pessoa (nome) values ('admin');
insert into Usuario (login, senha, role, pessoa_id) values ('admin', 'a/i6fIVQLLvYGWxCm1Ag5RTkBXPW6aKdDuk5rAekGVo=', 'ADMIN',  (select id from pessoa) );