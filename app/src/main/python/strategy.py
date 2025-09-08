import pandas as pd
import numpy as np
import glob
import os
from sklearn.model_selection import train_test_split, GridSearchCV
from sklearn.ensemble import RandomForestClassifier, GradientBoostingClassifier
from sklearn.preprocessing import LabelEncoder, StandardScaler
from sklearn.pipeline import Pipeline
from sklearn.metrics import accuracy_score, classification_report
import joblib
from datetime import datetime
import re


import warnings
warnings.filterwarnings('ignore')

HERE = os.path.dirname(__file__)
MODEL_DIR = os.path.join(HERE, "saved_models")  # same as your qualifying code
MODEL_FILE = os.path.join(MODEL_DIR, "f1_strategy_model.joblib")
class F1StrategyPredictor:
    """
    A comprehensive ML-based system for predicting optimal race strategies in Formula 1
    based on track characteristics, starting position, and race conditions.
    """

    def __init__(self):
        self.model_data = None
        self.data_path = os.path.join(os.path.dirname(__file__), "data")

    def train_model(self):
        """
        Train two machine learning models: one for dry races, one for wet races.
        """

        print("Training strategy models from Excel...")

        # Load the full dataset
        full_df = pd.read_excel(os.path.join(HERE, "StrategyData.xlsx"))

        # Basic cleaning
        full_df['IsWet'] = full_df['IsWet'].astype(int)
        # Add a new column with max pit stops for each row based on Track
        track_max_pitstops = {
            1: 2,
            2: 2,
            3: 2,
            4: 1,
            5: 2,
            6: 2,
            7: 2,
            8: 3,
            9: 3,
            10: 3,
            11: 2,
            12: 1,
            13: 2,
            14: 3,
            15: 2,
            16: 2,
            17: 2,
            18: 1,
            19: 2,
            20: 2,
            21: 1,
            22: 1,
            23: 2,
            24: 1,
        }
        # full_df['MaxPitStops'] = full_df['Track'].map(track_max_pitstops)


        # Now filter based on per-track maximums
        # full_df = full_df[full_df['NumPitStops'] <= full_df['MaxPitStops']]

        # Encode TrackType and OvertakingDifficulty
        track_type_encoder = LabelEncoder()
        overtaking_diff_encoder = LabelEncoder()
        strategy_encoder = LabelEncoder()

        full_df['TrackTypeEncoded'] = track_type_encoder.fit_transform(full_df['TrackType'])
        full_df['OvertakingDifficultyEncoded'] = overtaking_diff_encoder.fit_transform(full_df['OvertakingDifficulty'])
        full_df['StrategyEncoded'] = strategy_encoder.fit_transform(full_df['Strategy'])

        strategy_counts = full_df['Strategy'].value_counts()
        common_strategies = strategy_counts[strategy_counts >= 5].index  # Keep strategies with at least 3 appearances
        full_df = full_df[full_df['Strategy'].isin(common_strategies)]


        dry_df = full_df[(full_df['IsWet'] == 0) & (~full_df['Strategy'].str.contains('I|W'))]

        # For wet races, allow any strategy (dry, intermediate, or wet tyres)
        wet_df = full_df[(full_df['IsWet'] == 1) & (full_df['Strategy'].str.contains('W|I'))]

        models = {}
        encoders = {}

        for condition, df in [('dry', dry_df), ('wet', wet_df)]:
            print(f"  -> Training {condition} model on {len(df)} samples")

            # Define features to use
            features = [
                'Track', 'StartPosition', 'AvgStintLength', 'TempRange',
                'AirTemp', 'TrackTemp', 'IsWet', 'TrackSpeed',
                'TrackTypeEncoded', 'OvertakingDifficultyEncoded', 'NumPitStops'
            ]

            target = 'StrategyEncoded'

            # Split X and y
            X = df[features + ['FinishPosition', 'PositionChange']]  # keep FinishPosition for later
            y = df[target]

            # Now split
            X_train, X_test, y_train, y_test = train_test_split(
                X, y, test_size=0.2, random_state=42
            )

            # Drop forbidden columns from train
            X_train = X_train.drop(columns=['FinishPosition', 'PositionChange'])
            # Drop forbidden columns from test
            X_test = X_test.drop(columns=['FinishPosition', 'PositionChange'])

            # Define RandomForest parameters
            param_grid = {
                'model__n_estimators': [100, 300, 500],
                'model__max_depth': [None],
                'model__min_samples_split': [2, 5, 10],
                'model__max_features': [None],
                'model__bootstrap': [True, False]
            }

            # Pipeline
            pipeline = Pipeline([
                ('scaler', StandardScaler()),
                ('model', RandomForestClassifier(
                    random_state=42,
                    class_weight='balanced'
                ))
            ])

            # Grid search
            grid = GridSearchCV(
                pipeline,
                param_grid=param_grid,
                cv=3,
                n_jobs=-1,
                scoring='accuracy'
            )

            grid.fit(X_train, y_train)

            best_model = grid.best_estimator_
            y_pred = best_model.predict(X_test)
            acc = accuracy_score(y_test, y_pred)

            print(f"    {condition.capitalize()} RandomForest best accuracy: {acc:.3f} with {grid.best_params_}")

            models[condition] = best_model
            encoders[condition] = {
                'strategy_encoder': strategy_encoder,
                'track_type_encoder': track_type_encoder,
                'overtaking_diff_encoder': overtaking_diff_encoder
            }

        # Save everything
        self.model_data = {
            'dry_model': models['dry'],
            'wet_model': models['wet'],
            'dry_encoders': encoders['dry'],
            'wet_encoders': encoders['wet'],
            'features': features,
        }

        return self.model_data



    def predict_strategy(self, track, start_position, is_wet, air_temp, track_temp):
        if self.model_data is None:
            print("Model not trained yet. Training now...")
            self.train_model()

        # ── unpack things we saved after training ─────────────────────────────
        # Choose the correct model
        model_key = 'wet_model' if is_wet else 'dry_model'
        enc_key = 'wet_encoders' if is_wet else 'dry_encoders'

        model = self.model_data[model_key]
        encoders = self.model_data[enc_key]
        strategy_enc = encoders['strategy_encoder']
        track_type_enc = encoders['track_type_encoder']
        overtaking_enc = encoders['overtaking_diff_encoder']
        features_order = self.model_data['features']

        track_mapping = {
            1: (21.90, 274.53, 'high_speed', 'medium', 57),
            2: (20.52, 259.99, 'high_speed', 'medium', 58),
            3: (20.52, 259.99, 'high_speed', 'medium', 71),
            4: (22.37, 259.07, 'high_speed', 'medium', 51),
            5: (15.08, 263.30, 'high_speed', 'low', 66),
            6: (20.52, 259.99, 'high_speed', 'medium', 44),
            7: (20.52, 259.99, 'high_speed', 'medium', 71),
            8: (20.52, 259.99, 'high_speed', 'medium', 70),
            9: (20.52, 259.99, 'high_speed', 'medium', 56),
            10: (20.52, 259.99, 'high_speed', 'medium', 52),
            11: (20.52, 259.99, 'high_speed', 'medium', 70),
            12: (20.52, 259.99, 'high_speed', 'medium', 63),
            13: (20.52, 259.99, 'high_speed', 'medium', 53),
            14: (20.52, 259.99, 'high_speed', 'medium', 53),
            15: (18.55, 239.07, 'high_speed', 'medium', 50),
            16: (22.10, 280.69, 'high_speed', 'medium', 71),
            17: (23.49, 235.59, 'high_speed', 'medium', 57),
            18: (26.58, 219.80, 'medium_speed', 'high', 78),
            19: (20.52, 259.99, 'high_speed', 'medium', 72),
            20: (11.79, 261.35, 'high_speed', 'low', 57),
            21: (22.16, 280.60, 'high_speed', 'medium', 50),
            22: (27.37, 271.43, 'high_speed', 'high', 61),
            23: (20.52, 259.99, 'high_speed', 'medium', 66),
            24: (17.71, 213.35, 'medium_speed', 'low', 56)
        }

        # 📦 Get the correct track info
        avg_stint_length, track_speed, track_type, overtaking_diff, number_of_laps = track_mapping.get(track, (20, 250, 'medium_speed', 'medium', 60))

        # Encode TrackType and OvertakingDifficulty
        track_type_encoded = track_type_enc.transform([track_type])[0]
        overtaking_difficulty_encoded = overtaking_enc.transform([overtaking_diff])[0]


        track_max_pitstops = {
            1: 2, 2: 2, 3: 2, 4: 1, 5: 2, 6: 2, 7: 2, 8: 3, 9: 3, 10: 3,
            11: 2, 12: 1, 13: 2, 14: 3, 15: 2, 16: 2, 17: 2, 18: 1, 19: 2,
            20: 2, 21: 1, 22: 1, 23: 2, 24: 1
        }
        num_pit_stops = track_max_pitstops.get(track, 2)

        X = pd.DataFrame([{
            'Track': track,
            'StartPosition': start_position,
            'AvgStintLength': avg_stint_length,
            'TempRange': 8,
            'AirTemp': air_temp,
            'TrackTemp': track_temp,
            'IsWet': int(is_wet),
            'TrackSpeed': track_speed,
            'TrackTypeEncoded': track_type_encoded,
            'OvertakingDifficultyEncoded': overtaking_difficulty_encoded,
            'NumPitStops': num_pit_stops
        }])[features_order]

        probs = model.predict_proba(X)[0]
        class_labels = model.classes_

        # Build list of (strategy name, probability)
        candidates = []
        for idx, p in enumerate(probs):
            strat_encoded = class_labels[idx]
            strat = strategy_enc.inverse_transform([strat_encoded])[0]

            # Filter out Wet/Intermediate if dry race
            if not is_wet and ('W' in strat or 'I' in strat):
                continue

            candidates.append((strat, p))

        if is_wet:
            # Only keep strategies that START with W or I
            starting_wet_candidates = [c for c in candidates if c[0].split('-')[0] in ('W', 'I')]

            if starting_wet_candidates:
                # If we have strategies starting with Wet/Intermediate, prefer those
                candidates = starting_wet_candidates
            else:
                # Otherwise fallback to any available strategy (mixed conditions)
                pass

        # Sort candidates by probability
        candidates.sort(key=lambda x: x[1], reverse=True)

        # Return top 3
        best_strategy, best_confidence = candidates[0]
        alternative_strategies = candidates[1:5]

        return {
            'best_strategy': best_strategy,
            'best_strategy_confidence': best_confidence,
            'alternative_strategies': alternative_strategies,
            'prediction_timestamp': datetime.now().strftime('%Y-%m-%d %H:%M:%S')
        }


    def save_model(self, filename=MODEL_FILE):
        """Save the trained model to disk"""
        if self.model_data is None:
            print("No model to save. Training first...")
            self.train_model()

        # Ensure directory exists
        os.makedirs(os.path.dirname(filename), exist_ok=True)
        joblib.dump(self.model_data, filename)
        print(f"Model saved as {filename}")

    def load_model(self, filename=MODEL_FILE):
        """Load a trained model from disk"""
        self.model_data = joblib.load(filename)
        print(f"Model loaded from {filename}")
        return self.model_data

