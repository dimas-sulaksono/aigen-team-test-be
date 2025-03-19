-- Create database
--CREATE DATABASE school_payment;

-- Drop database
--DROP DATABASE school_payment2

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
    nis VARCHAR(17),
    email VARCHAR(255) UNIQUE NOT NULL,
    name VARCHAR(255) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) DEFAULT 'USER',
    image TEXT,
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


-- Seed untuk tabel school_years
INSERT INTO school_years (school_year, start_date, end_date) VALUES
('2023/2024', '2023-07-01', '2024-06-30'),
('2024/2025', '2024-07-01', '2025-06-30');

-- Seed untuk tabel class
INSERT INTO "class" (class_name, school_year_id) VALUES
('Kelas 10 IPA', 1),
('Kelas 11 IPS', 1),
('Kelas 12 IPA', 2);

-- Seed untuk tabel payment_type
INSERT INTO payment_type (payment_type_name) VALUES
('SPP'),
('UTS'),
('UAS'),
('Ekstrakurikuler');

-- Seed untuk tabel students
INSERT INTO students (nis, name, class_id, birthdate, address, phone_number) VALUES
('12345678901', 'Cantika', 1, '2007-05-15', 'Jl. Merdeka No.1', '081234567890'),
('12345678902', 'Cindy', 2, '2006-07-20', 'Jl. Pancasila No.2', '081298765432');

-- Seed untuk tabel users
INSERT INTO users (email, name, password, role) values
('admin@gmail.com', 'Administrator', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'ADMIN');

