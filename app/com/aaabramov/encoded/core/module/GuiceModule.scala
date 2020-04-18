package com.aaabramov.encoded.core.module

import java.time.Clock

import play.api.{Configuration, Environment}


class GuiceModule extends play.api.inject.Module {

  def bindings(environment: Environment, configuration: Configuration) = Seq(
    bind[Now].toInstance(Now.utc),
    bind[Clock].toInstance(Clock.systemUTC())
  )
}