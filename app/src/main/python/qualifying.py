import pandas as pd
from sklearn.ensemble import RandomForestRegressor
from sklearn.preprocessing import StandardScaler
import numpy as np
import joblib
import os
from typing import Tuple, Dict, Any

# Load datasets
HERE = os.path.dirname(__file__)
MODEL_DIR  = os.path.join(HERE, "saved_models")
SCALER_FILE, BASE_RF_FILE, EXTRA_FILE = [
    os.path.join(MODEL_DIR, fn) for fn in
    ("scaler.joblib", "base_rf.joblib", "extra_models.joblib")
]


# =============================
# 1) DEFINE MAPPINGS & BASELINES
# =============================

# Map track number to track names
track_mapping = {
    1: 'Abu Dhabi', 2: 'Australia', 3: 'Austria', 4: 'Azerbaijan', 5: 'Bahrain',
    6: 'Belgium', 7: 'Brazil', 8: 'Canada', 9: 'China', 10: 'Great Britain',
    11: 'Hungary', 12: 'Imola', 13: 'Italy', 14: 'Japan', 15: 'Las Vegas',
    16: 'Mexico', 17: 'Miami', 18: 'Monaco', 19: 'Netherlands', 20: 'Qatar',
    21: 'Saudi Arabia', 22: 'Singapore', 23: 'Spain', 24: 'United States'
}
# Map driver ID to short code
driver_mapping = {
    1: 'ALB', 2: 'ALO', 3: 'ANT', 4: 'BOR', 5: 'DOO', 6: 'GAS', 7: 'HAD', 8: 'HAM',
    9: 'HUL', 10: 'LAW', 11: 'LEC', 12: 'NOR', 13: 'OCO', 14: 'PIA', 15: 'RUS',
    16: 'SAI', 17: 'STR', 18: 'TSU', 19: 'VER', 20: 'BEA', 21: 'BOT', 22: 'LAT',
    23: 'MAG', 24: 'MSC', 25: 'PER', 26: 'RIC', 27: 'ZHO', 28: 'DEV', 29: 'SAR',
    30: 'VET', 31: 'COL'
}
# Map team ID to team names
team_mapping = {
    1: 'WILLIAMS', 2: 'ASTON MARTIN', 3: 'MERCEDES', 4: 'KICK', 5: 'ALPINE',
    6: 'RACING BULL', 7: 'FERRARI', 8: 'MCLAREN', 9: 'HAAS', 10: 'RED BULL'
}

# Baseline pole times for each track (e.g. last year’s best Q3 lap)
track_sector_baselines  = {
    1: (16.958, 35.776, 29.861),  # Abu Dhabi
    2: (25.961, 16.997, 32.128),  # Australia
    3: (16.254,	28.791,	19.269),  # Austria
    4: (35.702,	40.813,	24.850),  # Azerbaijan
    5: (28.784,	38.574,	22.483),  # Bahrain
    6: (31.998,	50.837,	30.324),  # Belgium
    7: (17.825,	34.909,	16.165),  # Brazil
    8: (20.057,	22.714,	28.971),  # Canada
    9: (23.996,	27.227,	39.418),  # China
    10: (28.016, 34.508, 23.295),  # Great Britain
    11: (27.606, 26.382, 21.239),  # Hungary
    12: (23.408, 25.922, 25.416),  # Imola
    13: (26.492, 26.579, 26.256),  # Italy
    14: (30.387,	39.355,	17.241),  # Japan
    15: (25.736,	30.916,	35.660),  # Las Vegas
    16: (27.037,	29.296,	19.613),  # Mexico
    17: (28.867,	33.499,	24.875),  # Miami
    18: (18.386,	33.174,	18.710),  # Monaco
    19: (23.824,	24.819,	21.030),  # Netherlands
    20: (29.598,	27.353,	23.569),  # Qatar
    21: (31.507,	27.756,	28.031),  # Saudi Arabia
    22: (26.599,	37.630,	25.296),  # Singapore
    23: (21.383,	28.402,	21.598),  # Spain
    24: (24.992,	36.887,	30.451),  # United States
}

