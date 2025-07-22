# AntiSnowballs

---

## Overview

AntiSnowballs is a mod that reduces lag and clutter by automatically removing snowball entities around the player. It includes commands to toggle the feature and track how many snowballs have been deleted.

---

## Features

- Automatically removes all snowballs within a 100-block radius every tick.
- Commands to toggle the feature, check deletion stats, and reset counters:
  - `/antisnowballs toggle`
  - `/antisnowballs count`
  - `/antisnowballs reset`

---

## Installation

1. Install Fabric Loader and Fabric API.
2. Download the mod `.jar` file.
3. Place the `.jar` file into your `mods` folder.
4. Launch the game with Fabric.

---

## Usage

- Use `/antisnowballs toggle` to enable or disable automatic snowball removal.
- Use `/antisnowballs count` to see how many snowballs were deleted last tick and in total.
- Use `/antisnowballs reset` to reset the deletion counters.

---

## Known Issues

- Snowball removal radius is fixed at 100 blocks (no config yet).
- All snowballs are removed indiscriminately, including those recently thrown.
- Deleted snowballs will respawn after chunks are reloaded or after relogging.
- Snowball count may double-count snowballs if chunks reload multiple times.

---

## Contributing

Feel free to fork the repo and submit pull requests! Suggestions and bug reports are welcome.

---
