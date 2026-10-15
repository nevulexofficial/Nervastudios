# NERVA FC — Football Manager & Real-Time 2D Match Engine

Bienvenido al proyecto oficial de **NERVA FC**, un simulador táctico y de gestión de fútbol para Android con estética de videojuego de consola AAA, construido con **Kotlin** y **Jetpack Compose**. 

El juego combina una experiencia profunda de gestión de clubes (plantilla, mercado de fichajes, patrocinadores, tácticas, finanzas, edición completa de club y panel admin) con un **motor de simulación de partidos 2D en tiempo real con Inteligencia Artificial individual en cada uno de los 22 futbolistas**.

---

## 🎮 1. Novedades y Contexto del Juego

### 1.1 Motor de Partido 2D (30 Minutos Reglamentarios + Tiempo Añadido)
- **Duración Reglamentaria**: Los partidos oficiales se disputan a **30 minutos de tiempo reglamentario**, divididos en dos tiempos de **15 minutos** cada uno:
  - **1° Tiempo**: Minuto 0' al 15' + tiempo de prolongación dictado por el colegiado.
  - **Entretiempo (Halftime)**: Pantalla interactiva con estadísticas del primer tiempo, ajuste táctico y botón para iniciar el segundo tiempo.
  - **2° Tiempo**: Minuto 15' al 30' + tiempo añadido final.
