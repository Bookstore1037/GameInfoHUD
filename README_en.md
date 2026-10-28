# Game Info HUD

Overlays customizable information on the game screen (coordinates, FPS, health, time, weather, biome, etc.).
This is a **1.20.1 Forge port** of [InGameInfoXML](https://github.com/Lunatrius/InGameInfoXML), originally created by
Jadran "Lunatrius" Kotnik and released under the MIT License. What you see and how it's laid out is fully controlled
by a single XML configuration file — you can build any overlay you want.

---

## 1. Where is the config file?

On first launch, the mod generates a default sample config:

```
config/gameinfohud/ingameinfo.xml
```

- After editing, press **F3 + T** in-game to reload resources, or just restart the game.
- A keybind for "Toggle HUD" is registered in Controls by default — use it to show/hide the whole overlay anytime.
- `.json` and `.txt` configs with the same base name are also supported (detected by extension). This document only covers XML.
- If you use a Chinese language pack, you can drop a `ingameinfo_zh_cn.xml` next to it — it will be loaded first when Chinese is selected.

---

## 2. Basic structure

A minimal config looks like this:

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<config>
    <lines at="topleft">
        <line>
            <str>HP: </str>
            <var>health</var>
            <str> / </str>
            <var>maxhealth</var>
        </line>
    </lines>
</config>
```

Explanation:

- `<config>` is the root node.
- `<lines at="...">` groups lines rendered at a given position (3×3 grid, see below).
- `<line>` is a single line — values inside are concatenated left to right into one string.
- `<str>` is literal text, `<var>` references a tag (see section 5).

> Values can also be written as `<value type="str">text</value>` — `<str>text</str>` is just shorthand; both are equivalent.

---

## 3. Nine-grid alignment (`at` values)

`at` accepts the following 9 values (lowercase):

| `at` value | Position |
|---|---|
| `topleft` | Top-left |
| `topcenter` | Top-center |
| `topright` | Top-right |
| `middleleft` | Middle-left |
| `middlecenter` | Center |
| `middleright` | Middle-right |
| `bottomleft` | Bottom-left |
| `bottomcenter` | Bottom-center |
| `bottomright` | Bottom-right |

You can have multiple `<lines>` at the same position, and multiple `<line>` inside one `<lines>` (one line per entry).

---

## 4. Color and formatting codes

Inside `<str>` text, use `$` followed by a character to apply color/formatting (it is translated to Minecraft `§` codes):

| Code | Effect |
|---|---|
| `$0` ~ `$9` | Black / Dark Blue / Dark Green / Dark Aqua / Dark Red / Dark Purple / Gold / Gray / Dark Gray / Blue |
| `$a` `$b` `$c` `$d` `$e` `$f` | Green / Aqua / Red / Light Purple / Yellow / White |
| `$k` | Obfuscated |
| `$l` | Bold |
| `$m` | Strikethrough |
| `$n` | Underline |
| `$o` | Italic |
| `$r` | Reset |

Example: `<str>$eHP$f: </str>` renders "HP" in yellow and the colon in white.

---

## 5. Tag reference

`<var>tagname</var>` outputs the value of the corresponding tag. Aliases are shown in parentheses.

### Player (General) — `playergeneral`

| Tag | Description |
|---|---|
| `health` (`healthpoints`) | Current health |
| `maxhealth` (`maxhealthpoints`) | Max health |
| `armor` (`armorpoints`) | Armor value |
| `foodlevel` (`foodpoints`) | Hunger |
| `saturation` (`foodsaturation`) | Saturation |
| `exhaustion` (`foodexhaustion`) | Exhaustion |
| `airticks` / `maxairticks` | Air / max air |
| `playerlevel` | XP level |
| `xpthislevel` | XP gained in current level |
| `xpuntilnext` | XP remaining to next level |
| `xpcap` | Total XP needed for next level |
| `score` | Scoreboard score |
| `gamemode` / `gamemodeid` | Game mode name / id |
| `username` | Player name |
| `light` | Total light level (block + sky, 0–15) |
| `lightfeet` | Light level at feet |
| `lightnosun` / `lightnosunfeet` | Block light only / at feet |
| `lightsun` / `lightsunfeet` | Sky light only / at feet |
| `lightlabel` `skylabel` `blocklabel` `feetlabel` | Localized labels for light info |
| `inwater` (`underwater`) `wet` `alive` `burning` `riding` `sneaking` `sprinting` `invisible` `eating` `invulnerable` | Boolean states, output `true`/`false` |

### Player (Position) — `playerposition`

| Tag | Description |
|---|---|
| `x`/`y`/`z` (aliases `xr`/`yr`/`zr`) | Coordinates (2 decimals) |
| `xi`/`yi`/`zi` | Coordinates (integer) |
| `yfeet` (`yfeetr`) / `yfeeti` | Feet Y (float / integer) |
| `chunkx`/`chunkz` | Chunk coordinates |
| `chunkoffsetx`/`chunkoffsetz` | Offset within chunk (0–15) |
| `speed`/`speedx`/`speedy`/`speedz`/`speedxz` | Movement speed |
| `direction` | Facing angle |
| `roughdirection` | Cardinal direction (S/W/N/E) |
| `finedirection` | 8-way direction |
| `abrroughdirection` / `abrfinedirection` | Abbreviated direction (S/W/N/E etc.) |
| `directionhud` | Direction HUD bar |
| `chunklabel` `offsetlabel` `facinglabel` | Localized labels for position info |

### World — `world`

| Tag | Description |
|---|---|
| `worldname` | Deprecated; same as `dimensionid` (returns dimension ID, e.g. `minecraft:overworld`) |
| `dimensionid` | Dimension ID (e.g. `minecraft:overworld`) |
| `dimension` | Localized dimension name (e.g. "Overworld") |
| `seed` | World seed |
| `difficulty` / `difficultyid` | Difficulty name / id |
| `daytime` | Day / Night |
| `raining` `thundering` `snowing` | Rain / Thunder / Snow (only outputs while that weather is active, empty otherwise) |
| `weather` | Current weather (localized; priority: Thunder > Snow > Rain > Clear) |
| `weatherlabel` `chunkslabel` `worldlabel` `biomelabel` | Localized labels for weather/chunks/world/biome |
| `biome` / `biomeid` | Biome name / ID |
| `slimes` | Slime chunk? (`true`/`false`) |
| `hardcore` | Hardcore mode? |
| `temphumidity` | Combined temperature + humidity |
| `humidity` | Humidity |
| `loadedchunks` | Number of loaded chunks |

### Time — `time`

| Tag | Description |
|---|---|
| `worldtimetotal` | Total world time (h:m:s) |
| `day` | Day number |
| `mctimeh` / `mctimem` | MC hour / minute |
| `mctime24` (`mctime`) / `mctime12` | MC time in 24h / 12h format |
| `rltime24` (`rltime`) / `rltime12` | Real time in 24h / 12h format |
| `mcdate` | MC date |

### Misc — `misc`

| Tag | Description |
|---|---|
| `memmax` / `memtotal` / `memfree` / `memused` | Memory (max / allocated / free / used) |
| `fps` | FPS |
| `ping` | Ping |
| `server` / `servername` / `serverip` / `serverport` | Server info |
| `entitiesline` | Entity count (rendered / total) |

### Mouse Over — `mouseover`

`mouseovername` (block/entity name), `mouseoveruniquename` (registry name), `mouseoverid` (id),
`mouseovermetadata` (metadata), `mouseoverpowerweak` / `mouseoverpowerstrong` / `mouseoverpowerinput` (redstone signal).

### Nearby Players — `nearbyplayer`

`nearbyplayername0` ~ `nearbyplayername15` (name of the N-th nearest player),
`nearbyplayerdistance0` ~ `nearbyplayerdistance15` (distance to the N-th nearest player).

### Equipment — `playerequipment`

Prefix: `equipped` / `helmet` / `chestplate` / `leggings` / `boots`. Suffix:

`name`, `uniquename` (registry name), `damage` (damage taken), `maxdamage` (max durability),
`damageleft` (remaining durability), `itemmeta`, `itemid`, `quantity` (count in inventory),
`icon` / `largeicon` (item icon).

Examples: `equippedname`, `equippeddamageleft`, `helmeticon`, `bootsmaxdamage`.

### Potion Effects — `playerpotion`

`potioneffect0` ~ `potioneffect31` (effect name), `potionduration0` ~ `31` (remaining duration),
`potiondurationticks0` ~ `31` (remaining ticks), `potionnegative0` ~ `31` (is negative effect).

### Riding — `riding`

`ridinghorse` (is riding a horse), `horsehealth` / `horsemaxhealth` / `horsespeed` /
`horsejumpstrength` (`horsejumpstr`).

### Formatting — `formatting`

Outputs `§` color/format codes; equivalent to the `$` codes in section 4 (usually easier to just use `$` in `<str>`):

`black` `darkblue` `darkgreen` `darkaqua` `darkred` `darkpurple` `gold` `gray` `darkgrey`
`blue` `green` `aqua` `red` `lightpurple` `yellow` `white` `obfuscated` `bold`
`strikethrough` `underline` `italic` `reset`.

---

## 6. Inline variables in strings

You can embed tag values directly in `<str>` text via `{tagname}`, without splitting into multiple `<var>`s:

```xml
<line>
    <str>Coords: {x}, {y}, {z}</str>
</line>
```

Equivalent to:

```xml
<line>
    <str>Coords: </str><var>x</var>
    <str>, </str><var>y</var>
    <str>, </str><var>z</var>
</line>
```

---

## 7. Composite values / logic / math

Besides `<str>`/`<num>`/`<var>`, the following "composite values" are available. They take sub-values as arguments.

### `<concat>` — concatenation

Concatenates sub-values in order (`<str>`/`<var>` already concatenate; rarely needed).

### `<if>` — conditional

```xml
<if>
    <var>sneaking</var>   <!-- condition: true/false -->
    <str>Sneaking</str>   <!-- if true -->
    <str>Standing</str>   <!-- if false (optional) -->
</if>
```

The condition is typically a boolean tag (`sneaking`, `slimes`, etc.) or a logic value (`<and>`, `<greater>`, etc.).

### `<max>` / `<min>` — max / min, or two-way choose

Two usages:

1. Two numeric arguments: returns the larger/smaller one.

```xml
<max><num>3</num><num>5</num></max>   <!-- outputs 5 -->
```

2. Four arguments: compares the first two numbers and returns the 3rd/4th string.

```xml
<max>
    <var>equippeddamageleft</var>
    <num>0.5</num>
    <str>Has durability</str>
    <str>No durability</str>
</max>
```

### `<op>` / `<operation>` — range comparison output

Format: `<str>op</str>` + base value + several "threshold + output" pairs + optional default output.

Operators: `gt` (>), `lt` (<), `ge` (>=), `le` (<=), `eq` (=), `ne` (≠).

```xml
<op>
    <str>ge</str>          <!-- greater or equal -->
    <var>fps</var>         <!-- base value -->
    <num>60</num><str>$a</str>   <!-- fps>=60 → green -->
    <num>30</num><str>$e</str>   <!-- fps>=30 → yellow -->
    <str>$c</str>                <!-- otherwise → red -->
</op>
```

### Logic values (used with `<if>`, output `true`/`false`)

- `<not>`: negation (1 argument).
- `<and>` / `<or>` / `<xor>`: logical AND / OR / XOR over multiple arguments.
- `<greater>` (`more`) / `<lesser>` (`less`): chained comparison (e.g. a>b>c).
- `<equal>` (`equals`): all equal.
- `<contains>` (`in`): first value equals any of the rest.

Example:

```xml
<if>
    <greater><var>fps</var><num>60</num></greater>
    <str>Smooth</str>
    <str>Laggy</str>
</if>
```

### Math

| Value | Description |
|---|---|
| `<add>` | Add |
| `<sub>` | Subtract |
| `<mul>` | Multiply |
| `<div>` | Divide |
| `<mod>` | Modulo (float) |
| `<modi>` | Modulo (integer) |
| `<pct>` | Percentage (a/b*100) |
| `<round>` | Round (first arg is value, second is decimal places) |

Example:

```xml
<round>
    <pct><var>xpthislevel</var><var>xpcap</var></pct>
    <num>1</num>
</round>
```

### Others

- `<itemquantity>`: count an item in the inventory. `<itemquantity><str>minecraft:diamond</str></itemquantity>`.
- `<trans>` (`translate`): translate a localization key. `<trans><str>key</str><var>arg1</var></trans>`.
- `<formattedtime>` (`rltimef`): format real time. `<formattedtime><str>HH:mm:ss</str></formattedtime>`.
- `<file>`: read the first line of a file in the config directory. `<file><str>note.txt</str></file>`.

---

## 8. Icons

`<icon>` renders an item icon inline:

```xml
<icon><str>minecraft:diamond_sword</str></icon>
```

Equipment icons can be referenced directly:

```xml
<var>equippedicon</var>  <var>helmeticon</var>
```

---

## 9. Full example

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<config>
    <!-- Top-left: player info -->
    <lines at="topleft">
        <line>
            <str>$e{username}$f  $aFPS$f: </str>
            <var>fps</var>
            <str>  $aPing$f: </str>
            <var>ping</var>
        </line>
        <line>
            <str>HP: $c</str>
            <var>health</var>
            <str>$f / </str>
            <var>maxhealth</var>
            <str>  Hunger: </str>
            <var>foodlevel</var>
        </line>
        <line>
            <str>Coords: {x}, {y}, {z}</str>
        </line>
    </lines>

    <!-- Top-right: time & weather -->
    <lines at="topright">
        <line>
            <var>rltime24</var>
            <str>  </str>
            <var>mcdate</var>
        </line>
        <line>
            <str>Weather: </str>
            <var>weather</var>
        </line>
    </lines>

    <!-- Bottom-left: equipment durability -->
    <lines at="bottomleft">
        <line>
            <var>equippedicon</var>
            <str> </str>
            <var>equippedname</var>
        </line>
    </lines>
</config>
```

---

## 10. FAQ

- **Config changes not showing?** Press F3 + T after saving, or restart the game.
- **Want to hide an area?** Just delete the corresponding `<lines>` block.
- **Can't remember tag names?** Tag names in `<var>` are case-insensitive; aliases work too.
- **Something went wrong?** A broken line is skipped or shows `<ERROR>`. Check tag names and XML closure on that line.

---

## License / Credits

This mod is a 1.20.1 Forge port of [InGameInfoXML](https://github.com/Lunatrius/InGameInfoXML).
Original author: Jadran "Lunatrius" Kotnik, released under the MIT License.

This project is distributed under the same license. See `LICENSE.txt` in the repository root for the copyright notice and license text.
