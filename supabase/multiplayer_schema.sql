-- ============================================================================
-- NERVA FC — SUPABASE MULTIPLAYER DATABASE SCHEMA
-- Compatible con PostgreSQL y Supabase Realtime
-- ============================================================================

-- 1. Extensiones necesarias
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- ============================================================================
-- 2. TABLA: PRESENCIA Y JUGADORES ONLINE (multiplayer_presence)
-- Registra managers conectados en tiempo real buscando rival
-- ============================================================================
CREATE TABLE IF NOT EXISTS public.multiplayer_presence (
    manager_id VARCHAR(100) PRIMARY KEY,
    manager_name VARCHAR(100) NOT NULL,
    club_name VARCHAR(100) NOT NULL,
    club_division INT DEFAULT 12,
    club_badge_shape VARCHAR(30) DEFAULT 'SHIELD',
    elo_rating INT DEFAULT 1200,
    status VARCHAR(30) DEFAULT 'online', -- 'online', 'looking_for_match', 'in_match'
    last_seen TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================================
-- 3. TABLA: SALAS Y LOBBIES DE MATCHMAKING (multiplayer_lobbies)
-- Gestión de salas públicas y privadas con código de 6 dígitos
-- ============================================================================
CREATE TABLE IF NOT EXISTS public.multiplayer_lobbies (
    id VARCHAR(100) PRIMARY KEY,
    room_code VARCHAR(20) UNIQUE NOT NULL, -- ej. 'NERVA-8492'
    host_manager_id VARCHAR(100) NOT NULL,
    host_club_name VARCHAR(100) NOT NULL,
    host_elo INT DEFAULT 1200,
    guest_manager_id VARCHAR(100),
    guest_club_name VARCHAR(100),
    guest_elo INT DEFAULT 1200,
    status VARCHAR(30) DEFAULT 'waiting', -- 'waiting', 'ready', 'playing', 'finished', 'cancelled'
    match_id VARCHAR(100),
    is_private BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================================
-- 4. TABLA: PARTIDOS MULTIJUGADOR EN VIVO (multiplayer_matches)
-- Sincronización del marcador, tiempo (30 min) y estado en tiempo real
-- ============================================================================
CREATE TABLE IF NOT EXISTS public.multiplayer_matches (
    id VARCHAR(100) PRIMARY KEY,
    lobby_id VARCHAR(100) REFERENCES public.multiplayer_lobbies(id) ON DELETE CASCADE,
    home_manager_id VARCHAR(100) NOT NULL,
    home_club_name VARCHAR(100) NOT NULL,
    away_manager_id VARCHAR(100) NOT NULL,
    away_club_name VARCHAR(100) NOT NULL,
    home_score INT DEFAULT 0,
    away_score INT DEFAULT 0,
    current_minute INT DEFAULT 0,
    current_second INT DEFAULT 0,
    current_half INT DEFAULT 1,
    is_playing BOOLEAN DEFAULT TRUE,
    is_halftime BOOLEAN DEFAULT FALSE,
    is_finished BOOLEAN DEFAULT FALSE,
    stoppage_time INT DEFAULT 0,
    ball_x REAL DEFAULT 0.5,
    ball_y REAL DEFAULT 0.5,
    last_commentary TEXT DEFAULT '¡Arranca el partido multijugador online!',
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================================
-- 5. TABLA: EVENTOS DEL PARTIDO EN VIVO (multiplayer_match_events)
-- Goles, tarjetas, faltas y cambios tácticos retransmitidos en tiempo real
-- ============================================================================
CREATE TABLE IF NOT EXISTS public.multiplayer_match_events (
    id BIGSERIAL PRIMARY KEY,
    match_id VARCHAR(100) REFERENCES public.multiplayer_matches(id) ON DELETE CASCADE,
    minute INT NOT NULL,
    event_type VARCHAR(30) NOT NULL, -- 'GOAL', 'YELLOW_CARD', 'RED_CARD', 'TACTIC_CHANGE', 'HALFTIME', 'FULLTIME'
    club_name VARCHAR(100) NOT NULL,
    player_name VARCHAR(100),
    description TEXT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================================
-- 6. TABLA: CLASIFICACIÓN Y RANKING GLOBAL ELO (multiplayer_leaderboard)
-- Tabla mundial de posiciones para el modo multijugador
-- ============================================================================
CREATE TABLE IF NOT EXISTS public.multiplayer_leaderboard (
    manager_id VARCHAR(100) PRIMARY KEY,
    manager_name VARCHAR(100) NOT NULL,
    club_name VARCHAR(100) NOT NULL,
    elo_rating INT DEFAULT 1200,
    matches_played INT DEFAULT 0,
    wins INT DEFAULT 0,
    draws INT DEFAULT 0,
    losses INT DEFAULT 0,
    goals_for INT DEFAULT 0,
    goals_against INT DEFAULT 0,
    division_tier VARCHAR(30) DEFAULT 'Gamma Nerva 12',
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- ============================================================================
-- 7. ÍNDICES PARA ALTO RENDIMIENTO
-- ============================================================================
CREATE INDEX IF NOT EXISTS idx_lobbies_status ON public.multiplayer_lobbies(status);
CREATE INDEX IF NOT EXISTS idx_lobbies_code ON public.multiplayer_lobbies(room_code);
CREATE INDEX IF NOT EXISTS idx_matches_lobby ON public.multiplayer_matches(lobby_id);
CREATE INDEX IF NOT EXISTS idx_events_match ON public.multiplayer_match_events(match_id);
CREATE INDEX IF NOT EXISTS idx_leaderboard_elo ON public.multiplayer_leaderboard(elo_rating DESC);

-- ============================================================================
-- 8. POLÍTICAS DE SEGURIDAD ROW LEVEL SECURITY (RLS)
-- Permite lectura y escritura transparente para jugadores con la clave anon
-- ============================================================================
ALTER TABLE public.multiplayer_presence ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.multiplayer_lobbies ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.multiplayer_matches ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.multiplayer_match_events ENABLE ROW LEVEL SECURITY;
ALTER TABLE public.multiplayer_leaderboard ENABLE ROW LEVEL SECURITY;

-- Reglas públicas para multiplayer_presence
DROP POLICY IF EXISTS "Public select presence" ON public.multiplayer_presence;
CREATE POLICY "Public select presence" ON public.multiplayer_presence FOR SELECT USING (true);
DROP POLICY IF EXISTS "Public upsert presence" ON public.multiplayer_presence;
CREATE POLICY "Public upsert presence" ON public.multiplayer_presence FOR ALL USING (true) WITH CHECK (true);

-- Reglas públicas para multiplayer_lobbies
DROP POLICY IF EXISTS "Public select lobbies" ON public.multiplayer_lobbies;
CREATE POLICY "Public select lobbies" ON public.multiplayer_lobbies FOR SELECT USING (true);
DROP POLICY IF EXISTS "Public upsert lobbies" ON public.multiplayer_lobbies;
CREATE POLICY "Public upsert lobbies" ON public.multiplayer_lobbies FOR ALL USING (true) WITH CHECK (true);

-- Reglas públicas para multiplayer_matches
DROP POLICY IF EXISTS "Public select matches" ON public.multiplayer_matches;
CREATE POLICY "Public select matches" ON public.multiplayer_matches FOR SELECT USING (true);
DROP POLICY IF EXISTS "Public upsert matches" ON public.multiplayer_matches;
CREATE POLICY "Public upsert matches" ON public.multiplayer_matches FOR ALL USING (true) WITH CHECK (true);

-- Reglas públicas para multiplayer_match_events
DROP POLICY IF EXISTS "Public select events" ON public.multiplayer_match_events;
CREATE POLICY "Public select events" ON public.multiplayer_match_events FOR SELECT USING (true);
DROP POLICY IF EXISTS "Public insert events" ON public.multiplayer_match_events;
CREATE POLICY "Public insert events" ON public.multiplayer_match_events FOR INSERT WITH CHECK (true);

-- Reglas públicas para multiplayer_leaderboard
DROP POLICY IF EXISTS "Public select leaderboard" ON public.multiplayer_leaderboard;
CREATE POLICY "Public select leaderboard" ON public.multiplayer_leaderboard FOR SELECT USING (true);
DROP POLICY IF EXISTS "Public upsert leaderboard" ON public.multiplayer_leaderboard;
CREATE POLICY "Public upsert leaderboard" ON public.multiplayer_leaderboard FOR ALL USING (true) WITH CHECK (true);

-- ============================================================================
-- 9. ACTIVAR REPLICACIÓN EN TIEMPO REAL (Supabase Realtime)
-- Permite suscribirse a cambios de estado en lobbies y partidos
-- ============================================================================
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_publication_tables 
        WHERE pubname = 'supabase_realtime' AND tablename = 'multiplayer_lobbies'
    ) THEN
        ALTER PUBLICATION supabase_realtime ADD TABLE public.multiplayer_lobbies;
    END IF;
    
    IF NOT EXISTS (
        SELECT 1 FROM pg_publication_tables 
        WHERE pubname = 'supabase_realtime' AND tablename = 'multiplayer_matches'
    ) THEN
        ALTER PUBLICATION supabase_realtime ADD TABLE public.multiplayer_matches;
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM pg_publication_tables 
        WHERE pubname = 'supabase_realtime' AND tablename = 'multiplayer_presence'
    ) THEN
        ALTER PUBLICATION supabase_realtime ADD TABLE public.multiplayer_presence;
    END IF;
END $$;

-- Datos iniciales para la tabla de clasificación de prueba
INSERT INTO public.multiplayer_leaderboard (manager_id, manager_name, club_name, elo_rating, matches_played, wins, draws, losses, goals_for, goals_against, division_tier)
VALUES 
    ('bot_1', 'Carlos DT', 'Real Madrid FC', 1450, 24, 18, 3, 3, 52, 19, 'División 1'),
    ('bot_2', 'Alexandre', 'Manchester Blues', 1380, 20, 14, 2, 4, 44, 22, 'División 1'),
    ('bot_3', 'Mateo Rossi', 'Milán Rossonero', 1310, 19, 12, 4, 3, 38, 20, 'División 2'),
    ('bot_4', 'Sébastien', 'Paris Saint DT', 1260, 16, 9, 3, 4, 31, 21, 'División 3'),
    ('bot_5', 'Lucas Silva', 'Flamengo Star', 1210, 15, 8, 2, 5, 27, 24, 'División 4')
ON CONFLICT (manager_id) DO NOTHING;
