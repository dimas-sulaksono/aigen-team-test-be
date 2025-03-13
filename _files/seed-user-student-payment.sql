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
    p.created_at
FROM payments p
JOIN students s ON p.student_id = s.student_id
JOIN "class" c ON s.class_id = c.class_id
JOIN school_years sy ON c.school_year_id = sy.school_year_id
JOIN payment_type pt ON p.payment_type_id = pt.payment_type_id
ORDER BY p.created_at DESC;

--INSERT INTO students (nis, name, class_id, birthdate, address, phone_number) VALUES
--('10000000001', 'Ahmad Fauzi', 1, '2007-01-15', 'Jl. Mawar No.1', '081234567801'),
--('10000000002', 'Budi Santoso', 2, '2006-02-10', 'Jl. Melati No.2', '081234567802'),
--('10000000003', 'Citra Dewi', 3, '2007-03-20', 'Jl. Anggrek No.3', '081234567803'),
--('10000000004', 'Dian Pratama', 1, '2007-04-05', 'Jl. Kenanga No.4', '081234567804'),
--('10000000005', 'Eka Saputra', 2, '2006-05-12', 'Jl. Cempaka No.5', '081234567805'),
--('10000000006', 'Fikri Maulana', 3, '2007-06-18', 'Jl. Dahlia No.6', '081234567806'),
--('10000000007', 'Gita Sari', 1, '2007-07-25', 'Jl. Teratai No.7', '081234567807'),
--('10000000008', 'Hadi Wijaya', 2, '2006-08-14', 'Jl. Flamboyan No.8', '081234567808'),
--('10000000009', 'Indah Permata', 3, '2007-09-22', 'Jl. Bougenville No.9', '081234567809'),
--('10000000010', 'Joko Susilo', 1, '2007-10-11', 'Jl. Cemara No.10', '081234567810'),
--('10000000011', 'Kiki Amelia', 2, '2006-11-09', 'Jl. Mahoni No.11', '081234567811'),
--('10000000012', 'Lia Kusuma', 3, '2007-12-15', 'Jl. Jati No.12', '081234567812'),
--('10000000013', 'Mira Ayu', 1, '2007-01-30', 'Jl. Pinus No.13', '081234567813'),
--('10000000014', 'Nanda Febri', 2, '2006-02-28', 'Jl. Akasia No.14', '081234567814'),
--('10000000015', 'Oki Pranata', 3, '2007-03-19', 'Jl. Karet No.15', '081234567815'),
--('10000000016', 'Putri Lestari', 1, '2007-04-25', 'Jl. Mangga No.16', '081234567816'),
--('10000000017', 'Qori Rizki', 2, '2006-05-14', 'Jl. Rambutan No.17', '081234567817'),
--('10000000018', 'Rudi Hartono', 3, '2007-06-20', 'Jl. Durian No.18', '081234567818'),
--('10000000019', 'Sari Wulandari', 1, '2007-07-10', 'Jl. Salak No.19', '081234567819'),
--('10000000020', 'Taufik Hidayat', 2, '2006-08-15', 'Jl. Alpukat No.20', '081234567820');
--
--INSERT INTO users (nis, email, name, password, role) VALUES
--('10000000001', 'ahmad@gmail.com', 'Ahmad Fauzi', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000002', 'budi@gmail.com', 'Budi Santoso', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000003', 'citra@gmail.com', 'Citra Dewi', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000004', 'dian@gmail.com', 'Dian Pratama', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000005', 'eka@gmail.com', 'Eka Saputra', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000006', 'fikri@gmail.com', 'Fikri Maulana', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000007', 'gita@gmail.com', 'Gita Sari', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000008', 'hadi@gmail.com', 'Hadi Wijaya', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000009', 'indah@gmail.com', 'Indah Permata', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000010', 'joko@gmail.com', 'Joko Susilo', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000011', 'kiki@gmail.com', 'Kiki Amelia', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000012', 'lia@gmail.com', 'Lia Kusuma', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000013', 'mira@gmail.com', 'Mira Ayu', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000014', 'nanda@gmail.com', 'Nanda Febri', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000015', 'oki@gmail.com', 'Oki Pranata', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000016', 'putri@gmail.com', 'Putri Lestari', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000017', 'qori@gmail.com', 'Qori Rizki', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000018', 'rudi@gmail.com', 'Rudi Hartono', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000019', 'sari@gmail.com', 'Sari Wulandari', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER'),
--('10000000020', 'taufik@gmail.com', 'Taufik Hidayat', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'USER');

--INSERT INTO users (email, name, password, role) VALUES
--('admin@gmail.com', 'Administrator', '$2a$10$U6JT0Tq4t5ZW5f6BAzrVjeOvg/XveN2uszHY2oU7YtyZ3Uy1keGlm', 'ADMIN');

--
--INSERT INTO payments (payment_name, user_id, student_id, payment_type_id, amount, payment_status, description) VALUES
--('SPP Januari 2024', 1, 1, 1, 500000.00, 'Paid', 'Pembayaran SPP bulan Januari'),
--('SPP Februari 2024', 2, 2, 1, 500000.00, 'Pending', 'Pembayaran SPP bulan Februari'),
--('UAS Semester 1', 3, 3, 3, 300000.00, 'Paid', 'Pembayaran Ujian Akhir Semester 1'),
--('SPP Maret 2024', 4, 4, 1, 500000.00, 'Overdue', 'Pembayaran SPP bulan Maret'),
--('UTS Semester 2', 5, 5, 2, 300000.00, 'Paid', 'Pembayaran Ujian Tengah Semester 2'),
--('SPP April 2024', 6, 6, 1, 500000.00, 'Pending', 'Pembayaran SPP bulan April'),
--('Ekstrakurikuler Musik', 7, 7, 4, 150000.00, 'Paid', 'Pembayaran kegiatan musik'),
--('SPP Mei 2024', 8, 8, 1, 500000.00, 'Pending', 'Pembayaran SPP bulan Mei'),
--('SPP Juni 2024', 9, 9, 1, 500000.00, 'Paid', 'Pembayaran SPP bulan Juni'),
--('UAS Semester 2', 10, 10, 3, 300000.00, 'Paid', 'Pembayaran Ujian Akhir Semester 2');
--



--INSERT INTO payments (payment_name, user_id, student_id, payment_type_id, amount, payment_status, description)
--SELECT 
--    'SPP Maret 2025' AS payment_name,
--    u.user_id,
--    s.student_id,
--    1 AS payment_type_id,  -- Asumsi payment_type_id 1 adalah SPP
--    500000.00 AS amount,   -- Sesuaikan nominal pembayaran
--    'Pending' AS payment_status,
--    'Tagihan SPP bulan Maret 2025' AS description
--FROM students s
--JOIN users u ON s.nis = u.nis
--WHERE s.class_id = 1;