- **Tiempo Añadido por el Árbitro**: Al finalizar cada periodo reglamentario, el colegiado dictamina el descuento (+1', +2', +3', +4' o +5' minutos) desplegando el **panel LED luminoso del 4to árbitro** y el cronómetro de descuento.
- **Eliminación de Controles de Velocidad**: Se eliminaron los botones artificiales de velocidad (1x, 2x, 4x) para mantener una experiencia inmersiva y continua similar a una transmisión televisiva de esports. Se incluye el botón **"⏩ Simular al Final"** para aquellos managers que deseen resolver el encuentro de forma instantánea.
- **Reglas Oficiales y Faltas**: Árbitro con sanciones disciplinarias, tarjetas amarillas (🟨), expulsiones por roja directa o doble amonestación (🟥) que dejan al equipo con 10 hombres en cancha, y penaltis sancionados dentro del área.

### 1.2 "Liga Gamma Nerva" (12 Divisiones de 30 Jugadores)
- **Punto de Partida Obligatorio**: Todo nuevo director técnico y club comienza en la **"Liga Gamma Nerva"**, específicamente en la **División 12** ("División Inicial - Nuevo Jugador").
- **Estructura Piramidal**: 12 divisiones con hasta **30 clubes/jugadores por división**.
- **Zonas de Clasificación**:
  - 🟢 **Zona de Ascenso Directo**: Puestos 1 al 4.
  - 🔵 **Zona de Playoff de Ascenso**: Puestos 5 al 8.
  - ⚪ **Zona Neutra de Permanencia**: Puestos 9 al 24.
  - 🔴 **Zona de Descenso Crítico**: Puestos 25 al 30.
- **Pestañas y Tabla Estilo Consola**: Selector horizontal de divisiones (Div 1 a Div 12) con escudos, victorias, empates, derrotas, diferencia de goles (+/-), puntos y racha de los últimos 5 partidos (V/E/D).

### 1.3 Partidos Automáticos Programados cada 5 Horas
El sistema de competición organiza los partidos de liga en 4 franjas horarias fijas a lo largo del día:
- **05:00 AM** — Jornada Matutina
- **10:00 AM** — Jornada Mediodía
- **03:00 PM** — Jornada Tarde
- **08:00 PM** — Jornada Nocturna Estelar

Cada franja cuenta con su tarjeta interactiva, indicador de estado ("En Juego", "Próximo", "Finalizado"), cuenta regresiva en vivo hacia el siguiente pitazo inicial, simulación automática de resultados y botón **"JUGAR PARTIDO 2D"** para disputar el encuentro en la cancha virtual.

### 1.4 Pantalla de Carga (Splash Screen) Fluida y Única
- Aparece **exclusivamente una única vez** al arrancar la aplicación.
- No contiene botones manuales ni interrupciones de usuario.
- Presenta animación de halo giratorio neón, escudo del club, barra de progreso con las fases de carga del motor táctico y transición suave y automática hacia el menú principal.

### 1.5 Sección Exclusiva: "Mi Club" (Edición Avanzada)
Acceso dedicado e independiente desde la barra de navegación lateral con herramientas de personalización total:
- **Identidad Básica**: Nombre del Club, Abreviatura corta (3-4 letras), Lema oficial del equipo ("Motto"), Nombre del Director Técnico.
- **Infraestructura**: Nombre del Estadio y Aforo / Capacidad de espectadores.
- **Paleta de Colores**: Selector dinámico de color primario y secundario con previsualización en vivo.
- **Heráldica y Escudos**:
  - Formas de Escudo: Clásico, Corona imperial, Círculo moderno, Rombo/Diamante y Hexágono cyberpunk.
  - Símbolos centrales: Balón estelar, Águila bicéfala, León rampante, Corona real, Rayo relámpago y Estrella de gloria.
- **Card Holográfica**: Tarjeta de presentación del club con brillo dinámico que refleja los cambios al instante.

### 1.6 Sección Exclusiva: "Panel Admin" (Superusuario y Control Total)
Acceso directo desde la barra de navegación lateral (desbloqueado de fábrica para pruebas y accesible también vía cupón `iamnevulex`). Dividido en 5 secciones de control:
1. **⚽ Trucos & Finanzas**:
   - Inyección instantánea de fondos: +$50M, +$100M, +$500M y modo "Dinero Infinito".
   - Restaurar energía y moral de la plantilla al 100%.
   - Potenciador Squad 99 OVR (todos los jugadores con media de leyenda mundial).
   - Limpieza de amonestaciones y tarjetas rojas acumuladas.
   - Nivel máximo de instalaciones de entrenamiento y cantera juvenil.
   - Reputación del club a 5 estrellas mundiales.
   - Forzar victoria garantizada para el próximo encuentro.
2. **🎨 Fondo & Banners**:
   - Selección de fondos visuales temáticos: Cyber Dark, Neon Violet, Stadium Lights, Gold Luxury, Emerald Pitch.
   - Cambio y personalización de banners decorativos y logos.
3. **🏆 Creador de Ligas**:
   - Formulario para dar de alta ligas y torneos personalizados: Nombre del torneo, país/región, cantidad de divisiones, clubes por división, icono, banner oficial, y **liga desde la que se asciende** (árbol jerárquico de ascensos y descensos).
4. **👤 Creador de Jugadores**:
   - Creación completa de futbolistas: Nombre, Dorsal, Posición (POR, DEF, MED, DEL), Nacionalidad con bandera, atributos individuales (Ritmo, Tiro, Pase, Regate, Defensa, Físico) con cálculo dinámico de Media y Valor de Mercado.
5. **🎵 Música Personalizada**:
   - Selector de pistas de audio estilo videojuego de fútbol ("Nerva Champions Anthem", "Cyber Arena Electro", "Stadium Lights Synth", "Neon Dribble Beats"), control de volumen y botón para reproducir o silenciar.

### 1.7 Sección de Patrocinadores VIP (Sponsors)
- Marcas aliadas con logos, aportes económicos por victoria, primas por objetivos alcanzados y contratos de patrocinio de indumentaria y estadio.

---

## 📱 2. Cómo Probar el Juego y Obtener el APK

En el entorno de **Google AI Studio Build**:

1. **Emulador Android en Streaming (Prueba Inmediata)**:
   - En el panel derecho de la interfaz de AI Studio, verás el **Streaming Android Emulator**.
   - El APK se compila e instala automáticamente tras cada cambio. Puedes interactuar directamente con la pantalla táctil, probar la navegación en el menú lateral, disputar partidos en 2D, editar tu club y explorar el Panel Admin.

2. **Descargar el archivo APK o AAB**:
   - Haz clic en el menú de **Ajustes / Opciones** (icono de engranaje o tres puntos en la esquina superior de AI Studio).
   - Selecciona **"Export project"** o **"Build / Download APK"** para obtener el paquete `.apk` listo para instalar en cualquier teléfono o tablet Android real (habilitando la instalación desde fuentes desconocidas en el dispositivo).
   - También puedes exportar el proyecto completo como archivo ZIP o sincronizarlo con un repositorio de GitHub para compilarlo con Android Studio localmente.

---

## 🗄️ 3. Base de Datos en Supabase (Arquitectura SQL Opcional)

Para desplegar persistencia en la nube multiusuario, autenticación de managers y almacenamiento de imágenes y música en **Supabase**, se incluye el esquema DDL listo para ejecutar:

```sql
-- 1. EXTENSIONES
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- 2. PAÍSES Y BANDERAS
CREATE TABLE public.countries (
    code VARCHAR(3) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    flag_emoji VARCHAR(10) NOT NULL,
    flag_svg_url TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 3. PERFILES DE MANAGERS (DTs)
CREATE TABLE public.manager_profiles (
    id UUID PRIMARY KEY REFERENCES auth.users(id) ON DELETE CASCADE,
    manager_name VARCHAR(100) NOT NULL,
    email VARCHAR(255),
    role VARCHAR(20) DEFAULT 'manager',
    coins BIGINT DEFAULT 25000000,
    gems INT DEFAULT 100,
    experience INT DEFAULT 0,
    level INT DEFAULT 1,
    avatar_url TEXT,
    created_at TIMESTAMPTZ DEFAULT NOW(),
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

-- 4. CLUBES
CREATE TABLE public.clubs (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    short_name VARCHAR(10) NOT NULL,
    owner_id UUID REFERENCES public.manager_profiles(id) ON DELETE SET NULL,
    country_code VARCHAR(3) REFERENCES public.countries(code),
    badge_url TEXT NOT NULL,
    flag_emoji VARCHAR(10) DEFAULT '🌐',
    league_id VARCHAR(50),
    division_number INT DEFAULT 12,
    primary_color VARCHAR(10) DEFAULT '#0A2540',
    secondary_color VARCHAR(10) DEFAULT '#00D4B2',
    budget BIGINT DEFAULT 30000000,
    reputation INT DEFAULT 4,
    stadium_name VARCHAR(100) DEFAULT 'Estadio Centenario',
    stadium_capacity INT DEFAULT 45000,
    training_facility_level INT DEFAULT 3,
    youth_academy_level INT DEFAULT 3,
    is_user_club BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 5. JUGADORES
CREATE TABLE public.players (
    id VARCHAR(50) PRIMARY KEY,
    club_id VARCHAR(50) REFERENCES public.clubs(id) ON DELETE SET NULL,
    name VARCHAR(100) NOT NULL,
    number INT NOT NULL,
    position VARCHAR(10) NOT NULL,
    specific_pos VARCHAR(10) NOT NULL,
    age INT NOT NULL,
    nationality_code VARCHAR(3) REFERENCES public.countries(code),
    flag_emoji VARCHAR(10) DEFAULT '🌐',
    photo_url TEXT,
    overall INT NOT NULL CHECK (overall BETWEEN 40 AND 99),
    pace INT NOT NULL CHECK (pace BETWEEN 30 AND 99),
    shooting INT NOT NULL CHECK (shooting BETWEEN 30 AND 99),
    passing INT NOT NULL CHECK (passing BETWEEN 30 AND 99),
    dribbling INT NOT NULL CHECK (dribbling BETWEEN 30 AND 99),
    defending INT NOT NULL CHECK (defending BETWEEN 30 AND 99),
    physical INT NOT NULL CHECK (physical BETWEEN 30 AND 99),
    stamina INT DEFAULT 100 CHECK (stamina BETWEEN 0 AND 100),
    moral INT DEFAULT 85 CHECK (moral BETWEEN 0 AND 100),
    market_value BIGINT NOT NULL,
    salary_per_week INT NOT NULL,
    contract_years INT DEFAULT 3,
    is_starter BOOLEAN DEFAULT FALSE,
    is_transfer_listed BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

-- 7. TABLAS MULTIJUGADOR (PRESENCIA, SALAS Y PARTIDOS ONLINE)
-- Archivo completo en: supabase/multiplayer_schema.sql
CREATE TABLE public.multiplayer_presence (
    manager_id VARCHAR(100) PRIMARY KEY,
    manager_name VARCHAR(100) NOT NULL,
    club_name VARCHAR(100) NOT NULL,
    club_division INT DEFAULT 12,
    elo_rating INT DEFAULT 1200,
    status VARCHAR(30) DEFAULT 'online',
    last_seen TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE public.multiplayer_lobbies (
    id VARCHAR(100) PRIMARY KEY,
    room_code VARCHAR(20) UNIQUE NOT NULL,
    host_manager_id VARCHAR(100) NOT NULL,
    host_club_name VARCHAR(100) NOT NULL,
    host_elo INT DEFAULT 1200,
    guest_manager_id VARCHAR(100),
    guest_club_name VARCHAR(100),
    guest_elo INT DEFAULT 1200,
    status VARCHAR(30) DEFAULT 'waiting',
    match_id VARCHAR(100),
    is_private BOOLEAN DEFAULT FALSE,
    created_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE public.multiplayer_matches (
    id VARCHAR(100) PRIMARY KEY,
    lobby_id VARCHAR(100) REFERENCES public.multiplayer_lobbies(id),
    home_manager_id VARCHAR(100) NOT NULL,
    home_club_name VARCHAR(100) NOT NULL,
    away_manager_id VARCHAR(100) NOT NULL,
    away_club_name VARCHAR(100) NOT NULL,
    home_score INT DEFAULT 0,
    away_score INT DEFAULT 0,
    current_minute INT DEFAULT 0,
    is_playing BOOLEAN DEFAULT TRUE,
    is_finished BOOLEAN DEFAULT FALSE,
    last_commentary TEXT,
    updated_at TIMESTAMPTZ DEFAULT NOW()
);

CREATE TABLE public.multiplayer_leaderboard (
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
    division_tier VARCHAR(30) DEFAULT 'Gamma Nerva 12'
);
```

---

## 🚀 4. Compilación Automática en GitHub Actions

El repositorio cuenta con el flujo de trabajo en **`.github/workflows/build-nerva.yml`** para compilar el APK nativo en cada `push` o manualmente:

- **JDK**: Java 17 Temurin preconfigurado.
- **Gradle Caching**: `gradle/actions/setup-gradle@v4` para compilaciones rápidas.
- **Preparación automática**: Si falta `.env`, se genera automáticamente a partir de `.env.example`, y se restaura el `debug.keystore`.
- **Comando de compilación**: `./gradlew assembleDebug --no-daemon`
- **Artefacto generado**: `NERVA-by-Nevulex-APK` con la ruta `app/build/outputs/apk/debug/app-debug.apk`.

---

## 🛠️ 5. Tecnologías y Librerías Utilizadas

- **Kotlin 2.0+** & **Jetpack Compose (BOM 2024.09.00)**.
- **Supabase Cloud REST & Realtime**: Sincronización de presencia, salas multijugador y marcadores.
- **Material 3 (M3)** con diseño adaptativo y paleta cromática deportiva de neón.
- **Coroutines & StateFlow** para el loop de simulación del motor 2D y el cronómetro de 30 minutos.
- **Compose Navigation & BackHandler** para navegación fluida y segura.
- **Iconografía**: Android Material Icons Extended.

