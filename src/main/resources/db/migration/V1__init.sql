CREATE TABLE endereco
(
    id          INT AUTO_INCREMENT PRIMARY KEY,
    rua         VARCHAR(255) NOT NULL,
    numero      VARCHAR(10),
    complemento VARCHAR(255),
    bairro      VARCHAR(255) NOT NULL,
    cidade      VARCHAR(255) NOT NULL,
    estado      VARCHAR(50)  NOT NULL,
    cep         VARCHAR(10)  NOT NULL
);

CREATE TABLE usuario
(
    id                     INT AUTO_INCREMENT PRIMARY KEY,
    cpf                    VARCHAR(14) UNIQUE NOT NULL,
    nome                   VARCHAR(255)       NOT NULL,
    username               varchar(15) UNIQUE NOT NULL,
    password               varchar(100)       NOT NULL,
    data_nascimento        DATE               NOT NULL,
    endereco_id            INT,
    status                 INT                NOT NULL DEFAULT 1,
    data_criacao           DATETIME           NOT NULL,
    usuario_criacao_id     INT,
    data_atualizacao       DATETIME,
    usuario_atualizacao_id INT,
    data_remocao           DATETIME,
    usuario_remocao_id     INT,

    FOREIGN KEY (endereco_id) REFERENCES endereco (id),
    FOREIGN KEY (usuario_criacao_id) REFERENCES usuario (id),
    FOREIGN KEY (usuario_atualizacao_id) REFERENCES usuario (id),
    FOREIGN KEY (usuario_remocao_id) REFERENCES usuario (id)
);
--
-- CREATE TABLE users (
--                        id int NOT NULL AUTO_INCREMENT,
--                        name varchar(40) NOT NULL,
--                        username varchar(15) NOT NULL,
--                        email varchar(40) NOT NULL,
--                        password varchar(100) NOT NULL,
--                        PRIMARY KEY (id),
--                        UNIQUE KEY uk_users_username (username),
--                        UNIQUE KEY uk_users_email (email)
-- ) DEFAULT CHARSET=utf8;


CREATE TABLE roles
(
    id   int NOT NULL AUTO_INCREMENT,
    name varchar(60) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_roles_name (name)
) AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;


CREATE TABLE user_roles
(
    user_id int NOT NULL,
    role_id int NOT NULL,
    PRIMARY KEY (user_id, role_id),
    KEY     fk_user_roles_role_id (role_id),
    CONSTRAINT fk_user_roles_role_id FOREIGN KEY (role_id) REFERENCES roles (id),
    CONSTRAINT fk_user_roles_user_id FOREIGN KEY (user_id) REFERENCES usuario (id)
) DEFAULT CHARSET=utf8;
