insert into Pessoa (nome) values ('admin');
insert into Usuario (login, senha, role, pessoa_id) values ('admin', 'a/i6fIVQLLvYGWxCm1Ag5RTkBXPW6aKdDuk5rAekGVo=', 'ADMIN',  (select id from pessoa) );

insert into tiposervico (nome) values ('Cabelo');
insert into tipoproduto (nome, tiposervico_id) values ('Qu�mica', (select id from tiposervico where nome = 'Cabelo'));
insert into tipoproduto (nome, tiposervico_id) values ('Tratamento', (select id from tiposervico where nome = 'Cabelo'));
insert into tipoproduto (nome, tiposervico_id) values ('Outros', (select id from tiposervico where nome = 'Cabelo'));

insert into tiposervico (nome) values ('Unha');
insert into tipoproduto (nome, tiposervico_id) values ('Esmalte', (select id from tiposervico where nome = 'Unha'));
insert into tipoproduto (nome, tiposervico_id) values ('Esmalte Tahe', (select id from tiposervico where nome = 'Unha'));
insert into tipoproduto (nome, tiposervico_id) values ('Longa dura��o', (select id from tiposervico where nome = 'Unha'));
insert into tipoproduto (nome, tiposervico_id) values ('Outros', (select id from tiposervico where nome = 'Unha'));

insert into tiposervico (nome) values ('Maquiagem');
insert into tipoproduto (nome, tiposervico_id) values ('Maquiagem', (select id from tiposervico where nome = 'Maquiagem'));
insert into tipoproduto (nome, tiposervico_id) values ('Est�tica facial', (select id from tiposervico where nome = 'Maquiagem'));
insert into tipoproduto (nome, tiposervico_id) values ('Est�tica corporal', (select id from tiposervico where nome = 'Maquiagem'));
insert into tipoproduto (nome, tiposervico_id) values ('Outros', (select id from tiposervico where nome = 'Maquiagem'));

insert into tiposervico (nome) values ('Est�tica');
insert into tipoproduto (nome, tiposervico_id) values ('Outros', (select id from tiposervico where nome = 'Est�tica'));

insert into tiposervico (nome) values ('Outros');
insert into tipoproduto (nome, tiposervico_id) values ('Outros', (select id from tiposervico where nome = 'Outros'));