CREATE DATABASE board_db;

USE board_db;

CREATE TABLE board (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL
);

CREATE TABLE coluna (
    id INT AUTO_INCREMENT PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    tipo ENUM('INICIAL', 'PENDENTE', 'FINAL', 'CANCELAMENTO') NOT NULL,
    ordem INT NOT NULL,
    board_id INT NOT NULL,
    FOREIGN KEY (board_id) REFERENCES board(id)
);

CREATE TABLE card (
    id INT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(100) NOT NULL,
    descricao TEXT,
    bloqueado BOOLEAN DEFAULT FALSE,
    motivo_bloqueio TEXT,
    motivo_desbloqueio TEXT,
    data_criacao DATETIME DEFAULT CURRENT_TIMESTAMP,
    data_movimentacao DATETIME,
    coluna_id INT NOT NULL,
    FOREIGN KEY (coluna_id) REFERENCES coluna(id)
);
