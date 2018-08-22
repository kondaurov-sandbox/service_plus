import sbt._

object Dependencies {

  lazy val akka_http = "com.typesafe.akka" %% "akka-http"   % "10.1.3"
  lazy val akka_actors =  "com.typesafe.akka" %% "akka-actor" % "2.5.14"
  lazy val akka_stream =  "com.typesafe.akka" %% "akka-stream" % "2.5.14"
  lazy val akka_http_json4s =  "de.heikoseeberger" %% "akka-http-json4s" % "1.21.0"
  lazy val scalatest = "org.scalatest" %% "scalatest" % "3.0.5" % "test"
  lazy val ficus = "com.iheart" %% "ficus" % "1.4.3"
  lazy val rxscala = "io.reactivex" %% "rxscala" % "0.26.5"
  lazy val flyway = "org.flywaydb" % "flyway-core" % "5.1.4"
  lazy val json4s = "org.json4s" %% "json4s-jackson" % "3.6.0"
  lazy val json4sExt = "org.json4s" %% "json4s-ext" % "3.6.0"
  lazy val scalajHttp = "org.scalaj" %% "scalaj-http" % "2.4.1"

  lazy val slick = Seq(
    "com.typesafe.slick" %% "slick" % "3.2.3",
    "org.slf4j" % "slf4j-nop" % "1.6.4",
    "com.typesafe.slick" %% "slick-hikaricp" % "3.2.3"
  )

  lazy val pgdriver = "org.postgresql" % "postgresql" % "42.2.4"

}