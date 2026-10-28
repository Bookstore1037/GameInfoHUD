# Game Info HUD

在游戏画面上叠加显示自定义信息（坐标、FPS、血量、时间、天气、生物群系等）。本模组是
[InGameInfoXML](https://github.com/Lunatrius/InGameInfoXML) 的 1.20.1 Forge 移植版，原作者为
Jadran "Lunatrius" Kotnik，沿用与原作MIT许可。信息内容和排版完全由
一份 XML 配置文件决定，你可以自己动手写任意想要显示的内容。

---

## 一、配置文件在哪

首次启动游戏后，模组会自动生成一份默认示例配置：

```
config/gameinfohud/ingameinfo.xml
```

- 编辑保存后，在游戏里按 **F3 + T** 重载资源即可生效，或直接重启游戏。
- 默认在"控制"设置里给「切换 HUD 显示」绑定一个按键，可随时开/关整个 HUD。
- 也支持同名 `.json` 和 `.txt` 配置文件（按扩展名自动识别），下面只讲 XML。
- 如果你装了中文语言包，可以放一份 `ingameinfo_zh_cn.xml`，中文界面下会自动优先加载它。

---

## 二、基础结构

一个最小配置长这样：

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<config>
    <lines at="topleft">
        <line>
            <str>血量: </str>
            <var>health</var>
            <str> / </str>
            <var>maxhealth</var>
        </line>
    </lines>
</config>
```

解释：

- `<config>` 是根节点。
- `<lines at="...">` 定义一组渲染在某个位置的行，`at` 决定位置（九宫格，见下）。
- `<line>` 是一行，里面按顺序放若干个「值」，从左到右拼成一行文本。
- `<str>` 是普通文本，`<var>` 是引用一个标签（tag，见第五节）。

> 值也可以用 `<value type="str">文本</value>` 的写法；写成 `<str>文本</str>` 只是简写，
> 两者等价。

---

## 三、九宫格对齐（at 的取值）

`at` 可以是以下 9 个值（小写）：

| `at` 值 | 位置 |
|---|---|
| `topleft` | 左上 |
| `topcenter` | 顶部居中 |
| `topright` | 右上 |
| `middleleft` | 左中 |
| `middlecenter` | 正中 |
| `middleright` | 右中 |
| `bottomleft` | 左下 |
| `bottomcenter` | 底部居中 |
| `bottomright` | 右下 |

同一位置可以写多个 `<lines>`，也可以在一个 `<lines>` 里写多个 `<line>`（一行为一条）。

---

## 四、颜色与格式代码

在 `<str>` 文本里用 `$` 加一个字符来上色/加格式（会被替换成 Minecraft 的 `§` 代码）：

| 代码 | 效果 |
|---|---|
| `$0` ~ `$9` | 黑 / 深蓝 / 深绿 / 深青 / 深红 / 深紫 / 金 / 灰 / 深灰 / 蓝 |
| `$a` `$b` `$c` `$d` `$e` `$f` | 绿 / 青 / 红 / 浅紫 / 黄 / 白 |
| `$k` | 乱码 |
| `$l` | 粗体 |
| `$m` | 删除线 |
| `$n` | 下划线 |
| `$o` | 斜体 |
| `$r` | 重置 |

例：`<str>$e血量$f: </str>` 会显示成黄色的"血量"加白色的冒号。

---

## 五、标签（tag）一览

`<var>标签名</var>` 会输出对应标签的值。括号里的是别名（可等价使用）。

### 玩家通用（playergeneral）

| 标签 | 说明 |
|---|---|
| `health`（`healthpoints`） | 生命值 |
| `maxhealth`（`maxhealthpoints`） | 最大生命 |
| `armor`（`armorpoints`） | 护甲值 |
| `foodlevel`（`foodpoints`） | 饥饿值 |
| `saturation`（`foodsaturation`） | 饱和度 |
| `exhaustion`（`foodexhaustion`） | 消耗度 |
| `airticks` / `maxairticks` | 氧气 / 最大氧气 |
| `playerlevel` | 等级 |
| `xpthislevel` | 当前等级已获得经验 |
| `xpuntilnext` | 距下一级还差多少经验 |
| `xpcap` | 升到下一级所需总经验 |
| `score` | 计分板分数 |
| `gamemode` / `gamemodeid` | 游戏模式名 / ID |
| `username` | 玩家名 |
| `light` | 光照等级（方块+天空，0~15） |
| `lightfeet` | 脚下光照 |
| `lightnosun` / `lightnosunfeet` | 仅方块光照 / 脚下 |
| `lightsun` / `lightsunfeet` | 仅天空光照 / 脚下 |
| `lightlabel` `skylabel` `blocklabel` `feetlabel` | 光照相关文字标签 |
| `inwater`（`underwater`）`wet` `alive` `burning` `riding` `sneaking` `sprinting` `invisible` `eating` `invulnerable` | 布尔状态，输出 `true`/`false` |

### 玩家位置（playerposition）

| 标签 | 说明 |
|---|---|
| `x`/`y`/`z`（别名 `xr`/`yr`/`zr`） | 坐标（两位小数） |
| `xi`/`yi`/`zi` | 坐标（整数） |
| `yfeet`（`yfeetr`）/`yfeeti` | 脚部 Y（小数 / 整数） |
| `chunkx`/`chunkz` | 所在区块坐标 |
| `chunkoffsetx`/`chunkoffsetz` | 区块内偏移（0~15） |
| `speed`/`speedx`/`speedy`/`speedz`/`speedxz` | 移动速度 |
| `direction` | 朝向角度 |
| `roughdirection` | 大致方向（南/西/北/东） |
| `finedirection` | 精确方向（8 向） |
| `abrroughdirection` / `abrfinedirection` | 简写方向（S/W/N/E 等） |
| `directionhud` | 方向 HUD 条 |
| `chunklabel` `offsetlabel` `facinglabel` | 坐标相关文字标签 |

### 世界（world）

| 标签 | 说明 |
|---|---|
| `worldname` | 已废弃，等同 `dimensionid`（返回维度 ID，如 `minecraft:overworld`） |
| `dimensionid` | 维度 ID（如 `minecraft:overworld`） |
| `dimension` | 维度名（本地化，如“主世界”） |
| `seed` | 世界种子 |
| `difficulty` / `difficultyid` | 难度名 / ID |
| `daytime` | 白天 / 黑夜 |
| `raining` `thundering` `snowing` | 下雨/雷雨/下雪（仅在该天气时输出，否则空） |
| `weather` | 当前天气（本地化，按 雷雨 > 下雪 > 下雨 > 晴天 判断） |
| `weatherlabel` `chunkslabel` `worldlabel` `biomelabel` | 天气/区块/世界/群系文字标签 |
| `biome` / `biomeid` | 生物群系名 / ID |
| `slimes` | 是否史莱姆区块（`true`/`false`） |
| `hardcore` | 是否极限模式 |
| `temphumidity` | 温度 + 湿度组合 |
| `humidity` | 湿度 |
| `loadedchunks` | 已加载区块数 |

### 时间（time）

| 标签 | 说明 |
|---|---|
| `worldtimetotal` | 世界总时长（时分秒） |
| `day` | 天数 |
| `mctimeh` / `mctimem` | MC 时 / 分 |
| `mctime24`（`mctime`）/ `mctime12` | MC 时间 24/12 小时制 |
| `rltime24`（`rltime`）/ `rltime12` | 现实时间 24/12 小时制 |
| `mcdate` | MC 日期 |

### 杂项（misc）

| 标签 | 说明 |
|---|---|
| `memmax` / `memtotal` / `memfree` / `memused` | 内存（最大/已分配/空闲/已用） |
| `fps` | 帧率 |
| `ping` | 延迟 |
| `server` / `servername` / `serverip` / `serverport` | 服务器信息 |
| `entitiesline` | 实体数（已渲染 / 总数） |

### 鼠标指向（mouseover）

`mouseovername`（指向方块/实体的名字）、`mouseoveruniquename`（注册名）、`mouseoverid`（ID）、
`mouseovermetadata`（元数据）、`mouseoverpowerweak` / `mouseoverpowerstrong` / `mouseoverpowerinput`（红石信号）。

### 附近玩家（nearbyplayer）

`nearbyplayername0` ~ `nearbyplayername15`（第 N 近的玩家名）、
`nearbyplayerdistance0` ~ `nearbyplayerdistance15`（第 N 近玩家的距离）。

### 装备（playerequipment）

前缀取 `equipped` / `helmet` / `chestplate` / `leggings` / `boots`，后缀：

`name`（名字）、`uniquename`（注册名）、`damage`（已损耗）、`maxdamage`（最大耐久）、
`damageleft`（剩余耐久）、`itemmeta`、`itemid`、`quantity`（背包里该物品数量）、
`icon` / `largeicon`（装备图标）。

例：`equippedname`、`equippeddamageleft`、`helmeticon`、`bootsmaxdamage`。

### 药水效果（playerpotion）

`potioneffect0` ~ `potioneffect31`（效果名）、`potionduration0` ~ `31`（剩余时长）、
`potiondurationticks0` ~ `31`（剩余 tick）、`potionnegative0` ~ `31`（是否负面效果）。

### 骑乘（riding）

`ridinghorse`（是否骑马）、`horsehealth` / `horsemaxhealth` / `horsespeed` /
`horsejumpstrength`（`horsejumpstr`）。

### 格式化（formatting）

输出 `§` 颜色/格式码，等价于第四节里的 `$` 代码（一般直接在 `<str>` 里用 `$` 更方便）：

`black` `darkblue` `darkgreen` `darkaqua` `darkred` `darkpurple` `gold` `gray` `darkgrey`
`blue` `green` `aqua` `red` `lightpurple` `yellow` `white` `obfuscated` `bold`
`strikethrough` `underline` `italic` `reset`。

---

## 六、字符串内嵌变量

在 `<str>` 文本里可以直接用 `{标签名}` 插入某个标签的值，无需拆成多个 `<var>`：

```xml
<line>
    <str>坐标: {x}, {y}, {z}</str>
</line>
```

等价于：

```xml
<line>
    <str>坐标: </str><var>x</var>
    <str>, </str><var>y</var>
    <str>, </str><var>z</var>
</line>
```

---

## 七、复合值 / 逻辑 / 数学

除了 `<str>`/`<num>`/`<var>`，还可以用下面这些「复合值」。它们把子值作为参数。

### `<concat>` — 拼接

把子值按顺序拼起来（`<str>`/`<var>` 本身已经会拼接，这个一般用不到）。

### `<if>` — 条件

```xml
<if>
    <var>sneaking</var>   <!-- 条件：true/false -->
    <str>正在潜行</str>     <!-- 条件为真 -->
    <str>站立</str>         <!-- 条件为假（可省略） -->
</if>
```

条件通常来自布尔标签（`sneaking`、`slimes` 等）或逻辑值（`<and>`、`<greater>` 等）。

### `<max>` / `<min>` — 最大值 / 最小值，或二选一

两种用法：

1. 两个数字参数：返回较大/较小的那个数。

```xml
<max><num>3</num><num>5</num></max>   <!-- 输出 5 -->
```

2. 四个参数：比较前两个数，返回后两个字符串之一。

```xml
<max>
    <var>equippeddamageleft</var>
    <num>0.5</num>
    <str>有耐久</str>
    <str>没耐久</str>
</max>
```

### `<op>` / `<operation>` — 区间比较输出

格式：`<str>比较符</str>` + 基准值 + 若干组「阈值 + 输出」，末尾可加一个默认输出。

比较符：`gt`（>）、`lt`（<）、`ge`（>=）、`le`（<=）、`eq`（=）、`ne`（≠）。

```xml
<op>
    <str>ge</str>          <!-- 大于等于 -->
    <var>fps</var>         <!-- 基准值 -->
    <num>60</num><str>$a</str>   <!-- fps>=60 输出绿色 -->
    <num>30</num><str>$e</str>   <!-- fps>=30 输出黄色 -->
    <str>$c</str>                <!-- 都不满足输出红色 -->
</op>
```

### 逻辑值（配合 `<if>` 使用，输出 `true`/`false`）

- `<not>`：取反（1 个参数）。
- `<and>` / `<or>` / `<xor>`：多个参数的与 / 或 / 异或。
- `<greater>`（`more`）/ `<lesser>`（`less`）：链式比较（如 a>b>c）。
- `<equal>`（`equals`）：是否全部相等。
- `<contains>`（`in`）：第一个值是否等于后面任一个。

例：

```xml
<if>
    <greater><var>fps</var><num>60</num></greater>
    <str>流畅</str>
    <str>卡顿</str>
</if>
```

### 数学

| 值 | 说明 |
|---|---|
| `<add>` | 加 |
| `<sub>` | 减 |
| `<mul>` | 乘 |
| `<div>` | 除 |
| `<mod>` | 取余（浮点） |
| `<modi>` | 取余（整数） |
| `<pct>` | 百分比（a/b*100） |
| `<round>` | 保留小数（第 1 个参数是值，第 2 个是保留位数） |

例：

```xml
<round>
    <pct><var>xpthislevel</var><var>xpcap</var></pct>
    <num>1</num>
</round>
```

### 其它

- `<itemquantity>`：统计背包里某物品数量。`<itemquantity><str>minecraft:diamond</str></itemquantity>`。
- `<trans>`（`translate`）：翻译某个本地化 key。`<trans><str>key</str><var>参数1</var></trans>`。
- `<formattedtime>`（`rltimef`）：按格式输出现实时间。`<formattedtime><str>HH:mm:ss</str></formattedtime>`。
- `<file>`：读取配置目录下某文件的第一行。`<file><str>note.txt</str></file>`。

---

## 八、图标（icon）

`<icon>` 可以在行内渲染物品图标：

```xml
<icon><str>minecraft:diamond_sword</str></icon>
```

装备图标直接引用标签即可：

```xml
<var>equippedicon</var>  <var>helmeticon</var>
```

---

## 九、完整示例

```xml
<?xml version="1.0" encoding="UTF-8" standalone="no"?>
<config>
    <!-- 左上：玩家信息 -->
    <lines at="topleft">
        <line>
            <str>$e{username}$f  $aFPS$f: </str>
            <var>fps</var>
            <str>  $aPing$f: </str>
            <var>ping</var>
        </line>
        <line>
            <str>血量: $c</str>
            <var>health</var>
            <str>$f / </str>
            <var>maxhealth</var>
            <str>  饥饿: </str>
            <var>foodlevel</var>
        </line>
        <line>
            <str>坐标: {x}, {y}, {z}</str>
        </line>
    </lines>

    <!-- 右上：时间与天气 -->
    <lines at="topright">
        <line>
            <var>rltime24</var>
            <str>  </str>
            <var>mcdate</var>
        </line>
        <line>
            <str>天气: </str>
            <var>weather</var>
        </line>
    </lines>

    <!-- 左下：装备耐久 -->
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

## 十、常见问题

- **改了配置没反应？** 保存后按 F3 + T，或重启游戏。
- **想关掉某个区域？** 直接把对应的 `<lines>` 块删掉即可。
- **标签名记不住？** 用 `<var>` 时标签名不区分大小写，别名也能用。
- **写错了？** 出错的那一行会被跳过或显示 `<ERROR>`，检查该行标签名和 XML 是否闭合。

---

## 许可 / 版权

本模组是 [InGameInfoXML](https://github.com/Lunatrius/InGameInfoXML) 的 1.20.1 Forge 移植版。
原模组作者：Jadran "Lunatrius" Kotnik，依据 MIT License 发布。

本项目沿用相同许可，版权声明与许可文本见仓库根目录的 `LICENSE.txt`。
