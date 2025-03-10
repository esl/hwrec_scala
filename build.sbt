lazy val akkaHttpVersion = "10.1.9"
lazy val akkaVersion    = "2.5.25"

lazy val root = (project in file(".")).
enablePlugins(JavaAppPackaging).
  settings(
    inThisBuild(List(
      organization    := "com.hwrec",
      scalaVersion    := "2.12.1"
    )),
    name := "hwrec",
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http"            % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-http-xml"        % akkaHttpVersion,
      "com.typesafe.akka" %% "akka-stream"          % akkaVersion,

      "com.typesafe.akka" %% "akka-http-testkit"    % akkaHttpVersion % Test,
      "com.typesafe.akka" %% "akka-testkit"         % akkaVersion     % Test,
      "com.typesafe.akka" %% "akka-stream-testkit"  % akkaVersion     % Test,
      "org.scalatest"     %% "scalatest"            % "3.0.5"         % Test
    )
  )

//cancelable in Global := true
