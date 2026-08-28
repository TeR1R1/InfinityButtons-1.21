import json
import os
import shutil
from pathlib import Path

ROOT = Path(r"C:\Users\28424\Desktop\Playground\inf_button")
JAR = ROOT / "_jar"
DEST = ROOT / "src" / "main" / "resources"

SKIP_NAME_PARTS = (
    "create",
    "nethersdelight",
    "nether_brick_button_from_propelplant",
    "propelplant",
    "rose_quartz",
    "scoria",
    "limestone",
    "ochrum",
    "veridium",
    "crimsite",
    "asurine",
    "tuff_brick",  # keep? vanilla 1.21 has tuff bricks - jar 1.20.1 might have create tuff
)

# Overlay extra assets (cherry/bamboo etc.) from 1.20.1 jar
src_assets = JAR / "assets" / "infinitybuttons"
dst_assets = DEST / "assets" / "infinitybuttons"
if src_assets.exists():
    shutil.copytree(src_assets, dst_assets, dirs_exist_ok=True)

# icon
for cand in (JAR / "assets" / "infinitybuttons" / "icon.png", ROOT / "_upstream" / "src" / "main" / "resources" / "assets" / "infinitybuttons" / "icon.png"):
    if cand.exists():
        shutil.copy2(cand, DEST / "icon.png")
        shutil.copy2(cand, DEST / "assets" / "infinitybuttons" / "icon.png")
        break


def skip_path(p: Path) -> bool:
    s = str(p).replace("\\", "/").lower()
    return any(part in s for part in ("create", "nethersdelight", "nether's", "propelplant"))


def convert_recipe(obj):
    result = obj.get("result")
    if isinstance(result, dict) and "item" in result and "id" not in result:
        result["id"] = result.pop("item")
        if "count" not in result:
            result["count"] = 1
    return obj


def convert_advancement(obj):
    display = obj.get("display")
    if isinstance(display, dict):
        icon = display.get("icon")
        if isinstance(icon, dict) and "item" in icon and "id" not in icon:
            icon["id"] = icon.pop("item")
    return obj


def copy_json_tree(src: Path, dest: Path, converter=None):
    dest.parent.mkdir(parents=True, exist_ok=True)
    if src.suffix == ".json":
        try:
            data = json.loads(src.read_text(encoding="utf-8"))
            if converter:
                data = converter(data)
            dest.write_text(json.dumps(data, indent=2) + "\n", encoding="utf-8")
        except Exception:
            shutil.copy2(src, dest)
    else:
        shutil.copy2(src, dest)


data_src = JAR / "data"
if data_src.exists():
    for src in data_src.rglob("*"):
        if not src.is_file() or skip_path(src):
            continue
        rel = src.relative_to(data_src)
        parts = list(rel.parts)
        # 1.21 folder renames
        renamed = []
        for i, part in enumerate(parts):
            if part == "recipes":
                renamed.append("recipe")
            elif part == "advancements":
                renamed.append("advancement")
            elif part == "loot_tables":
                renamed.append("loot_table")
            elif part == "blocks" and i >= 1 and parts[i - 1] == "tags":
                renamed.append("block")
            elif part == "items" and i >= 1 and parts[i - 1] == "tags":
                renamed.append("item")
            else:
                renamed.append(part)
        dest = DEST / "data" / Path(*renamed)
        name = src.name
        if src.suffix == ".json":
            if "recipe" in renamed:
                copy_json_tree(src, dest, convert_recipe)
            elif "advancement" in renamed:
                copy_json_tree(src, dest, convert_advancement)
            else:
                copy_json_tree(src, dest)
        else:
            dest.parent.mkdir(parents=True, exist_ok=True)
            shutil.copy2(src, dest)

print("data conversion done")
# count
for folder in ("recipe", "advancement", "loot_table", "tags"):
    p = DEST / "data"
    n = len(list(p.rglob(f"*{folder}*"))) if p.exists() else 0
print("files in data:", len(list((DEST / "data").rglob("*"))) if (DEST / "data").exists() else 0)
