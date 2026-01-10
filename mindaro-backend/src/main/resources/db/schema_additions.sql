-- Mindaro backend: schema additions for newly introduced API modules
-- STRICT snake_case tables, keeping existing prefix convention (a1_/x1_/d1_)
-- NOTE: Run these in your MariaDB database (test_advj or your target DB).

-- -----------------------------------------
-- Bookings
-- -----------------------------------------
CREATE TABLE IF NOT EXISTS a1_bookings (
  id BINARY(16) NOT NULL,
  user_id BINARY(16) NOT NULL,
  mentor_id BINARY(16) NOT NULL,
  scheduled_at DATETIME(6) NOT NULL,
  session_type VARCHAR(50) NULL,
  amount DECIMAL(10,2) NULL,
  status VARCHAR(20) NOT NULL,
  notes TEXT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  KEY idx_a1_bookings_user_id (user_id),
  KEY idx_a1_bookings_mentor_id (mentor_id),
  CONSTRAINT fk_a1_bookings_user FOREIGN KEY (user_id) REFERENCES a1_user (id),
  CONSTRAINT fk_a1_bookings_mentor FOREIGN KEY (mentor_id) REFERENCES a1_mentor (id)
);

-- -----------------------------------------
-- Payments
-- -----------------------------------------
CREATE TABLE IF NOT EXISTS a1_payments (
  id BINARY(16) NOT NULL,
  user_id BINARY(16) NOT NULL,
  amount DECIMAL(10,2) NOT NULL,
  currency VARCHAR(50) NULL,
  provider VARCHAR(50) NULL,
  provider_payment_id VARCHAR(200) NULL,
  status VARCHAR(20) NOT NULL,
  meta TEXT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  KEY idx_a1_payments_user_id (user_id),
  CONSTRAINT fk_a1_payments_user FOREIGN KEY (user_id) REFERENCES a1_user (id)
);

-- -----------------------------------------
-- Kundli
-- -----------------------------------------
CREATE TABLE IF NOT EXISTS a1_kundli (
  id BINARY(16) NOT NULL,
  user_id BINARY(16) NOT NULL,
  full_name VARCHAR(80) NOT NULL,
  dob VARCHAR(30) NOT NULL,
  tob VARCHAR(30) NOT NULL,
  place VARCHAR(120) NOT NULL,
  chart_json TEXT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  KEY idx_a1_kundli_user_id (user_id),
  CONSTRAINT fk_a1_kundli_user FOREIGN KEY (user_id) REFERENCES a1_user (id)
);

-- -----------------------------------------
-- Favorites
-- -----------------------------------------
CREATE TABLE IF NOT EXISTS a1_favorites (
  id BINARY(16) NOT NULL,
  user_id BINARY(16) NOT NULL,
  mentor_id BINARY(16) NOT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  UNIQUE KEY uq_a1_favorites_user_mentor (user_id, mentor_id),
  KEY idx_a1_favorites_user_id (user_id),
  KEY idx_a1_favorites_mentor_id (mentor_id),
  CONSTRAINT fk_a1_favorites_user FOREIGN KEY (user_id) REFERENCES a1_user (id),
  CONSTRAINT fk_a1_favorites_mentor FOREIGN KEY (mentor_id) REFERENCES a1_mentor (id)
);

-- -----------------------------------------
-- Wallet
-- -----------------------------------------
CREATE TABLE IF NOT EXISTS a1_wallet (
  id BINARY(16) NOT NULL,
  user_id BINARY(16) NOT NULL,
  balance DECIMAL(10,2) NOT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  UNIQUE KEY uq_a1_wallet_user_id (user_id),
  CONSTRAINT fk_a1_wallet_user FOREIGN KEY (user_id) REFERENCES a1_user (id)
);

CREATE TABLE IF NOT EXISTS a1_wallet_transaction (
  id BINARY(16) NOT NULL,
  wallet_id BINARY(16) NULL,
  transaction_type VARCHAR(50) NULL,
  amount DECIMAL(10,2) NULL,
  status VARCHAR(50) NULL,
  payment_gateway_reference VARCHAR(255) NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  KEY idx_a1_wallet_transaction_wallet_id (wallet_id),
  CONSTRAINT fk_a1_wallet_transaction_wallet FOREIGN KEY (wallet_id) REFERENCES a1_wallet (id)
);

-- -----------------------------------------
-- Credits
-- -----------------------------------------
CREATE TABLE IF NOT EXISTS a1_credits_balance (
  id BINARY(16) NOT NULL,
  user_id BINARY(16) NOT NULL,
  credits INT NOT NULL DEFAULT 0,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  UNIQUE KEY uq_a1_credits_balance_user_id (user_id),
  CONSTRAINT fk_a1_credits_balance_user FOREIGN KEY (user_id) REFERENCES a1_user (id)
);

