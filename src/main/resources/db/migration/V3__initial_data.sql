INSERT INTO endereco (id, rua, numero, complemento, bairro, cidade, estado, cep) VALUES (1, 'Rua Exemplo', '123', 'Complemento Exemplo', 'Bairro Exemplo', 'Cidade Exemplo', 'Estado Exemplo', '12345-678');
INSERT INTO usuario (id, cpf, nome, username, password, data_nascimento, endereco_id, status, data_criacao,
                     usuario_criacao_id, data_atualizacao, usuario_atualizacao_id, data_remocao, usuario_remocao_id)
VALUES (1, '123.456.789-01', 'Nome do Usuário', 'user-root',
        '$2a$10$il.8AIsPkwPbwoz7f735ce/4sjnCe/rHHtfPQ3tS9HKThkbxdd/9e', '2000-01-01', 1, 0, '2024-01-15 13:26:27', null,
        null, null, null, null);

INSERT INTO user_roles(user_id, role_id) values (1, 5);