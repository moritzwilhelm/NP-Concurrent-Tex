# -*- coding:utf-8 -*-
#
# Copyright (C) 2019, Maximilian Köhl <mkoehl@cs.uni-saarland.de>

from __future__ import annotations

from sidekick import tester
from sidekick.runner import Handle, Runner


def make_test(name: str):
    source, reference = tester.load_book(name)

    @tester.add_configuration('default')
    async def run_test(runner: Runner, handle: Handle):
        handle.segments(source)
        handle.eof()
        await tester.expect_pages(handle, reference)

    return run_test


test_alice_wonderland = make_test('alice-wonderland')
test_dracula = make_test('dracula')
test_frankenstein = make_test('frankenstein')
test_huckleberry_finn = make_test('huckleberry-finn')
test_metamorphosis = make_test('metamorphosis')
test_moby_dick = make_test('moby-dick')
test_sherlock_holmes = make_test('sherlock-holmes')
test_tom_sawyer = make_test('tom-sawyer')