CREATE TABLE IF NOT EXISTS a1_credits_transactions (
  id BINARY(16) NOT NULL,
  user_id BINARY(16) NOT NULL,
  credits INT NOT NULL,
  type VARCHAR(30) NULL,
  ref VARCHAR(200) NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  KEY idx_a1_credits_transactions_user_id (user_id),
  CONSTRAINT fk_a1_credits_transactions_user FOREIGN KEY (user_id) REFERENCES a1_user (id)
);

-- -----------------------------------------
-- Analytics
-- -----------------------------------------
CREATE TABLE IF NOT EXISTS x1_analytics_event (
  id BINARY(16) NOT NULL,
  user_id BINARY(16) NULL,
  event_name VARCHAR(80) NOT NULL,
  properties_json TEXT NULL,
  source VARCHAR(120) NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  KEY idx_x1_analytics_event_user_id (user_id),
  CONSTRAINT fk_x1_analytics_event_user FOREIGN KEY (user_id) REFERENCES a1_user (id)
);

-- -----------------------------------------
-- Notifications
-- -----------------------------------------
CREATE TABLE IF NOT EXISTS x1_notification (
  id BINARY(16) NOT NULL,
  user_id BINARY(16) NOT NULL,
  title VARCHAR(120) NOT NULL,
  body TEXT NULL,
  data_json TEXT NULL,
  is_read BOOLEAN NOT NULL DEFAULT FALSE,
  scheduled_at DATETIME(6) NULL,
  sent_at DATETIME(6) NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  KEY idx_x1_notification_user_id (user_id),
  CONSTRAINT fk_x1_notification_user FOREIGN KEY (user_id) REFERENCES a1_user (id)
);

CREATE TABLE IF NOT EXISTS x1_notification_preference (
  id BINARY(16) NOT NULL,
  user_id BINARY(16) NOT NULL,
  push_enabled BOOLEAN NOT NULL DEFAULT TRUE,
  email_enabled BOOLEAN NOT NULL DEFAULT TRUE,
  sms_enabled BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  UNIQUE KEY uq_x1_notification_preference_user_id (user_id),
  CONSTRAINT fk_x1_notification_preference_user FOREIGN KEY (user_id) REFERENCES a1_user (id)
);

-- -----------------------------------------
-- Video sessions
-- -----------------------------------------
CREATE TABLE IF NOT EXISTS x1_video_session (
  id BINARY(16) NOT NULL,
  room_id VARCHAR(120) NOT NULL,
  user_id BINARY(16) NOT NULL,
  mentor_id BINARY(16) NOT NULL,
  status VARCHAR(20) NOT NULL,
  started_at DATETIME(6) NULL,
  ended_at DATETIME(6) NULL,
  meta TEXT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  KEY idx_x1_video_session_room_id (room_id),
  KEY idx_x1_video_session_user_id (user_id),
  KEY idx_x1_video_session_mentor_id (mentor_id),
  CONSTRAINT fk_x1_video_session_user FOREIGN KEY (user_id) REFERENCES a1_user (id),
  CONSTRAINT fk_x1_video_session_mentor FOREIGN KEY (mentor_id) REFERENCES a1_mentor (id)
);

-- -----------------------------------------
-- App content (banners/testimonials/faqs)
-- -----------------------------------------
CREATE TABLE IF NOT EXISTS d1_banners (
  id BINARY(16) NOT NULL,
  title VARCHAR(120) NULL,
  image_url TEXT NULL,
  redirect_url TEXT NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS d1_testimonials (
  id BINARY(16) NOT NULL,
  name VARCHAR(120) NULL,
  message TEXT NULL,
  rating DECIMAL(3,2) NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS d1_faqs (
  id BINARY(16) NOT NULL,
  question TEXT NOT NULL,
  answer TEXT NOT NULL,
  is_active BOOLEAN NOT NULL DEFAULT TRUE,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id)
);

-- -----------------------------------------
-- Feedback
-- -----------------------------------------
CREATE TABLE IF NOT EXISTS x1_feedback (
  id BINARY(16) NOT NULL,
  user_id BINARY(16) NULL,
  message TEXT NOT NULL,
  rating INT NULL,
  meta TEXT NULL,
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
  PRIMARY KEY (id),
  KEY idx_x1_feedback_user_id (user_id),
  CONSTRAINT fk_x1_feedback_user FOREIGN KEY (user_id) REFERENCES a1_user (id)
);
