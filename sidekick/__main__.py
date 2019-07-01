# -*- coding:utf-8 -*-
#
# Copyright (C) 2019, Maximilian Köhl <mkoehl@cs.uni-saarland.de>

from __future__ import annotations

import pathlib

from typing import List

try:
    from hyphen import dictools, Hyphenator
except ImportError:
    Hyphenator = None

import argparse

from .tokenize import tokenize, TokenType
from .font import GLYPHS


parser = argparse.ArgumentParser()

subparsers = parser.add_subparsers(dest='command')

tester = subparsers.add_parser('tester')
tester.add_argument('executable', type=pathlib.Path)

render = subparsers.add_parser('render')
render.add_argument('input', type=argparse.FileType('rt', encoding='UTF-8'))
render.add_argument('output', type=argparse.FileType('wb'))

compare = subparsers.add_parser('compare')
compare.add_argument('file1', type=argparse.FileType('rt', encoding='utf-8'))
compare.add_argument('file2', type=argparse.FileType('rt', encoding='utf-8'))

tester = subparsers.add_parser('tester')
tester.add_argument('executable', type=pathlib.Path)
tester.add_argument('test', nargs='+')

if Hyphenator is not None:
    hyphenate = subparsers.add_parser('hyphenate')
    hyphenate.add_argument('language')
    hyphenate.add_argument('input', type=argparse.FileType('rb'))
    hyphenate.add_argument('output', type=argparse.FileType('wb'))

export_font = subparsers.add_parser('export_font')
export_font.add_argument('font_name', type=str)
export_font.add_argument('font_size', type=int)
export_font.add_argument('output', type=pathlib.Path)
export_font.add_argument('text', nargs='*')


def main(arguments: List[str] = None):
    namespace = parser.parse_args(arguments)

    command = namespace.command

    if command == 'export_font':
        from .pdf import PDF
        glyphs = set(GLYPHS)
        cwd = pathlib.Path('.')
        if namespace.text is not None:
            for text_glob in namespace.text:
                for text_file in cwd.glob(text_glob):
                    print(f'Taking glyphs from:\n  {text_file}')
                    glyphs.update(set(text_file.read_text('utf-8')))
        font = PDF.font(namespace.font_name, namespace.font_size, glyphs=glyphs)
        font.export(namespace.output)
    elif command == 'tester':
        from .tester import main
        main(namespace)
    elif command == 'hyphenate':
        text = namespace.input.read().decode()
        hyphenator = Hyphenator(language=namespace.language)
        for token_type, text in tokenize(text):
            if token_type is TokenType.WORD:
                syllables = hyphenator.syllables(text) or [text]
                namespace.output.write_chunk('-'.join(syllables).encode())
            else:
                namespace.output.write_chunk(text.encode())
    elif command == 'render':
        import json

        from .printer import Page, FontSpec, Fragment
        from .pdf import PDF

        text = namespace.input.read()
        raw_pages = text.split('\0\n')

        pages = []

        for raw_page in raw_pages:
            if not raw_page:
                continue
            page_data = json.loads(raw_page)
            font_spec = FontSpec(page_data['font_spec']['name'], page_data['font_spec']['size'])
            paper_width = page_data['paper_width']
            paper_height = page_data['paper_height']
            fragments = [Fragment(**fragment) for fragment in page_data['fragments']]
            page = Page(font_spec, paper_width, paper_height, fragments)
            pages.append(page)

        pdf = PDF(namespace.output)
        pdf.render(pages)
        pdf.finish()


if __name__ == '__main__':
    main()
