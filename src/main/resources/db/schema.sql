CREATE DATABASE IF NOT EXISTS model_3d CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE model_3d;

CREATE TABLE IF NOT EXISTS admin_users (
  id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Internal primary key for administrator accounts.',
  username VARCHAR(80) NOT NULL COMMENT 'Unique administrator login name.',
  password_hash VARCHAR(255) NOT NULL COMMENT 'BCrypt password hash used by Spring Security.',
  display_name VARCHAR(120) NULL COMMENT 'Human readable administrator name.',
  role VARCHAR(40) NOT NULL DEFAULT 'ADMIN' COMMENT 'Security role. Keep ADMIN for management APIs.',
  enabled TINYINT(1) NOT NULL DEFAULT 1 COMMENT 'Whether this administrator account can authenticate.',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Creation timestamp.',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT 'Last update timestamp.',
  PRIMARY KEY (id),
  UNIQUE KEY uk_admin_users_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Administrator accounts for secured management endpoints.';

CREATE TABLE IF NOT EXISTS element_types (
  id CHAR(36) NOT NULL COMMENT 'Stable UUID for an extensible 3D element type.',
  element_key VARCHAR(80) NOT NULL COMMENT 'Machine key such as structural, display, signage.',
  name VARCHAR(120) NOT NULL COMMENT 'Display name for UI filters.',
  description TEXT NULL COMMENT 'Explanation of how this element type should be used.',
  icon VARCHAR(80) NULL COMMENT 'Optional frontend icon key.',
  color VARCHAR(32) NULL COMMENT 'Optional hex color used by the frontend.',
  sort_order INT NOT NULL DEFAULT 0 COMMENT 'Ordering value for menus.',
  active TINYINT(1) NOT NULL DEFAULT 1 COMMENT 'Soft visibility flag.',
  schema_json JSON NULL COMMENT 'Optional future validation metadata for this element type.',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Creation timestamp.',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT 'Last update timestamp.',
  PRIMARY KEY (id),
  UNIQUE KEY uk_element_types_key (element_key),
  KEY idx_element_types_active_sort (active, sort_order)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Catalog of model element types; designed to be extended without code changes.';

CREATE TABLE IF NOT EXISTS model_assets (
  id CHAR(36) NOT NULL COMMENT 'Stable UUID for one uploaded 3D model.',
  name VARCHAR(180) NOT NULL COMMENT 'Model display name.',
  description TEXT NULL COMMENT 'Model description supplied by admin.',
  tags_json JSON NOT NULL COMMENT 'Array of searchable tags.',
  category VARCHAR(40) NOT NULL DEFAULT 'other' COMMENT 'High-level category: architecture, character, vehicle, environment, prop, furniture, electronics, other.',
  element_type_id CHAR(36) NULL COMMENT 'Optional link to element_types for extensible classification.',
  license VARCHAR(40) NOT NULL DEFAULT 'CC0' COMMENT 'License code: CC0, CC_BY, MIT, proprietary.',
  original_filename VARCHAR(255) NOT NULL COMMENT 'Original uploaded filename.',
  format VARCHAR(20) NOT NULL COMMENT 'Stored model format such as glb, gltf, obj, fbx, stl, ply, usdz.',
  file_url VARCHAR(500) NOT NULL COMMENT 'Public relative URL for model download/rendering.',
  thumbnail_url VARCHAR(500) NULL COMMENT 'Optional public relative URL for preview thumbnail.',
  file_size BIGINT NOT NULL COMMENT 'Stored file size in bytes.',
  polygon_count INT NULL COMMENT 'Optional geometry polygon count.',
  vertex_count INT NULL COMMENT 'Optional geometry vertex count.',
  material_count INT NULL COMMENT 'Optional material count.',
  has_animations TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Whether this model has animation data.',
  has_textures TINYINT(1) NOT NULL DEFAULT 0 COMMENT 'Whether this model references textures.',
  custom_props_json JSON NULL COMMENT 'Flexible model metadata for rigging, source normalization, and future flags.',
  bounding_box_json JSON NULL COMMENT 'Optional bounding box metadata from model analysis.',
  position_json JSON NOT NULL COMMENT 'Default editor position vector [x,y,z].',
  rotation_json JSON NOT NULL COMMENT 'Default editor rotation vector [x,y,z].',
  scale_json JSON NOT NULL COMMENT 'Default editor scale vector [x,y,z].',
  download_count INT NOT NULL DEFAULT 0 COMMENT 'Total download counter.',
  view_count INT NOT NULL DEFAULT 0 COMMENT 'Total detail-view counter.',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Creation timestamp.',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT 'Last update timestamp.',
  PRIMARY KEY (id),
  KEY idx_model_assets_category (category),
  KEY idx_model_assets_format (format),
  KEY idx_model_assets_element_type (element_type_id),
  CONSTRAINT fk_model_assets_element_type FOREIGN KEY (element_type_id) REFERENCES element_types(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Uploaded 3D model catalog and editor metadata.';

CREATE TABLE IF NOT EXISTS model_versions (
  id CHAR(36) NOT NULL COMMENT 'Stable UUID for a model version snapshot.',
  model_id CHAR(36) NOT NULL COMMENT 'Model that owns this version.',
  version_number INT NOT NULL COMMENT 'Incremental version number per model.',
  file_url VARCHAR(500) NOT NULL COMMENT 'Public relative URL of the versioned model file.',
  change_note TEXT NULL COMMENT 'Optional admin note explaining the version.',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Creation timestamp.',
  PRIMARY KEY (id),
  UNIQUE KEY uk_model_versions_number (model_id, version_number),
  CONSTRAINT fk_model_versions_model FOREIGN KEY (model_id) REFERENCES model_assets(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Version history for uploaded 3D models.';

CREATE TABLE IF NOT EXISTS animation_assets (
  id CHAR(36) NOT NULL COMMENT 'Stable UUID for an uploaded animation source.',
  name VARCHAR(180) NOT NULL COMMENT 'Animation display name.',
  description TEXT NULL COMMENT 'Animation description supplied by admin.',
  tags_json JSON NOT NULL COMMENT 'Array of searchable tags.',
  source_kind VARCHAR(20) NOT NULL COMMENT 'single for one FBX or pack for ZIP bundles.',
  original_filename VARCHAR(255) NOT NULL COMMENT 'Original uploaded filename.',
  format VARCHAR(20) NOT NULL COMMENT 'Stored animation format: fbx or zip.',
  file_url VARCHAR(500) NOT NULL COMMENT 'Public relative URL for the stored animation source.',
  file_size BIGINT NOT NULL COMMENT 'Stored file size in bytes.',
  action_count INT NOT NULL DEFAULT 1 COMMENT 'Number of actions represented by this asset.',
  actions_json JSON NOT NULL COMMENT 'Action manifest with action ids, names, and source paths.',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Creation timestamp.',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT 'Last update timestamp.',
  PRIMARY KEY (id),
  KEY idx_animation_assets_format (format)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Uploaded FBX/ZIP animation sources for character actions.';

CREATE TABLE IF NOT EXISTS levels (
  id CHAR(36) NOT NULL COMMENT 'Stable UUID for a playable level.',
  name VARCHAR(180) NOT NULL COMMENT 'Level display name.',
  map_model_url VARCHAR(500) NOT NULL COMMENT 'Public relative URL for the map model.',
  player_character_json JSON NULL COMMENT 'Optional selected player character metadata.',
  player_spawn_json JSON NOT NULL COMMENT 'Player spawn vector [x,y,z].',
  robot_spawn_json JSON NOT NULL COMMENT 'Robot/NPC spawn vector [x,y,z].',
  robot_story TEXT NOT NULL COMMENT 'Plain story text used by the NPC.',
  story_graph_json JSON NOT NULL COMMENT 'Node graph for story/dialogue/game events.',
  zombie_spawns_json JSON NOT NULL COMMENT 'Enemy spawn definitions.',
  placed_objects_json JSON NOT NULL COMMENT 'Placed map/object instances for the level.',
  created_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Creation timestamp.',
  updated_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6) COMMENT 'Last update timestamp.',
  PRIMARY KEY (id),
  KEY idx_levels_updated_at (updated_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Playable game levels assembled from uploaded models.';

CREATE TABLE IF NOT EXISTS game_sessions (
  id CHAR(36) NOT NULL COMMENT 'Stable UUID for one public game session.',
  player_name VARCHAR(120) NOT NULL COMMENT 'Player character name entered before starting the game.',
  level_id CHAR(36) NULL COMMENT 'Optional level selected for the session.',
  state_json JSON NULL COMMENT 'Flexible runtime state checkpoint for future save/resume support.',
  started_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Session start timestamp.',
  last_seen_at DATETIME(6) NOT NULL DEFAULT CURRENT_TIMESTAMP(6) COMMENT 'Last heartbeat or progress update timestamp.',
  PRIMARY KEY (id),
  KEY idx_game_sessions_player_name (player_name),
  KEY idx_game_sessions_level (level_id),
  CONSTRAINT fk_game_sessions_level FOREIGN KEY (level_id) REFERENCES levels(id) ON DELETE SET NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='Public game sessions; no login required, only a player name.';

INSERT IGNORE INTO element_types (id, element_key, name, description, color, sort_order)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'structural', 'Structural', 'Walls, floors, ceilings, frames', '#8b7d6b', 1),
  ('22222222-2222-2222-2222-222222222222', 'display', 'Display', 'Screens, panels, media surfaces', '#3b82f6', 2),
  ('33333333-3333-3333-3333-333333333333', 'signage', 'Signage', 'Headers, wayfinding, logos', '#059669', 3);
