package com.starter

import com.natpryce.hamkrest.assertion.assertThat
import com.natpryce.hamkrest.equalTo
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.specification.describe

object AppTest: Spek({

    describe("a calculator") {

        context("adding") {
            it("2 + 3 equals 5") {
                assertThat(2 + 3, equalTo(5))
            }
        }
    }
})