# -*- coding:utf-8 -*-
#
# Copyright (C) 2019, Maximilian Köhl <mkoehl@cs.uni-saarland.de>

from __future__ import annotations

from typing import Dict

import json
import pathlib
import string

from dataclasses import dataclass, field


@dataclass
class Font:
    name: str
    size: int
    glyphs: Dict[str, Glyph] = field(default_factory=dict)

    def __getitem__(self, glyph: str) -> Glyph:
        return self.glyphs[glyph]

    def measure_text(self, text: str) -> float:
        return sum(self[glyph].width for glyph in text)

    def export(self, path: pathlib.Path):
        path.write_text(json.dumps({
            'font_spec': {'name': self.name, 'size': self.size},
            'glyphs': {
                glyph.value: {
                    'value': glyph.value, 'width': glyph.width
                } for glyph in self.glyphs.values()
            }
        }), encoding='utf-8')


GLYPHS = string.printable + '’—“”‘é'


@dataclass
class Glyph:
    width: float
    value: str
