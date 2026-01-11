@echo off
echo Regenerating Dentist Office Structure NBT...
echo.

REM Check if Python is installed
python --version >nul 2>&1
if errorlevel 1 (
    echo ERROR: Python is not installed or not in PATH
    echo.
    echo Please install Python from https://www.python.org/
    echo OR create the structure manually using Structure Blocks in-game
    echo See HOW_TO_CREATE_STRUCTURE_NBT.md for instructions
    pause
    exit /b 1
)

REM Check if nbtlib is installed
python -c "import nbtlib" >nul 2>&1
if errorlevel 1 (
    echo Installing nbtlib...
    pip install nbtlib
    if errorlevel 1 (
        echo ERROR: Failed to install nbtlib
        pause
        exit /b 1
    )
)

REM Run the structure generation script
echo Running structure generation script...
python generate_structure.py

if errorlevel 1 (
    echo ERROR: Structure generation failed
    pause
    exit /b 1
)

echo.
echo SUCCESS! Structure NBT file has been created.
echo.
echo Next steps:
echo 1. Run: gradlew build
echo 2. Install the mod in Minecraft
echo 3. Create a NEW world
echo 4. Use: /locate structure teethdweller:dentist_office
echo.
pause

