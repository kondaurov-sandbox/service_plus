import com.typesafe.sbt.packager.docker.DockerPlugin.autoImport.dockerRepository

val dockerSettings = Seq(
  dockerBaseImage := "robsonoduarte/8-jre-alpine-bash",
  dockerRepository := Some("dispatch")
)

lazy val sms_gateway = (project in file("sms_gateway"))
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.ficus
    )
  )
  .dependsOn(akka_http_common)
  .settings(dockerSettings: _*)

lazy val router = (project in file("router"))
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.ficus
    )
  )
  .settings(dockerSettings: _*)
  .dependsOn(pbmodels, akka_http_common)

lazy val dispatch_service = (project in file("dispatch_service"))
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.akka_actors,
      Dependencies.ficus,
      Dependencies.scalajHttp,
      Dependencies.scalatest
    )
  )
  .settings(dockerSettings: _*)
  .dependsOn(pbmodels)

lazy val ref_service = (project in file("ref_service"))
  .enablePlugins(JavaServerAppPackaging)
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.rxscala,
      Dependencies.pgdriver,
      Dependencies.scalatest,
      Dependencies.ficus,
      Dependencies.flyway
    ) ++ Dependencies.slick
  )
  .settings(dockerSettings: _*)
  .dependsOn(pbmodels)

lazy val pbmodels = (project in file("pbmodels"))
  .settings(
    libraryDependencies ++= Seq(
      "io.grpc" % "grpc-netty" % scalapb.compiler.Version.grpcJavaVersion,
      "com.thesamet.scalapb" %% "scalapb-runtime-grpc" % scalapb.compiler.Version.scalapbVersion,
      "com.thesamet.scalapb" %% "scalapb-json4s" % "0.7.0",
      "com.thesamet.scalapb" %% "scalapb-runtime" % scalapb.compiler.Version.scalapbVersion % "protobuf",
      Dependencies.scalatest
    ),
    PB.targets in Compile := Seq(
      scalapb.gen() -> (sourceManaged in Compile).value
    )
  )

lazy val akka_http_common = (project in file("common/akka_http"))
  .settings(
    libraryDependencies ++= Seq(
      Dependencies.akka_http,
      Dependencies.akka_stream,
      Dependencies.json4s,
      Dependencies.json4sExt
    )
  )