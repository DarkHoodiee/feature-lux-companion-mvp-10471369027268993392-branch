import matplotlib.pyplot as plt
from matplotlib.path import Path
import matplotlib.patches as patches
import numpy as np

def build_eye_path_new(geom):
    w, h = geom['width'], geom['height']
    halfW, halfH = w / 2.0, h / 2.0
    kBase = 0.5522 * geom.get('softness', 1.0)
    kUpper = kBase * geom.get('upperCurve', 1.0)
    kLower = kBase * geom.get('lowerCurve', 1.0)
    innerKMult = 1.0 - (geom.get('innerTaper', 0.0) * 0.6)
    outerKMult = 1.0 + (geom.get('outerExpansion', 0.0) * 0.4)

    topXOffset = geom.get('shear', 0.0) * halfH
    bottomXOffset = -geom.get('shear', 0.0) * halfH

    topY, bottomY = -halfH, halfH
    leftX, rightX = -halfW, halfW

    verts = [
        (topXOffset, topY),
        # 1. Top-Right (Outer Upper)
        (topXOffset + halfW * kUpper * outerKMult, topY),
        (rightX, -halfH * kUpper * outerKMult),
        (rightX, 0.0),
        # 2. Bottom-Right (Outer Lower)
        (rightX, halfH * kLower * outerKMult),
        (bottomXOffset + halfW * kLower * outerKMult, bottomY),
        (bottomXOffset, bottomY),
        # 3. Bottom-Left (Inner Lower)
        (bottomXOffset - halfW * kLower * innerKMult, bottomY),
        (leftX, halfH * kLower * innerKMult),
        (leftX, 0.0),
        # 4. Top-Left (Inner Upper)
        (leftX, -halfH * kUpper * innerKMult),
        (topXOffset - halfW * kUpper * innerKMult, topY),
        (topXOffset, topY),
    ]

    codes = [
        Path.MOVETO,
        Path.CURVE4, Path.CURVE4, Path.CURVE4,
        Path.CURVE4, Path.CURVE4, Path.CURVE4,
        Path.CURVE4, Path.CURVE4, Path.CURVE4,
        Path.CURVE4, Path.CURVE4, Path.CURVE4,
    ]

    return Path(verts, codes)

def build_eye_path_old(geom):
    w, h = geom['width'], geom['height']
    halfW, halfH = w / 2.0, h / 2.0
    kUpper = 0.552 * geom.get('upperCurve', 1.0)
    kLower = 0.552 * geom.get('lowerCurve', 1.0)
    outerComp = geom.get('outerCompression', 0.0)
    innerComp = geom.get('innerCompression', 0.0)

    topXOffset = geom.get('shear', 0.0) * halfH
    bottomXOffset = -geom.get('shear', 0.0) * halfH

    topY, bottomY = -halfH, halfH
    leftX, rightX = -halfW, halfW

    verts = [
        (topXOffset, topY),
        # Top-Right
        (topXOffset + halfW * kUpper * (1.0 - outerComp * 0.5), topY),
        (rightX, -halfH * kUpper * (1.0 - outerComp * 0.2)),
        (rightX, 0.0),
        # Bottom-Right
        (rightX, halfH * kLower * (1.0 - outerComp * 0.2)),
        (bottomXOffset + halfW * kLower * (1.0 - outerComp * 0.5), bottomY),
        (bottomXOffset, bottomY),
        # Bottom-Left
        (bottomXOffset - halfW * kLower * (1.0 - innerComp * 0.5), bottomY),
        (leftX, halfH * kLower * (1.0 - innerComp * 0.2)),
        (leftX, 0.0),
        # Top-Left
        (leftX, -halfH * kUpper * (1.0 - innerComp * 0.2)),
        (topXOffset - halfW * kUpper * (1.0 - innerComp * 0.5), topY),
        (topXOffset, topY),
    ]

    codes = [
        Path.MOVETO,
        Path.CURVE4, Path.CURVE4, Path.CURVE4,
        Path.CURVE4, Path.CURVE4, Path.CURVE4,
        Path.CURVE4, Path.CURVE4, Path.CURVE4,
        Path.CURVE4, Path.CURVE4, Path.CURVE4,
    ]

    return Path(verts, codes)

# Presets from Phase A (Old)
presets_old = {
    'NEUTRAL': {'width': 120, 'height': 160, 'upperCurve': 1.0, 'lowerCurve': 1.0, 'innerCompression': 0.05, 'outerCompression': 0.05},
    'CURIOUS': {'width': 135, 'height': 180, 'upperCurve': 1.1, 'lowerCurve': 0.9, 'innerCompression': 0.0, 'outerCompression': 0.0},
    'FOCUSED': {'width': 150, 'height': 70, 'upperCurve': 0.4, 'lowerCurve': 0.4, 'innerCompression': 0.3, 'outerCompression': 0.3},
}

