-- Matikan sementara constraint foreign key untuk mencegah error
SET session_replication_role = 'replica';

-- Truncate semua tabel (menghapus semua data tanpa menghapus struktur tabel)
TRUNCATE TABLE 
    payments,
    students,
    users
--    payment_type,
--    "class",
--    school_years
RESTART IDENTITY;

-- Aktifkan kembali constraint foreign key
SET session_replication_role = 'origin';