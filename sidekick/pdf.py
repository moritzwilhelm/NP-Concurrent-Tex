# -*- coding:utf-8 -*-
#
# Copyright (C) 2019, Maximilian Köhl <mkoehl@cs.uni-saarland.de>

from __future__ import annotations

from typing import Iterable, List

from .printer import Page
from .font import Font, Glyph, GLYPHS

try:
    import cairo
except ImportError:
    cairo = None


PPI = 72  # pixels per inch


def in2px(inch: float) -> float:
    return inch * PPI


A4_WIDTH = in2px(8.3)
A4_HEIGHT = in2px(11.7)


class PDF:
    width: float
    height: float

    surface: cairo.PDFSurface

    @classmethod
    def font(cls, name: str, size: int = 12, glyphs: Iterable[str] = GLYPHS) -> Font:
        return cls()._font(name, size, glyphs)

    def __init__(self, file_obj=None, width: float = A4_WIDTH, height: float = A4_HEIGHT):
        self.width = width
        self.height = height
        self.surface = cairo.PDFSurface(file_obj, width, height)
        self.context = cairo.Context(self.surface)

    def _font(self, name: str, size: int = 12, glyphs: Iterable[str] = GLYPHS) -> Font:
        self.context.select_font_face(name, cairo.FONT_SLANT_NORMAL, cairo.FONT_WEIGHT_NORMAL)
        self.context.set_font_size(size)
        font = Font(name=name, size=size, glyphs={})
        for glyph in glyphs:
            width = self.context.text_extents(glyph).x_advance
            font.glyphs[glyph] = Glyph(width=width, value=glyph)
        return font

    def render(self, pages: List[Page]):
        for page in pages:
            self.context.select_font_face(page.font_spec.name)
            self.context.set_font_size(page.font_spec.size)
            self.surface.set_size(page.paper_width, page.paper_height)
            for show in page.fragments:
                self.context.move_to(show.x, show.y)
                self.context.show_text(show.text)
            self.context.show_page()

    def finish(self):
        self.surface.finish()
