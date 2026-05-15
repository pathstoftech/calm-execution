from __future__ import annotations

import json
import shutil
import sys
from pathlib import Path

MANIFEST_PATH = Path("docs/assets/runtime_export_manifest.json")
SOURCE_DIR = Path("docs/assets/runtime_exports")
TARGET_DIR = Path("app/src/main/res/drawable-nodpi")

EXPECTED_WIDTH = 1600
EXPECTED_HEIGHT = 900
SOFT_MAX_SIZE_KB = 500


def fail(message: str) -> None:
    print(f"ERROR: {message}", file=sys.stderr)
    raise SystemExit(1)


def read_webp_size(path: Path) -> tuple[int, int]:
    data = path.read_bytes()

    if len(data) < 30:
        fail(f"{path} is too small to be a valid WebP file")

    if data[0:4] != b"RIFF" or data[8:12] != b"WEBP":
        fail(f"{path} is not a RIFF WEBP file")

    offset = 12

    while offset + 8 <= len(data):
        chunk_type = data[offset : offset + 4]
        chunk_size = int.from_bytes(data[offset + 4 : offset + 8], "little")
        chunk_data_start = offset + 8
        chunk_data_end = chunk_data_start + chunk_size

        if chunk_data_end > len(data):
            fail(f"{path} has a malformed WebP chunk")

        chunk = data[chunk_data_start:chunk_data_end]

        if chunk_type == b"VP8X":
            if len(chunk) < 10:
                fail(f"{path} has malformed VP8X data")

            width = 1 + int.from_bytes(chunk[4:7], "little")
            height = 1 + int.from_bytes(chunk[7:10], "little")
            return width, height

        if chunk_type == b"VP8L":
            if len(chunk) < 5:
                fail(f"{path} has malformed VP8L data")

            bits = int.from_bytes(chunk[1:5], "little")
            width = (bits & 0x3FFF) + 1
            height = ((bits >> 14) & 0x3FFF) + 1
            return width, height

        if chunk_type == b"VP8 ":
            if len(chunk) < 10:
                fail(f"{path} has malformed VP8 data")

            width = int.from_bytes(chunk[6:8], "little") & 0x3FFF
            height = int.from_bytes(chunk[8:10], "little") & 0x3FFF
            return width, height

        offset = chunk_data_end + (chunk_size % 2)

    fail(f"{path} does not contain a supported WebP image chunk")


def load_manifest_items() -> list[dict]:
    if not MANIFEST_PATH.exists():
        fail(f"Missing manifest: {MANIFEST_PATH}")

    manifest = json.loads(MANIFEST_PATH.read_text(encoding="utf-8"))
    items = manifest.get("items", [])

    if len(items) != 30:
        fail(f"Expected 30 manifest items, found {len(items)}")

    return items


def validate_source_directory() -> None:
    if not SOURCE_DIR.exists():
        fail(f"Missing source export directory: {SOURCE_DIR}")


def validate_expected_files(items: list[dict]) -> None:
    expected_files = {item["runtimeFileName"] for item in items}

    if len(expected_files) != 30:
        fail("Manifest runtime filenames are not unique")

    actual_files = {
        path.name
        for path in SOURCE_DIR.glob("*.webp")
        if path.is_file()
    }

    missing = sorted(expected_files - actual_files)
    extra = sorted(actual_files - expected_files)

    if missing:
        fail("Missing runtime exports:\n" + "\n".join(missing))

    if extra:
        fail("Unexpected extra runtime exports:\n" + "\n".join(extra))


def validate_and_copy_item(item: dict) -> None:
    file_name = item["runtimeFileName"]
    image_key = item["imageKey"]

    expected_file_name = f"{image_key}.webp"

    if file_name != expected_file_name:
        fail(f"{file_name} does not match imageKey {image_key}")

    source_path = SOURCE_DIR / file_name
    target_path = TARGET_DIR / file_name

    width, height = read_webp_size(source_path)

    if width != EXPECTED_WIDTH or height != EXPECTED_HEIGHT:
        fail(
            f"{file_name} is {width}x{height}; "
            f"expected {EXPECTED_WIDTH}x{EXPECTED_HEIGHT}"
        )

    size_kb = source_path.stat().st_size / 1024

    if size_kb > SOFT_MAX_SIZE_KB:
        fail(
            f"{file_name} is {size_kb:.1f} KB; "
            f"soft max is {SOFT_MAX_SIZE_KB} KB"
        )

    shutil.copy2(source_path, target_path)


def main() -> None:
    validate_source_directory()

    items = load_manifest_items()
    validate_expected_files(items)

    TARGET_DIR.mkdir(parents=True, exist_ok=True)

    for item in items:
        validate_and_copy_item(item)

    print(f"Copied {len(items)} runtime assets to {TARGET_DIR}")


if __name__ == "__main__":
    main()