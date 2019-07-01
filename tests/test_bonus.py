# -*- coding:utf-8 -*-
#
# Copyright (C) 2019, Maximilian Köhl <mkoehl@cs.uni-saarland.de>

from __future__ import annotations

from sidekick import tester
from sidekick.runner import Handle, Runner


source, reference = tester.load_book('sherlock-holmes')

the_end_source, the_end_reference = tester.load_the_end()


@tester.bonus_only
@tester.add_configuration('default')
async def test_bonus(runner: Runner, handle: Handle):
    handle.segments(source, the_end_source)
    await tester.expect_pages(handle, reference, timeout=5)
    handle.segments(source)
    handle.eof()
    await tester.expect_pages(handle, the_end_reference)
    await tester.expect_pages(handle, reference)