def preprocess_data(df):
    df = df.copy()

    # Unpack baseline sectors
    df['Baseline_S1'] = df['Track'].map(lambda t: track_sector_baselines[t][0])
    df['Baseline_S2'] = df['Track'].map(lambda t: track_sector_baselines[t][1])
    df['Baseline_S3'] = df['Track'].map(lambda t: track_sector_baselines[t][2])

    # Compute deltas
    df['Delta_S1'] = df['Sector1Time'] - df['Baseline_S1']
    df['Delta_S2'] = df['Sector2Time'] - df['Baseline_S2']
    df['Delta_S3'] = df['Sector3Time'] - df['Baseline_S3']

    # Team‑year average for each sector
    for sec in ['1','2','3']:
        avg = (df.groupby(['Year','Team'])[f'Sector{sec}Time']
               .mean()
               .reset_index()
               .rename(columns={f'Sector{sec}Time':f'TeamAvg_S{sec}'}))
        df = df.merge(avg, on=['Year','Team'], how='left')

    # Fill any NaNs
    for col in ['Delta_S1','Delta_S2','Delta_S3','TeamAvg_S1','TeamAvg_S2','TeamAvg_S3']:
        df[col] = df[col].fillna(df[col].mean())

    return df

# =============================
# 2) LOAD DATA
# =============================

historical_data = pd.read_excel(os.path.join(HERE, "HistoricalData.xlsx"))
current_data = pd.read_excel(os.path.join(HERE, "CurrentData.xlsx"))
hist = preprocess_data(historical_data)
curr = preprocess_data(current_data)
all_data = pd.concat([hist, curr], ignore_index=True)

# =============================
# 3) HELPER FUNCTION: GET 2025 TEAMS
# =============================

def get_current_driver_teams(df):
    """
    Return one row per driver with their latest team (based on 2025 data).
    """
    return df.drop_duplicates('Driver', keep='last')[['Driver','Team']]

current_driver_teams = get_current_driver_teams(current_data)

# =============================
# 4) FEATURE ENGINEERING
# =============================


FEATURES = ['Year','Driver','Team','Track',
            'TeamAvg_S1','TeamAvg_S2','TeamAvg_S3']
TARGETS = ['Delta_S1','Delta_S2','Delta_S3']
EXTRA_COLS = ['Sector1SpeedMin','Sector1SpeedMax','Sector1SpeedAvg','Sector1RPMAvg','Sector1ThrottleAvg','Sector1BrakeAvg','Sector2SpeedMin','Sector2SpeedMax','Sector2SpeedAvg','Sector2RPMAvg','Sector2ThrottleAvg','Sector2BrakeAvg',
              'Sector3SpeedMin','Sector3SpeedMax','Sector3SpeedAvg','Sector3RPMAvg','Sector3ThrottleAvg','Sector3BrakeAvg','SpeedI1','SpeedI2','SpeedFL','SpeedST']


def fit_models() -> Tuple[StandardScaler, RandomForestRegressor, Dict[str, RandomForestRegressor]]:
    # ensure we have a session identifier in chronological order:
    all_data['SessionID'] = (
            all_data['Year'].astype(str) + "_" +
            all_data['RaceNo'].astype(str)  # or whatever orders your races
    )

    sessions = all_data['SessionID'].unique().tolist()

    splits = []
    for i in range(len(sessions)-1):
        train_sess = sessions[: i+1]     # train on all up to session i
        val_sess   = sessions[i+1]       # validate on session i+1
        train_idx  = all_data['SessionID'].isin(train_sess)
        val_idx    = all_data['SessionID'] == val_sess
        splits.append((train_idx, val_idx))

    for train_mask, val_mask in splits:

        X_tr = all_data.loc[train_mask, FEATURES]
        y_tr = all_data.loc[train_mask, TARGETS]
        X_vl = all_data.loc[val_mask, FEATURES]
        y_vl = all_data.loc[val_mask, TARGETS]

        scaler = StandardScaler().fit(X_tr)

        base_rf = RandomForestRegressor(
            n_estimators=200,
            max_depth=12,
            max_features="sqrt",
            bootstrap=True,
            random_state=42,)

        base_rf.fit(scaler.transform(X_tr), y_tr)



    extra_models: dict[str, RandomForestRegressor] = {}
    for col in EXTRA_COLS:
        mask = all_data[col].notna()
        if mask.sum() < 30:        # arbitrary minimum to avoid overfitting on tiny samples
            continue
        X_sub = scaler.transform(all_data.loc[mask, FEATURES])
        y_sub = all_data.loc[mask, col]
        model = RandomForestRegressor(n_estimators=120, max_depth=10, random_state=42)
        model.fit(X_sub, y_sub)
        extra_models[col] = model

    # make sure model dir exists and save everything for next run
    os.makedirs(MODEL_DIR, exist_ok=True)
    joblib.dump(scaler,      SCALER_FILE)
    joblib.dump(base_rf,     BASE_RF_FILE)
    joblib.dump(extra_models,EXTRA_FILE)
    return scaler, base_rf, extra_models


