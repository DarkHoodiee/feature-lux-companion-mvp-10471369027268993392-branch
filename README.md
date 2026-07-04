# LUX Hoodie

LUX Hoodie is a living digital character for Android, inspired by Pixar's EVE from WALL-E. It focuses on the "Illusion of Life" through expressive topology-driven rendering and a layered cognitive architecture.

## Architecture (v3.1)
The project follows a strict 7-layer hierarchy:
1. **Perception**: Filtered event routing (EventBus).
2. **Context**: Attention and short-term memory (AttentionSystem).
3. **Thought**: Motivation evaluation (BehaviorPlanner).
4. **Decision**: Goal-oriented planning (BehaviorExecutor).
5. **Behavior**: Intention sequencing.
6. **Animation**: Spring-based physics interpolation (FaceAnimator).
7. **Renderer**: Stateless hardware-first drawing (LuxFaceCanvas).

## Key Features
- **Canonical Neutral Eye**: Asymmetrical leaf-profile topology.
- **Hardware Startup**: Cinematic 7-phase boot sequence.
- **Hardware Raster**: Horizontal scanlines for display depth.
- **Dynamic Glow**: 5-layer emissive bloom model.

## Getting Started

### Prerequisites
- Android Studio Ladybug (or newer).
- JDK 17+.
- Android Device/Emulator (API 26+).

### Build & Run
1. Clone the repository.
2. Open in Android Studio.
3. Sync Gradle.
4. Press **Run**.

Alternatively, via command line:
```bash
./gradlew assembleDebug
```

## Documentation
- [Cognitive Architecture](LUX_Cognitive_Architecture_v4.md)
- [Goal System](Goal_System.md)
- [Memory Model](Memory_Model.md)
- [Motivation Model](Motivation_Model.md)

## License
MIT License. See [LICENSE](LICENSE) for details.
