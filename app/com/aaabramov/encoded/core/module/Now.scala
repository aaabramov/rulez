package com.aaabramov.encoded.core.module

import java.time.{LocalDateTime, ZoneId, ZoneOffset}

trait Now extends (() => LocalDateTime)

object Now {
  lazy val utc: Now = () => LocalDateTime.now(ZoneOffset.UTC)

  def of(zone: ZoneId): Now = () => LocalDateTime.now(zone)
}
