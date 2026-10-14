-- =====================================================================
-- HelpDesk Academico - criacao do banco de dados (MySQL 8+)
-- Execute com:  mysql -u root -p < sql/schema.sql
-- ATENCAO: recria o banco do zero (DROP DATABASE) - apaga dados existentes.
-- =====================================================================

SET NAMES utf8mb4;

DROP DATABASE IF EXISTS helpdesk;
CREATE DATABASE helpdesk
    CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;
USE helpdesk;

-- ---------------------------------------------------------------------
-- usuarios  (UsuarioDAO / model.Usuario)
-- ---------------------------------------------------------------------
CREATE TABLE usuarios (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    nome          VARCHAR(100) NOT NULL,
    email         VARCHAR(150) NOT NULL UNIQUE,
    senha         VARCHAR(255) NOT NULL,          -- hoje em texto puro (ver UsuarioDAO.logar)
    perfil        ENUM('ADMIN','TECNICO','SOLICITANTE') NOT NULL DEFAULT 'SOLICITANTE',
    ativo         BOOLEAN      NOT NULL DEFAULT TRUE,
    data_cadastro DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- categorias  (CategoriaDAO / model.Categorias)
-- ---------------------------------------------------------------------
CREATE TABLE categorias (
    id        INT AUTO_INCREMENT PRIMARY KEY,
    nome      VARCHAR(100) NOT NULL,
    descricao VARCHAR(255),
    ativo     BOOLEAN      NOT NULL DEFAULT TRUE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- solicitacoes  (model.Solicitacoes - DAO/telas ainda nao implementados)
-- ---------------------------------------------------------------------
CREATE TABLE solicitacoes (
    id               INT AUTO_INCREMENT PRIMARY KEY,
    titulo           VARCHAR(150) NOT NULL,
    descricao        TEXT         NOT NULL,
    local_ocorrencia VARCHAR(150),
    prioridade       ENUM('BAIXA','MEDIA','ALTA','URGENTE') NOT NULL DEFAULT 'MEDIA',
    status           ENUM('ABERTA','EM_ATENDIMENTO','RESOLVIDA','FECHADA','CANCELADA')
                     NOT NULL DEFAULT 'ABERTA',
    data_abertura    DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    data_fechamento  DATETIME     NULL,
    usuario_id       INT NOT NULL,                -- quem abriu
    tecnico_id       INT NULL,                    -- quem atende (nulo ate ser atribuido)
    categoria_id     INT NOT NULL,
    CONSTRAINT fk_solic_usuario   FOREIGN KEY (usuario_id)   REFERENCES usuarios(id),
    CONSTRAINT fk_solic_tecnico   FOREIGN KEY (tecnico_id)   REFERENCES usuarios(id),
    CONSTRAINT fk_solic_categoria FOREIGN KEY (categoria_id) REFERENCES categorias(id),
    INDEX idx_solic_status  (status),
    INDEX idx_solic_usuario (usuario_id),
    INDEX idx_solic_tecnico (tecnico_id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- interacoes  (model.Interacoes - DAO/telas ainda nao implementados)
-- ---------------------------------------------------------------------
CREATE TABLE interacoes (
    id             INT AUTO_INCREMENT PRIMARY KEY,
    mensagem       TEXT     NOT NULL,
    data_interacao DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    solicitacao_id INT NOT NULL,
    usuario_id     INT NOT NULL,
    CONSTRAINT fk_inter_solicitacao FOREIGN KEY (solicitacao_id)
        REFERENCES solicitacoes(id) ON DELETE CASCADE,
    CONSTRAINT fk_inter_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
    INDEX idx_inter_solicitacao (solicitacao_id)
) ENGINE=InnoDB;

-- =====================================================================
-- DADOS DE TESTE
-- (datas gravadas com UTC_TIMESTAMP() porque a conexao do projeto usa serverTimezone=UTC;
--  assim elas aparecem na hora certa nas telas)
-- Logins (senha em texto puro, como o sistema atual espera):
--   admin@helpdesk.com   / admin123   (ADMIN)
--   tecnico@helpdesk.com / tecnico123 (TECNICO)
--   aluno@helpdesk.com   / aluno123   (SOLICITANTE)
-- =====================================================================
INSERT INTO usuarios (nome, email, senha, perfil) VALUES
    ('Admin HelpDesk',  'admin@helpdesk.com',   'admin123',   'ADMIN'),
    ('Carlos Técnico', 'tecnico@helpdesk.com', 'tecnico123', 'TECNICO'),
    ('Ana Aluna',      'aluno@helpdesk.com',   'aluno123',   'SOLICITANTE');

INSERT INTO categorias (nome, descricao, ativo) VALUES
    ('Hardware',      'Computadores, monitores, impressoras e periféricos', TRUE),
    ('Software',      'Instalação e erros em programas',                    TRUE),
    ('Rede / Wi-Fi',  'Internet, Wi-Fi e acesso à rede',                    TRUE),
    ('Projetor / Multimídia', 'Equipamentos de sala de aula',               TRUE),
    ('Legado',        'Categoria antiga, mantida inativa',                  FALSE);

INSERT INTO solicitacoes
    (titulo, descricao, local_ocorrencia, prioridade, status, data_abertura, data_fechamento, usuario_id, tecnico_id, categoria_id)
VALUES
    ('Projetor não liga', 'O projetor da sala 204 não acende ao ligar.', 'Sala 204', 'ALTA', 'EM_ATENDIMENTO',
        UTC_TIMESTAMP() - INTERVAL 2 DAY, NULL, 3, 2, 4),
    ('Wi-Fi caindo no laboratório', 'A conexão cai a cada poucos minutos.', 'Laboratório 3', 'MEDIA', 'ABERTA',
        UTC_TIMESTAMP() - INTERVAL 1 DAY, NULL, 3, NULL, 3),
    ('Instalar o Eclipse', 'Preciso do Eclipse IDE instalado no PC 12.', 'Laboratório 1', 'BAIXA', 'RESOLVIDA',
        UTC_TIMESTAMP() - INTERVAL 7 DAY, UTC_TIMESTAMP() - INTERVAL 6 DAY, 3, 2, 2);

INSERT INTO interacoes (mensagem, data_interacao, solicitacao_id, usuario_id) VALUES
    ('Chamado recebido, vou verificar a lâmpada do projetor.', UTC_TIMESTAMP() - INTERVAL 2 DAY, 1, 2),
    ('Obrigada! Fico no aguardo.',                             UTC_TIMESTAMP() - INTERVAL 2 DAY, 1, 3),
    ('Eclipse instalado e testado.',                           UTC_TIMESTAMP() - INTERVAL 6 DAY, 3, 2);