import pandas as pd

def main(track_number: int, start_position: int, is_wet: bool, air_temp: int, track_temp: int) -> str:
    print(f"[DEBUG] track_number: {track_number}")
    print(f"[DEBUG] start_position: {start_position}")
    print(f"[DEBUG] is_wet: {is_wet}")
    print(f"[DEBUG] air_temp: {air_temp}")
    print(f"[DEBUG] track_temp: {track_temp}")

    predictor = F1StrategyPredictor()
    try:
        predictor.load_model()
    except FileNotFoundError:
        predictor.train_model()
        predictor.save_model()

    result = predictor.predict_strategy(
        track=track_number,
        start_position=start_position,
        is_wet=is_wet,
        air_temp=air_temp,
        track_temp=track_temp
    )

    num_stops = result['best_strategy'].count('-')

    # Build dynamic alternative columns
    alternatives = {}
    for idx, (strategy, confidence) in enumerate(result['alternative_strategies']):
        alternatives[f'alternative_{idx+1}'] = strategy
        alternatives[f'confidence_{idx+1}'] = confidence

    # Format output into a simple one-row DataFrame
    data = {
        "best_strategy": result['best_strategy'],
        "confidence": result['best_strategy_confidence'],
        "num_stops": num_stops,
        **alternatives  # <<<<<<<< include all dynamic alternatives
    }

    df = pd.DataFrame([data])
    return df.to_csv(index=False)

