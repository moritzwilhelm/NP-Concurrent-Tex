# -*- coding:utf-8 -*-
#
# Copyright (C) 2019, Maximilian Köhl <mkoehl@cs.uni-saarland.de>

from __future__ import annotations

import enum
import re


class TokenType(enum.Enum):
    WORD = r'\w+'
    WHITESPACE = r'\s+'
    SPECIAL = r'[^\w\s]+'


_REGEX = re.compile('|'.join(
    f'(?P<{token_type.name}>{token_type.value})'
    for token_type in TokenType
))


def tokenize(text: str):
    for match in _REGEX.finditer(text):
        token_type = TokenType[match.lastgroup]
        yield token_type, match.group(0)
