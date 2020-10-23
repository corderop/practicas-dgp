-- LISTA DE TABLAS -- 
CREATE TABLE USUARIO(
    cod_usuario int AUTO_INCREMENT primary key,
    nombre varchar(200) not null,
    pass varchar(200) not null,
    tipo varchar(200) not null
);

--tipo puede tomar valores [USER, TUTOR, ADMIN]

CREATE TABLE TAREA(
    cod_tarea int AUTO_INCREMENT PRIMARY KEY,
    titulo varchar(200) not null,
    descripcion text,
    fecha_limite date not null,
    objetivo varchar(200),
    multimedia varchar(200),
    crea int,
    realiza int,
    realizada boolean default false,
    FOREIGN KEY (crea) REFERENCES USUARIO(cod_usuario),
    FOREIGN KEY (realiza) REFERENCES USUARIO(cod_usuario)
);

CREATE TABLE GRUPO_TRABAJO(
    cod_grupo int AUTO_INCREMENT PRIMARY KEY,
    nombre varchar(200) not null,
    confecciona int,
    FOREIGN KEY (confecciona) REFERENCES USUARIO(cod_usuario)
);

CREATE TABLE INTEGRADO(
    cod_grupo int,
    cod_usuario int,
    FOREIGN KEY (cod_grupo) REFERENCES GRUPO_TRABAJO(cod_grupo),
    FOREIGN KEY (cod_usuario) REFERENCES USUARIO(cod_usuario),
    PRIMARY KEY (cod_grupo, cod_usuario)
);

CREATE TABLE MENSAJE(
    cod_mensaje int AUTO_INCREMENT PRIMARY KEY,
    fecha date not null,
    contenido text,
    multimedia varchar(200),
    contiene int,
    envia int,
    FOREIGN KEY (contiene) REFERENCES TAREA(cod_tarea),
    FOREIGN KEY (envia) REFERENCES USUARIO(cod_usuario)
);




