package vchat.time

import java.time.{ZoneOffset, LocalDateTime => Time}

/*
 * 任意の時間から始まり、任意の時間間隔で進む時計(yyyy/mm/ddから始まる1000秒で1秒進む時計とか)
 * */
trait TimeScale {
  val zoneOffset: ZoneOffset
  def epochMilli: Long
}

object TimeScale {
  type DateTimeManager = Any => Time
  private def apply(tk: DateTimeManager): TimeScale =
    new TimeScale {
      override val zoneOffset: ZoneOffset = ZoneOffset.UTC
      override def epochMilli: Long = tk(()).toInstant(zoneOffset).toEpochMilli
    }
  def default: TimeScale = TimeScale(_ => Time.now())
}
