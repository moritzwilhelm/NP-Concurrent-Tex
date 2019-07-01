# -*- coding:utf-8 -*-
#
# Copyright (C) 2019, Maximilian Köhl <mkoehl@cs.uni-saarland.de>

from __future__ import annotations

import asyncio

from sidekick import tester
from sidekick.runner import Handle, Runner


source, reference = tester.load_book('sherlock-holmes')


@tester.add_configuration('default')
@tester.add_configuration('default')
@tester.add_configuration('default')
@tester.add_configuration('default')
async def test_multiple_docs(runner: Runner, *handles: Handle):
    for handle in handles:
        handle.segments(source)
        handle.eof()

    await asyncio.gather(*(
        tester.expect_pages(handle, reference) for handle in handles
    ))