def _load_or_train_models() -> Tuple[StandardScaler,RandomForestRegressor,Dict[str, RandomForestRegressor]]:
    """
    Try to load cached models; if that fails for *any* reason, retrain and cache.
    """

    paths = (SCALER_FILE, BASE_RF_FILE, EXTRA_FILE)
    print("[ML] expecting models at:")
    for p in paths:
        print("      ", p, "✓" if os.path.exists(p) else "✗")

    try:
        if all(map(os.path.exists, paths)):
            print("[ML] Loading cached models …")
            # — turn off mem-mapping: some Android/embedded builds don’t like it
            scaler       = joblib.load(SCALER_FILE, mmap_mode=None)
            base_rf      = joblib.load(BASE_RF_FILE, mmap_mode=None)
            extra_models = joblib.load(EXTRA_FILE, mmap_mode=None)
            print("[ML] …loaded OK.")
            return scaler, base_rf, extra_models
        else:
            print("[ML] One or more model files missing; will train fresh.")
    except Exception as e:
        # any pickle or version incompatibility lands here
        print("[ML] Failed to load cached models ->", repr(e))
        print("[ML] Retraining from scratch…")

    # ↳ either files missing *or* load failed: build everything again
    return fit_models()

def _fallback_latest(driver_id: int, col: str) -> float:
    """Return the most‑recent recorded value for (driver, col) or np.nan."""
    rows = curr[curr['Driver'] == driver_id]
    if rows.empty or col not in rows.columns:
        return np.nan
    return rows.iloc[-1][col]

# =============================
# 6) PREDICTION FUNCTION
# =============================

def predict_qualifying_results(track_number: int) -> Dict[str, Any]:
    # the three globals used by predict_qualifying_results below
    scaler, base_rf, extra_models = _load_or_train_models()
    # Ensure track_number is valid
    if track_number not in track_mapping:
        raise ValueError(f"Track number {track_number} not in track_mapping.")

    # feature rows (one per current driver)
    latest = curr.drop_duplicates('Driver', keep='last')
    rows = [{
        'Year': 2025,
        'Driver': r['Driver'],
        'Team': r['Team'],
        'Track': track_number,
        'TeamAvg_S1': r['TeamAvg_S1'],
        'TeamAvg_S2': r['TeamAvg_S2'],
        'TeamAvg_S3': r['TeamAvg_S3']
    } for _, r in latest.iterrows()]
    X_inf = pd.DataFrame(rows)[FEATURES]
    Xs_inf = scaler.transform(X_inf)

    # sector‑time deltas → absolute times
    deltas = base_rf.predict(Xs_inf)
    b1, b2, b3 = track_sector_baselines[track_number]
    secs   = deltas + np.array([b1, b2, b3])
    laps   = secs.sum(axis=1)

    out = pd.DataFrame({
        'Driver': latest['Driver'].values,
        'Sector1Time': secs[:, 0],
        'Sector2Time': secs[:, 1],
        'Sector3Time': secs[:, 2],
        'LapTime': laps
    })

    # Telemetry predictions / fallbacks
    for col in EXTRA_COLS:
        if col in extra_models:
            out[col] = extra_models[col].predict(Xs_inf)
        else:
            out[col] = [ _fallback_latest(drv, col) for drv in out['Driver'] ]

    # Final formatting & ordering
    out['Position']    = out['LapTime'].rank(method='first').astype(int)
    out['DriverName']  = out['Driver'].map(driver_mapping)
    out['TeamName']    = out['Driver'].apply(lambda d: team_mapping[curr[curr['Driver']==d]['Team'].iloc[-1]])

    ordered_cols = ['Position', 'DriverName', 'TeamName',
                    'Sector1Time', 'Sector2Time', 'Sector3Time'] + EXTRA_COLS + ['LapTime']

    return out[ordered_cols].sort_values('Position').reset_index(drop=True)

# =============================
# 7) OPTIONAL: CLI MAIN
# =============================

def main(track_num):
    try:
        predictions = predict_qualifying_results(track_num)
        return predictions.to_csv(index=False) # ✅ safest output format
    except Exception as e:
        return str(e)