INSERT INTO users (nis, email, name, password, role) VALUES
('12345678901', 'user1@gmail.com', 'Ahmad', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('12345678902', 'user2@gmail.com', 'Budi', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER');


-- Seed untuk tabel payments
INSERT INTO payments (payment_name, user_id, student_id, payment_type_id, amount, payment_status, description) VALUES
('Pembayaran SPP Juli 2023', 2, 1, 1, 500000.00, 'Paid', 'Pembayaran SPP bulan Juli'),
('Pembayaran UTS Semester 1', 3, 2, 2, 300000.00, 'Pending', 'Pembayaran Ujian Tengah Semester');

-- Menampilkan data yang telah diinput
SELECT * FROM school_years;
SELECT * FROM "class";
SELECT * FROM payment_type;
SELECT * FROM users;
SELECT * FROM students;
SELECT * FROM payments;

INSERT INTO students (nis, name, class_id, birthdate, address, phone_number) VALUES
('10000000001', 'Ahmad Fauzi', 1, '2007-01-15', 'Jl. Mawar No.1', '081234567801'),
('10000000002', 'Budi Santoso', 2, '2006-02-10', 'Jl. Melati No.2', '081234567802'),
('10000000003', 'Citra Dewi', 3, '2007-03-20', 'Jl. Anggrek No.3', '081234567803'),
('10000000004', 'Dian Pratama', 1, '2007-04-05', 'Jl. Kenanga No.4', '081234567804'),
('10000000005', 'Eka Saputra', 2, '2006-05-12', 'Jl. Cempaka No.5', '081234567805'),
('10000000006', 'Fikri Maulana', 3, '2007-06-18', 'Jl. Dahlia No.6', '081234567806'),
('10000000007', 'Gita Sari', 1, '2007-07-25', 'Jl. Teratai No.7', '081234567807'),
('10000000008', 'Hadi Wijaya', 2, '2006-08-14', 'Jl. Flamboyan No.8', '081234567808'),
('10000000009', 'Indah Permata', 3, '2007-09-22', 'Jl. Bougenville No.9', '081234567809'),
('10000000010', 'Joko Susilo', 1, '2007-10-11', 'Jl. Cemara No.10', '081234567810'),
('10000000011', 'Kiki Amelia', 2, '2006-11-09', 'Jl. Mahoni No.11', '081234567811'),
('10000000012', 'Lia Kusuma', 3, '2007-12-15', 'Jl. Jati No.12', '081234567812'),
('10000000013', 'Mira Ayu', 1, '2007-01-30', 'Jl. Pinus No.13', '081234567813'),
('10000000014', 'Nanda Febri', 2, '2006-02-28', 'Jl. Akasia No.14', '081234567814'),
('10000000015', 'Oki Pranata', 3, '2007-03-19', 'Jl. Karet No.15', '081234567815'),
('10000000016', 'Putri Lestari', 1, '2007-04-25', 'Jl. Mangga No.16', '081234567816'),
('10000000017', 'Qori Rizki', 2, '2006-05-14', 'Jl. Rambutan No.17', '081234567817'),
('10000000018', 'Rudi Hartono', 3, '2007-06-20', 'Jl. Durian No.18', '081234567818'),
('10000000019', 'Sari Wulandari', 1, '2007-07-10', 'Jl. Salak No.19', '081234567819'),
('10000000020', 'Taufik Hidayat', 2, '2006-08-15', 'Jl. Alpukat No.20', '081234567820');

INSERT INTO users (nis, email, name, password, role) VALUES
('10000000001', 'ahmad@gmail.com', 'Ahmad Fauzi', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000002', 'budi@gmail.com', 'Budi Santoso', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000003', 'citra@gmail.com', 'Citra Dewi', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000004', 'dian@gmail.com', 'Dian Pratama', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000005', 'eka@gmail.com', 'Eka Saputra', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000006', 'fikri@gmail.com', 'Fikri Maulana', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000007', 'gita@gmail.com', 'Gita Sari', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000008', 'hadi@gmail.com', 'Hadi Wijaya', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000009', 'indah@gmail.com', 'Indah Permata', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000010', 'joko@gmail.com', 'Joko Susilo', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000011', 'kiki@gmail.com', 'Kiki Amelia', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000012', 'lia@gmail.com', 'Lia Kusuma', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000013', 'mira@gmail.com', 'Mira Ayu', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000014', 'nanda@gmail.com', 'Nanda Febri', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000015', 'oki@gmail.com', 'Oki Pranata', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000016', 'putri@gmail.com', 'Putri Lestari', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000017', 'qori@gmail.com', 'Qori Rizki', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000018', 'rudi@gmail.com', 'Rudi Hartono', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000019', 'sari@gmail.com', 'Sari Wulandari', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
('10000000020', 'taufik@gmail.com', 'Taufik Hidayat', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER');

INSERT INTO payments (payment_name, user_id, student_id, payment_type_id, amount, payment_status, description) VALUES
('SPP Januari 2024', 1, 1, 1, 500000.00, 'Paid', 'Pembayaran SPP bulan Januari'),
('SPP Februari 2024', 2, 2, 1, 500000.00, 'Pending', 'Pembayaran SPP bulan Februari'),
('UAS Semester 1', 3, 3, 3, 300000.00, 'Paid', 'Pembayaran Ujian Akhir Semester 1'),
('SPP Maret 2024', 4, 4, 1, 500000.00, 'Overdue', 'Pembayaran SPP bulan Maret'),
('UTS Semester 2', 5, 5, 2, 300000.00, 'Paid', 'Pembayaran Ujian Tengah Semester 2'),
('SPP April 2024', 6, 6, 1, 500000.00, 'Pending', 'Pembayaran SPP bulan April'),
('Ekstrakurikuler Musik', 7, 7, 4, 150000.00, 'Paid', 'Pembayaran kegiatan musik'),
('SPP Mei 2024', 8, 8, 1, 500000.00, 'Pending', 'Pembayaran SPP bulan Mei'),
('SPP Juni 2024', 9, 9, 1, 500000.00, 'Paid', 'Pembayaran SPP bulan Juni'),
('UAS Semester 2', 10, 10, 3, 300000.00, 'Paid', 'Pembayaran Ujian Akhir Semester 2');

SELECT 
    p.payment_id,
    s.name AS student_name,
    s.nis,
    c.class_name,
    sy.school_year,
    pt.payment_type_name,
    p.payment_name,
    p.amount,
    p.payment_status,
    p.created_at,
    p.deleted_at
FROM payments p
JOIN students s ON p.student_id = s.student_id
JOIN "class" c ON s.class_id = c.class_id
JOIN school_years sy ON c.school_year_id = sy.school_year_id
JOIN payment_type pt ON p.payment_type_id = pt.payment_type_id
ORDER BY p.created_at DESC;
