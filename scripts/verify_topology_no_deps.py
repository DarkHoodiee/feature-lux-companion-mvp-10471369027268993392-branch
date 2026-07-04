import math

def build_eye_path_points(geom, num_points=40):
    w, h = geom['width'], geom['height']
    halfW, halfH = w / 2.0, h / 2.0
    kBase = 0.5522 * geom.get('softness', 1.0)
    kUpper = kBase * geom.get('upperCurve', 1.0)
    kLower = kBase * geom.get('lowerCurve', 1.0)
    innerTaper = geom.get('innerTaper', 0.0)
    outerExpansion = geom.get('outerExpansion', 0.0)

    innerKMult = 1.0 - (innerTaper * 0.6)
    outerKMult = 1.0 + (outerExpansion * 0.4)

    def cubic_bezier(p0, p1, p2, p3, t):
        return (1-t)**3 * p0 + 3*(1-t)**2 * t * p1 + 3*(1-t) * t**2 * p2 + t**3 * p3

    points = []

    # Q1: Top-Right (Outer Upper)
    for i in range(num_points // 4):
        t = i / (num_points // 4)
        x = cubic_bezier(0, halfW*kUpper*outerKMult, halfW, halfW, t)
        y = cubic_bezier(-halfH, -halfH, -halfH*kUpper*outerKMult, 0, t)
        points.append((x, y))

    # Q2: Bottom-Right (Outer Lower)
    for i in range(num_points // 4):
        t = i / (num_points // 4)
        x = cubic_bezier(halfW, halfW, halfW*kLower*outerKMult, 0, t)
        y = cubic_bezier(0, halfH*kLower*outerKMult, halfH, halfH, t)
        points.append((x, y))

    # Q3: Bottom-Left (Inner Lower)
    for i in range(num_points // 4):
        t = i / (num_points // 4)
        x = cubic_bezier(0, -halfW*kLower*innerKMult, -halfW, -halfW, t)
        y = cubic_bezier(halfH, halfH, halfH*kLower*innerKMult, 0, t)
        points.append((x, y))

    # Q4: Top-Left (Inner Upper)
    for i in range(num_points // 4):
        t = i / (num_points // 4)
        x = cubic_bezier(-halfW, -halfW, -halfW*kUpper*innerKMult, 0, t)
        y = cubic_bezier(0, -halfH*kUpper*innerKMult, -halfH, -halfH, t)
        points.append((x, y))

    return points

def plot_ascii(points, title):
    grid_size_x = 60
    grid_size_y = 30
    grid = [[' ' for _ in range(grid_size_x)] for _ in range(grid_size_y)]

    min_x, max_x = -120, 120
    min_y, max_y = -120, 120

    for x, y in points:
        ix = int((x - min_x) / (max_x - min_x) * (grid_size_x - 1))
        iy = int((y - min_y) / (max_y - min_y) * (grid_size_y - 1))
        if 0 <= ix < grid_size_x and 0 <= iy < grid_size_y:
            grid[iy][ix] = '*'

    print(f"\n--- {title} ---")
    for row in grid:
        print("".join(row))

# Presets from Phase B1 (New)
presets_new = {
    'NEUTRAL': {'width': 130, 'height': 165, 'upperCurve': 1.0, 'lowerCurve': 0.95, 'innerTaper': 0.4, 'outerExpansion': 0.15, 'softness': 1.0},
    'FOCUSED': {'width': 155, 'height': 80, 'upperCurve': 0.4, 'lowerCurve': 0.5, 'innerTaper': 0.5, 'outerExpansion': 0.1, 'softness': 0.8},
    'HAPPY': {'width': 135, 'height': 120, 'upperCurve': 1.3, 'lowerCurve': 0.2, 'innerTaper': 0.3, 'outerExpansion': 0.2, 'softness': 1.2},
}

# Old simulated
preset_old_neutral = {'width': 120, 'height': 160, 'upperCurve': 1.0, 'lowerCurve': 1.0, 'innerTaper': 0.0, 'outerExpansion': 0.0, 'softness': 1.0}

plot_ascii(build_eye_path_points(preset_old_neutral), "OLD NEUTRAL (Symmetric)")
plot_ascii(build_eye_path_points(presets_new['NEUTRAL']), "NEW NEUTRAL (Asymmetrical - Right Eye View)")
plot_ascii(build_eye_path_points(presets_new['FOCUSED']), "NEW FOCUSED")
plot_ascii(build_eye_path_points(presets_new['HAPPY']), "NEW HAPPY")
