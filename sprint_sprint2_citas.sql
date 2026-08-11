USE HospitalDB;
GO

-- =========================================================
-- PASO 1: LIMPIEZA DE TABLAS PREVIAS DEL SPRINT 2
-- =========================================================
IF OBJECT_ID('dbo.Citas', 'U') IS NOT NULL DROP TABLE dbo.Citas;
IF OBJECT_ID('dbo.Horarios', 'U') IS NOT NULL DROP TABLE dbo.Horarios;
IF OBJECT_ID('dbo.Consultorios', 'U') IS NOT NULL DROP TABLE dbo.Consultorios;

IF EXISTS (SELECT * FROM sys.foreign_keys WHERE name = 'FK_Medicos_Especialidades')
BEGIN
    ALTER TABLE dbo.Medicos DROP CONSTRAINT FK_Medicos_Especialidades;
END

IF OBJECT_ID('dbo.Especialidades', 'U') IS NOT NULL DROP TABLE dbo.Especialidades;
GO

-- =========================================================
-- PASO 2: CREACIÓN DE TABLAS DEL SPRINT 2
-- =========================================================

-- 1. Tabla Especialidades
CREATE TABLE Especialidades (
    id_especialidad INT IDENTITY(1,1) PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL UNIQUE,
    descripcion VARCHAR(255) NULL
);
GO

-- 2. Tabla Consultorios
CREATE TABLE Consultorios (
    id_consultorio INT IDENTITY(1,1) PRIMARY KEY,
    numero VARCHAR(10) NOT NULL UNIQUE,
    piso VARCHAR(10) NULL,
    ubicacion VARCHAR(100) NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'Disponible'
        CHECK (estado IN ('Disponible', 'Ocupado', 'Mantenimiento'))
);
GO

-- 3. Vincular Especialidad a Médicos
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('Medicos') AND name = 'id_especialidad')
BEGIN
    ALTER TABLE Medicos ADD id_especialidad INT NULL;
END
GO

ALTER TABLE Medicos ADD CONSTRAINT FK_Medicos_Especialidades 
    FOREIGN KEY (id_especialidad) REFERENCES Especialidades(id_especialidad);
GO

-- 4. Tabla Horarios (Referenciando correctamente IdMedico/id_medico)
CREATE TABLE Horarios (
    id_horario INT IDENTITY(1,1) PRIMARY KEY,
    id_medico INT NOT NULL,
    id_consultorio INT NOT NULL,
    dia_semana VARCHAR(15) NOT NULL
        CHECK (dia_semana IN ('Lunes','Martes','Miercoles','Jueves','Viernes','Sabado','Domingo')),
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    disponible BIT NOT NULL DEFAULT 1,
    CONSTRAINT FK_Horarios_Consultorios
        FOREIGN KEY (id_consultorio) REFERENCES Consultorios(id_consultorio),
    CONSTRAINT CK_Horarios_HoraValida
        CHECK (hora_fin > hora_inicio)
);
GO

-- 5. Tabla Citas
CREATE TABLE Citas (
    id_cita INT IDENTITY(1,1) PRIMARY KEY,
    id_paciente INT NOT NULL,
    id_medico INT NOT NULL,
    id_consultorio INT NOT NULL,
    id_horario INT NOT NULL,
    fecha DATE NOT NULL,
    hora_cita TIME NOT NULL,
    estado VARCHAR(20) NOT NULL DEFAULT 'Pendiente'
        CHECK (estado IN ('Pendiente','Confirmada','Cancelada','Atendida')),
    observaciones VARCHAR(255) NULL,
    CONSTRAINT FK_Citas_Consultorios
        FOREIGN KEY (id_consultorio) REFERENCES Consultorios(id_consultorio),
    CONSTRAINT FK_Citas_Horarios
        FOREIGN KEY (id_horario) REFERENCES Horarios(id_horario)
);
GO

-- =========================================================
-- PASO 3: DATOS INICIALES DE PRUEBA
-- =========================================================

INSERT INTO Especialidades (nombre, descripcion) VALUES
('Cardiologia', 'Atencion de enfermedades del corazon'),
('Pediatria', 'Atencion medica para niños'),
('Dermatologia', 'Atencion de enfermedades de la piel');

INSERT INTO Consultorios (numero, piso, ubicacion, estado) VALUES
('101', '1', 'Ala Norte', 'Disponible'),
('205', '2', 'Ala Sur', 'Disponible');
GO
