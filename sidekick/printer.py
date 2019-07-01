# -*- coding:utf-8 -*-
#
# Copyright (C) 2019, Maximilian Köhl <mkoehl@cs.uni-saarland.de>

from __future__ import annotations

from typing import List

import json

from dataclasses import dataclass


@dataclass
class FontSpec:
    name: str
    size: float


@dataclass
class Fragment:
    x: float
    y: float
    text: str


@dataclass
class Page:
    font_spec: FontSpec
    paper_width: float
    paper_height: float
    fragments: List[Fragment]

    @classmethod
    def load(cls, data: bytes):
        obj = json.loads(data, encoding='utf-8')
        font_spec = FontSpec(obj['font_spec']['name'], obj['font_spec']['size'])
        paper_width = obj['paper_width']
        paper_height = obj['paper_height']
        fragments = [
            Fragment(**fragment) for fragment in obj['fragments']
        ]
        return Page(font_spec, paper_width, paper_height, fragments)
        

def iter_pages(data: bytes):
    for chunk in data.split(b'\0\n'):
        if not chunk:
            continue
        yield Page.load(chunk)


def load_pages(data: bytes):
    return list(iter_pages(data))

