PRAGMA foreign_keys = ON;

CREATE TABLE IF NOT EXISTS admin_users (
  id INTEGER PRIMARY KEY AUTOINCREMENT,
  username TEXT NOT NULL UNIQUE,
  password_hash TEXT NOT NULL,
  display_name TEXT,
  role TEXT NOT NULL DEFAULT 'ADMIN',
  enabled INTEGER NOT NULL DEFAULT 1,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS element_types (
  id TEXT PRIMARY KEY,
  element_key TEXT NOT NULL UNIQUE,
  name TEXT NOT NULL,
  description TEXT,
  icon TEXT,
  color TEXT,
  sort_order INTEGER NOT NULL DEFAULT 0,
  active INTEGER NOT NULL DEFAULT 1,
  schema_json TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_element_types_active_sort
  ON element_types (active, sort_order);

CREATE TABLE IF NOT EXISTS model_assets (
  id TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  description TEXT,
  tags_json TEXT NOT NULL,
  category TEXT NOT NULL DEFAULT 'other',
  element_type_id TEXT,
  license TEXT NOT NULL DEFAULT 'CC0',
  original_filename TEXT NOT NULL,
  format TEXT NOT NULL,
  file_url TEXT NOT NULL,
  thumbnail_url TEXT,
  file_size INTEGER NOT NULL,
  polygon_count INTEGER,
  vertex_count INTEGER,
  material_count INTEGER,
  has_animations INTEGER NOT NULL DEFAULT 0,
  has_textures INTEGER NOT NULL DEFAULT 0,
  custom_props_json TEXT,
  bounding_box_json TEXT,
  position_json TEXT NOT NULL,
  rotation_json TEXT NOT NULL,
  scale_json TEXT NOT NULL,
  download_count INTEGER NOT NULL DEFAULT 0,
  view_count INTEGER NOT NULL DEFAULT 0,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (element_type_id) REFERENCES element_types(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_model_assets_category
  ON model_assets (category);

CREATE INDEX IF NOT EXISTS idx_model_assets_format
  ON model_assets (format);

CREATE INDEX IF NOT EXISTS idx_model_assets_element_type
  ON model_assets (element_type_id);

CREATE TABLE IF NOT EXISTS model_versions (
  id TEXT PRIMARY KEY,
  model_id TEXT NOT NULL,
  version_number INTEGER NOT NULL,
  file_url TEXT NOT NULL,
  change_note TEXT,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  UNIQUE (model_id, version_number),
  FOREIGN KEY (model_id) REFERENCES model_assets(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS animation_assets (
  id TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  description TEXT,
  tags_json TEXT NOT NULL,
  source_kind TEXT NOT NULL,
  original_filename TEXT NOT NULL,
  format TEXT NOT NULL,
  file_url TEXT NOT NULL,
  file_size INTEGER NOT NULL,
  action_count INTEGER NOT NULL DEFAULT 1,
  actions_json TEXT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_animation_assets_format
  ON animation_assets (format);

CREATE TABLE IF NOT EXISTS levels (
  id TEXT PRIMARY KEY,
  name TEXT NOT NULL,
  map_model_url TEXT NOT NULL,
  player_character_json TEXT,
  player_spawn_json TEXT NOT NULL,
  robot_spawn_json TEXT NOT NULL,
  robot_story TEXT NOT NULL,
  story_graph_json TEXT NOT NULL,
  zombie_spawns_json TEXT NOT NULL,
  placed_objects_json TEXT NOT NULL,
  created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_levels_updated_at
  ON levels (updated_at);

CREATE TABLE IF NOT EXISTS game_sessions (
  id TEXT PRIMARY KEY,
  player_name TEXT NOT NULL,
  level_id TEXT,
  state_json TEXT,
  started_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  last_seen_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  FOREIGN KEY (level_id) REFERENCES levels(id) ON DELETE SET NULL
);

CREATE INDEX IF NOT EXISTS idx_game_sessions_player_name
  ON game_sessions (player_name);

CREATE INDEX IF NOT EXISTS idx_game_sessions_level
  ON game_sessions (level_id);

INSERT OR IGNORE INTO element_types (id, element_key, name, description, color, sort_order)
VALUES
  ('11111111-1111-1111-1111-111111111111', 'structural', 'Structural', 'Walls, floors, ceilings, frames', '#8b7d6b', 1),
  ('22222222-2222-2222-2222-222222222222', 'display', 'Display', 'Screens, panels, media surfaces', '#3b82f6', 2),
  ('33333333-3333-3333-3333-333333333333', 'signage', 'Signage', 'Headers, wayfinding, logos', '#059669', 3);
