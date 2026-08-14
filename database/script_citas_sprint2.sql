USE HospitalDB;
GO

-- =========================================================
-- PASO 1: LIMPIEZA DE TABLAS Y CONSTRAINTS DEL SPRINT 2
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

-- 4. Tabla Horarios
CREATE TABLE Horarios (
    id_horario INT IDENTITY(1,1) PRIMARY KEY,
    id_medico INT NOT NULL,
    id_consultorio INT NOT NULL,
    dia_semana VARCHAR(15) NOT NULL
        CHECK (dia_semana IN ('Lunes','Martes','Miercoles','Jueves','Viernes','Sabado','Domingo')),
    hora_inicio TIME NOT NULL,
    hora_fin TIME NOT NULL,
    disponible BIT NOT NULL DEFAULT 1,
    CONSTRAINT FK_Horarios_Medicos FOREIGN KEY (id_medico) REFERENCES Medicos(id_medico),
    CONSTRAINT FK_Horarios_Consultorios FOREIGN KEY (id_consultorio) REFERENCES Consultorios(id_consultorio),
    CONSTRAINT CK_Horarios_HoraValida CHECK (hora_fin > hora_inicio)
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
    CONSTRAINT FK_Citas_Pacientes FOREIGN KEY (id_paciente) REFERENCES Pacientes(id_paciente),
    CONSTRAINT FK_Citas_Medicos FOREIGN KEY (id_medico) REFERENCES Medicos(id_medico),
    CONSTRAINT FK_Citas_Consultorios FOREIGN KEY (id_consultorio) REFERENCES Consultorios(id_consultorio),
    CONSTRAINT FK_Citas_Horarios FOREIGN KEY (id_horario) REFERENCES Horarios(id_horario)
);
GO

-- =========================================================
-- PASO 3: DATOS INICIALES Y MÉDICOS DE PRUEBA
-- =========================================================

-- Especialidades
INSERT INTO Especialidades (nombre, descripcion) VALUES
('Medicina General', 'Atención médica primaria y preventiva'),
('Cardiologia', 'Atención de enfermedades del corazón'),
('Gastroenterologia', 'Atención de afecciones digestivas'),
('Traumatologia y Ortopedia', 'Atención de lesiones musculares y óseas');

-- Consultorios
INSERT INTO Consultorios (numero, piso, ubicacion, estado) VALUES
('101', '1', 'Ala Norte', 'Disponible'),
('102', '1', 'Ala Norte', 'Disponible'),
('205', '2', 'Ala Sur', 'Disponible');

-- Actualizar contraseñas BCrypt de usuarios de prueba ('123456')
UPDATE Usuarios 
SET password_hash = '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', activo = 1
WHERE correo IN ('juan@gmail.com', 'miguel@gmail.com');

-- Crear un Médico de prueba si no existe
IF NOT EXISTS (SELECT * FROM Usuarios WHERE correo = 'medico1@hospital.com')
BEGIN
    INSERT INTO Usuarios (correo, password_hash, id_rol, activo)
    VALUES ('medico1@hospital.com', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuVGkqRzgVymGe07xd00DMxs.AQubh4a', 2, 1);
    
    DECLARE @id_user_medico INT = SCOPE_IDENTITY();
    
    INSERT INTO Medicos (id_usuario, nombres, apellidos, especialidad, colegiatura, telefono, id_especialidad)
    VALUES (@id_user_medico, 'Carlos', 'Mendoza', 'Medicina General', 'CMP-45892', '987654321', 1);
END;

-- Crear Horario disponible para el Médico 1
IF NOT EXISTS (SELECT * FROM Horarios)
BEGIN
    INSERT INTO Horarios (id_medico, id_consultorio, dia_semana, hora_inicio, hora_fin, disponible)
    VALUES (1, 1, 'Lunes', '08:00:00', '12:00:00');
END;
GO
