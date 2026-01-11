# Troubleshooting: Textures Not Showing In-Game

If you've added a texture but don't see it in-game, try these steps:

## Step 1: Rebuild the Mod
After adding or changing texture files, you **must rebuild** the mod:

1. **In your IDE (IntelliJ/Eclipse)**:
   - Run the `build` task
   - Or use: `./gradlew build` in terminal

2. **Or use Gradle command**:
   ```bash
   ./gradlew build
   ```

## Step 2: Restart the Game
- **Completely close** Minecraft
- **Restart** the game
- Textures are loaded when the game starts, not while it's running

## Step 3: Clear Resource Cache (If Still Not Working)
1. Close Minecraft
2. Delete the cache folder:
   - Windows: `%appdata%\.minecraft\assets\`
   - Or just delete the `run` folder in your project
3. Restart Minecraft

## Step 4: Check Texture File
- **File name must match exactly**: `electric_toothbrush.png` (case-sensitive)
- **File location**: `src/main/resources/assets/teethdweller/textures/item/electric_toothbrush.png`
- **File format**: PNG (not JPG or other formats)
- **File size**: Should be readable (your 80KB file is fine, even if large)

## Step 5: Verify Item is in Creative Menu
The electric toothbrush should appear in:
- **Creative Menu** → **Tools** tab
- Look for "Electric Toothbrush"

## Step 6: Use Give Command (For Testing)
If you want to test without creative menu:
```
/give @s teethdweller:electric_toothbrush
```

## Step 7: Check Console for Errors
When starting the game, check the console/logs for:
- Texture loading errors
- Resource pack errors
- Any red error messages

## Common Issues

### Purple/Black Checkered Texture
- **Cause**: Texture file not found or wrong path
- **Fix**: Verify file name and location match exactly

### Item Shows Default Texture
- **Cause**: Model file not loading or wrong texture path
- **Fix**: Check `models/item/electric_toothbrush.json` has correct path

### Item Not in Creative Menu
- **Cause**: Item group registration issue
- **Fix**: Check that `ModItems.initialize()` is called in `TeethDweller.java`

## Quick Checklist
- [ ] Texture file exists: `textures/item/electric_toothbrush.png`
- [ ] Model file exists: `models/item/electric_toothbrush.json`
- [ ] Rebuilt the mod after adding texture
- [ ] Restarted Minecraft completely
- [ ] Checked Tools tab in creative menu
- [ ] No errors in console/logs

