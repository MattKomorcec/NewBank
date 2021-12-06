CREATE TABLE IF NOT EXISTS users (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    dob VARCHAR(20) NOT NULL,
    username VARCHAR(20) NOT NULL UNIQUE,
    password VARCHAR(20) NOT NULL,
    account_number VARCHAR(20) NOT NULL UNIQUE,
    sortcode VARCHAR(20) NOT NULL UNIQUE,
    balance INTEGER NOT NULL,
    secret_answer VARCHAR(50),
    account_locked INTEGER NOT NULL CHECK (account_locked IN (0, 1)) DEFAULT 0,
    full_name VARCHAR(100) NOT NULL,
    CONSTRAINT 'id_unique' UNIQUE ('id')
)
