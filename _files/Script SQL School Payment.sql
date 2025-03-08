-- Create database
CREATE DATABASE school_payment;

\c school_payment;

-- Tabel School Years
CREATE TABLE school_years (
    school_year_id SERIAL PRIMARY KEY,
    school_year VARCHAR(9) NOT NULL,  -- Format contoh: "2024/2025"
    start_date DATE NOT NULL,
    end_date DATE NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Index untuk pencarian cepat berdasarkan tahun ajaran
CREATE INDEX idx_school_years_year ON school_years (school_year);


-- Tabel Class
CREATE TABLE "class" (
    class_id SERIAL PRIMARY KEY,
    class_name VARCHAR(100) NOT NULL,
    school_year_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,

    -- Foreign key untuk menghubungkan dengan school_years
    CONSTRAINT fk_class_school_year FOREIGN KEY (school_year_id) 
    REFERENCES school_years(school_year_id) ON DELETE CASCADE
);

-- Index untuk pencarian cepat berdasarkan nama kelas
CREATE INDEX idx_class_name ON "class" (class_name);


-- Tabel Payment Type
CREATE TABLE payment_type (
    payment_type_id SERIAL PRIMARY KEY,
    payment_type_name VARCHAR(255) NOT NULL UNIQUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);

-- Index untuk pencarian cepat berdasarkan nama tipe pembayaran
CREATE INDEX idx_payment_type_name ON payment_type (payment_type_name);


-- Tabel Users
CREATE TABLE users (
    user_id SERIAL PRIMARY KEY,
    nis VARCHAR(17) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL
);


-- Tabel Payments
CREATE TABLE payments (
    payment_id SERIAL PRIMARY KEY,
    payment_name VARCHAR(255) NOT NULL,
    user_id INT NOT NULL,
    student_id INT NOT NULL,
    payment_type_id INT NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_status VARCHAR(50) NOT NULL,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,

    -- Foreign Key Constraints
    CONSTRAINT fk_payments_users FOREIGN KEY (user_id) 
        REFERENCES users(user_id) ON DELETE CASCADE,
    CONSTRAINT fk_payments_payment_type FOREIGN KEY (payment_type_id) 
        REFERENCES payment_type(payment_type_id) ON DELETE CASCADE
);

-- Index untuk mempercepat pencarian berdasarkan status pembayaran
CREATE INDEX idx_payment_status ON payments (payment_status);
CREATE INDEX idx_payment_name ON payments (payment_name);


-- Tabel Students
CREATE TABLE students (
    student_id SERIAL PRIMARY KEY,
    nis VARCHAR(17) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    class_id INT,
    birthdate DATE,
    address TEXT,
    phone_number VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    deleted_at TIMESTAMP NULL,

    CONSTRAINT fk_students_class FOREIGN KEY (class_id) 
    REFERENCES "class"(class_id) ON DELETE SET NULL
);


-- Tambahkan Foreign Key setelah tabel selesai dibuat
ALTER TABLE users 
    ADD CONSTRAINT fk_users_students FOREIGN KEY (nis) 
    REFERENCES students(nis) ON DELETE SET NULL;

ALTER TABLE payments
    ADD CONSTRAINT fk_payments_students FOREIGN KEY (student_id) 
    REFERENCES students(student_id) ON DELETE SET NULL;


-- Meningkatkan performa pencarian dan join berdasarkan foreign key
CREATE INDEX idx_students_class_id ON students (class_id);
CREATE INDEX idx_payments_user_id ON payments (user_id);
CREATE INDEX idx_payments_student_id ON payments (student_id);
CREATE INDEX idx_payments_payment_type_id ON payments (payment_type_id);
