# -*- coding:utf-8 -*-
#
# Copyright (C) 2019, Maximilian Köhl <mkoehl@cs.uni-saarland.de>

from __future__ import annotations

import asyncio
import time

from sidekick import tester
from sidekick.runner import Handle, Runner


source, reference = tester.load_book('sherlock-holmes')

the_end_source, the_end_reference = tester.load_the_end()


@tester.add_configuration('slow')
async def test_vintage_media(runner: Runner, handle: Handle):
    handle.segments(source, the_end_source)
    await asyncio.sleep(5)
    handle.eof()

    start = time.monotonic()
    await handle.drain()
    print(time.monotonic() - start)
    await runner.wait_for_termination(5)
    print(time.monotonic() - start)
