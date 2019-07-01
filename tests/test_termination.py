# -*- coding:utf-8 -*-
#
# Copyright (C) 2019, Maximilian Köhl <mkoehl@cs.uni-saarland.de>

from __future__ import annotations

from sidekick import tester
from sidekick.runner import Handle, Runner


source, reference = tester.load_book('alice-wonderland')

CHUNK = '''
His man-ner was not ef-fus-ive. It sel-dom was; but he was glad, I think,
to see me. With hardly a word spoken, but with a kindly eye, he waved
me to an arm-chair, threw across his case of ci-gars, and in-dic-ated a
spirit case and a gas-o-gene in the corner. Then he stood be-fore the fire
and looked me over in his sin-gu-lar in-tro-spect-ive fash-ion.
'''.strip() * 100


@tester.add_configuration('slow')
@tester.add_configuration('default')
async def test_termination(runner: Runner, normal: Handle, error: Handle):
    normal.segments(source)
    normal.eof()

    for _ in range(1000):
        error.chunk(CHUNK)
    error.end_of_segment()
    error.chunk('A' * 250)
    error.eof()

    await tester.expect_pages(normal, reference, timeout=5)
