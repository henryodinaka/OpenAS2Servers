-- ----------------------------------------------------------------------- 
-- msg_metadata 
-- ----------------------------------------------------------------------- 

DROP TABLE msg_metadata IF EXISTS;

-- ----------------------------------------------------------------------- 
-- msg_metadata 
-- ----------------------------------------------------------------------- 

CREATE TABLE msg_metadata
(
    id INTEGER NOT NULL IDENTITY,
    msg_id VARCHAR NOT NULL,
    prior_msg_id VARCHAR,
    mdn_id VARCHAR,
    direction VARCHAR(25),
    is_resend VARCHAR(1),
    resend_count INTEGER,
    sender_id VARCHAR(255) NOT NULL,
    receiver_id VARCHAR(255) NOT NULL,
    status VARCHAR(255),
    state VARCHAR(255),
    signature_algorithm VARCHAR(255),
    encryption_algorithm VARCHAR(255),
    compression VARCHAR(255),
    file_name VARCHAR(255),
    original_file_name VARCHAR(255),
    content_type VARCHAR(255),
    content_transfer_encoding VARCHAR(255),
    mdn_mode VARCHAR(255),
    mdn_response VARCHAR,
    state_msg VARCHAR,
    create_dt TIMESTAMP,
    update_dt TIMESTAMP,
    PRIMARY KEY (id)
);

CREATE UNIQUE INDEX msg_id_unique ON msg_metadata (msg_id);