# Presets from Phase B1 (New)
presets_new = {
    'NEUTRAL': {'width': 130, 'height': 165, 'upperCurve': 1.0, 'lowerCurve': 0.95, 'innerTaper': 0.4, 'outerExpansion': 0.15, 'softness': 1.0},
    'CURIOUS': {'width': 140, 'height': 185, 'upperCurve': 1.2, 'lowerCurve': 0.9, 'innerTaper': 0.2, 'outerExpansion': 0.2, 'softness': 1.1},
    'FOCUSED': {'width': 155, 'height': 80, 'upperCurve': 0.4, 'lowerCurve': 0.5, 'innerTaper': 0.5, 'outerExpansion': 0.1, 'softness': 0.8},
    'HAPPY': {'width': 135, 'height': 120, 'upperCurve': 1.3, 'lowerCurve': 0.2, 'innerTaper': 0.3, 'outerExpansion': 0.2, 'softness': 1.2},
    'SLEEPY': {'width': 120, 'height': 60, 'upperCurve': 0.2, 'lowerCurve': 0.7, 'innerTaper': 0.3, 'outerExpansion': 0.1, 'softness': 0.9},
}

def render_comparison(name, old_geom, new_geom, filename):
    fig, (ax1, ax2, ax3) = plt.subplots(1, 3, figsize=(15, 5))

    # Old
    path_old = build_eye_path_old(old_geom)
    patch_old = patches.PathPatch(path_old, facecolor='none', edgecolor='blue', lw=2)
    ax1.add_patch(patch_old)
    ax1.set_title(f"Old {name}")
    ax1.set_xlim(-100, 100)
    ax1.set_ylim(-100, 100)
    ax1.set_aspect('equal')

    # New
    path_new = build_eye_path_new(new_geom)
    patch_new = patches.PathPatch(path_new, facecolor='none', edgecolor='green', lw=2)
    ax2.add_patch(patch_new)
    ax2.set_title(f"New {name}")
    ax2.set_xlim(-100, 100)
    ax2.set_ylim(-100, 100)
    ax2.set_aspect('equal')

    # Overlay (Mirroring for left eye simulation)
    # Right eye
    patch_new_r = patches.PathPatch(path_new, facecolor='green', alpha=0.3, lw=0)
    ax3.add_patch(patch_new_r)
    # Left eye (mirrored)
    verts_mirrored = [( -v[0], v[1]) for v in path_new.vertices]
    path_new_l = Path(verts_mirrored, path_new.codes)
    patch_new_l = patches.PathPatch(path_new_l, facecolor='green', alpha=0.3, lw=0)
    ax3.add_patch(patch_new_l)

    ax3.set_title(f"Overlay {name}")
    ax3.set_xlim(-200, 200)
    ax3.set_ylim(-150, 150)
    ax3.set_aspect('equal')

    plt.tight_layout()
    plt.savefig(filename)
    plt.close()

# Generate main comparisons
render_comparison("Neutral", presets_old['NEUTRAL'], presets_new['NEUTRAL'], "neutral_comp.png")
render_comparison("Curious", presets_old['CURIOUS'], presets_new['CURIOUS'], "curious_comp.png")
render_comparison("Focused", presets_old['FOCUSED'], presets_new['FOCUSED'], "focused_comp.png")

# Generate new-only renders for all
def render_single(name, new_geom, filename):
    fig, ax = plt.subplots(figsize=(5, 5))
    path_new = build_eye_path_new(new_geom)
    patch_new = patches.PathPatch(path_new, facecolor='#00A8E8', edgecolor='white', lw=1)
    ax.add_patch(patch_new)
    ax.set_title(f"LUX {name}")
    ax.set_xlim(-100, 100)
    ax.set_ylim(-100, 100)
    ax.set_aspect('equal')
    ax.axis('off')
    plt.savefig(filename, facecolor='black')
    plt.close()

for name, geom in presets_new.items():
    render_single(name, geom, f"render_{name.lower()}.png")

# Scale validation
def render_scale_validation(geom, scales, filename):
    fig, axes = plt.subplots(1, len(scales), figsize=(5*len(scales), 5))
    path = build_eye_path_new(geom)
    for i, scale in enumerate(scales):
        patch = patches.PathPatch(path, facecolor='#00A8E8', edgecolor='white', lw=1*scale)
        axes[i].add_patch(patch)
        axes[i].set_xlim(-100/scale, 100/scale)
        axes[i].set_ylim(-100/scale, 100/scale)
        axes[i].set_title(f"Scale {scale}x")
        axes[i].set_aspect('equal')
        axes[i].axis('off')
    plt.savefig(filename)
    plt.close()

render_scale_validation(presets_new['NEUTRAL'], [0.5, 1.0, 2.0], "scale_validation.png")

# Motion validation (interpolation)
def lerp_geom(g1, g2, t):
    res = {}
    for k in set(g1.keys()) | set(g2.keys()):
        res[k] = g1.get(k, 1.0) + (g2.get(k, 1.0) - g1.get(k, 1.0)) * t
    return res

def render_motion(g_start, g_end, frames, filename):
    fig, axes = plt.subplots(1, frames, figsize=(3*frames, 3))
    for i in range(frames):
        t = i / (frames - 1)
        g = lerp_geom(g_start, g_end, t)
        path = build_eye_path_new(g)
        patch = patches.PathPatch(path, facecolor='#00A8E8', alpha=0.8)
        axes[i].add_patch(patch)
        axes[i].set_xlim(-100, 100)
        axes[i].set_ylim(-100, 100)
        axes[i].set_aspect('equal')
        axes[i].axis('off')
    plt.savefig(filename)
    plt.close()

render_motion(presets_new['NEUTRAL'], presets_new['CURIOUS'], 5, "motion_neutral_curious.png")
render_motion(presets_new['CURIOUS'], presets_new['NEUTRAL'], 5, "motion_curious_neutral.png")
