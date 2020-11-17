package vchat.app.env

trait AppTime {
  def defaultTokenTimeout: Long = 60 * 1000 * 5
  def currentTimeMillis: Long = System.currentTimeMillis()
}
