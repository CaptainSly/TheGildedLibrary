# The Gilded Library

The Gilded Library is a 3D RPG Focused Game Engine that adheres to a philosophy of "Eating its own Cooking", or in layman terms, the engine is COMPLETELY modular. 

The architecture of the engine is designed to fuse the Data Oriented approach of the Gamebryo/Creation Engine used by Bethesda for their Elder Scrolls and Fallout series of games, as well as the Object Oriented structure of game engines such as Godot/Unity/Unreal. 

You can see documentation about the different systems here:
    * [Mod System](readme_docs/Mod System.md)
    * [Plugin System](readme_docs/Plugin System.md)
    

## Current State

As of March 28, 2026 the engine is currently in the "Core Phase" of it's development cycle. The foundation has been laid out, and currently research is being done for the default records. After research is complete, development will move forwards toward implementing the default systems and rendering. 

The current road-map:
    - Record Layer (In Progress)
        * Core Record Hierarchy
        * Default RPG Centric Records
    - Core Engine
        * Asset Manager
        * Input System
        * Rendering Pipeline (OpenGL First, Vulkan afterwards)
        * Audio System (OpenAL)
        * Physics System (Bullet)
    - Game play + Server
        * Scene Graph
        * Script System
        * World System
        * Camera System
        * NPC/AI System
        * Dialogue System
        * Save System
        * Server - Headless Gameloop + Netty Networking
    - Editor
        * Record Editor
        * Scene Viewport
        * Model Viewer
        * Blender Mesh Export Pipeline
    - Launcher
        * Launcher Design
        
## 
