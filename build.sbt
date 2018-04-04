lazy val commonSettings = Seq(
  organization := "ru.ars-co",
  name := "scala-solr-client",
  version := "0.0.6-SNAPSHOT"
)

lazy val crossBuildSettings = Seq(
  scalaVersion := "2.11.8",
  crossScalaVersions := Seq("2.11.8", "2.12.4")
)

lazy val publishMavenSettings = Seq(
  publishMavenStyle := true,
  publishArtifact in Test := false
)

val Log4jVersion = "2.7"
val ScalaTestVersion = "3.0.0"
val Json4sVersion = "3.5.0"

lazy val loggingDependencies = Seq(
  "com.typesafe.scala-logging" %% "scala-logging" % "3.5.0",
  "org.slf4j" % "jcl-over-slf4j" % "1.7.21",
  "org.apache.logging.log4j" % "log4j-slf4j-impl" % Log4jVersion,
  "org.apache.logging.log4j" % "log4j-api" % Log4jVersion,
  "org.apache.logging.log4j" % "log4j-core" % Log4jVersion
)

lazy val preconditionsDependencies = Seq(
  "ru.ars-co" %% "scala-preconditions" % "0.0.3"
)

lazy val solrDependencies = Seq(
  "org.apache.lucene" % "lucene-queries" % SolrVersion,
  "org.apache.solr" % "solr-solrj" % SolrVersion,
)

lazy val httpDependencies = Seq(
  "org.scalaj" %% "scalaj-http" % "2.3.0"
)

lazy val jsonDependencies = Seq(
  "org.json4s" %% "json4s-native" % Json4sVersion
//  "org.json4s" %% "json4s-jackson" % Json4sVersion,
//  "org.json4s" %% "json4s-ext" % Json4sVersion,
//  "com.github.java-json-tools" % "json-schema-validator" % "2.2.8"
)

lazy val poolDependencies = Seq(
  "io.github.andrebeat" %% "scala-pool" % "0.4.0"
)

lazy val testingDependencies = Seq(
  "org.scalactic" %% "scalactic" % ScalaTestVersion % Test,
  "org.scalatest" %% "scalatest" % ScalaTestVersion % Test,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.5.0" % Test,
  "org.apache.solr" % "solr-core" % SolrVersion % Test
)

lazy val `scala-solr-client` = (project in file("."))
  .settings(
    commonSettings,
    crossBuildSettings,
    publishMavenSettings,

    libraryDependencies ++= loggingDependencies ++ preconditionsDependencies ++ solrDependencies
      ++ httpDependencies ++ jsonDependencies ++ poolDependencies ++ testingDependencies,

    resolvers += DefaultMavenRepository,

    publishTo := {
      val nexus = "https://oss.sonatype.org/"
      if (isSnapshot.value)
        Some("snapshots" at nexus + "content/repositories/snapshots")
      else
        Some("releases"  at nexus + "service/local/staging/deploy/maven2")
    },

    homepage := Some(url("https://github.com/ArsCo/scala-solr-client")),

    scmInfo := Some(
      ScmInfo(
        url("https://github.com/ArsCo/scala-solr-client"),
        "scm:git@github.com:ArsCo/scala-solr-client.git"
      )
    ),

    developers := List(
      Developer(
        id    = "ars",
        name  = "Arsen Ibragimov",
        email = "ars@ars-co.ru",
        url   = url("https://github.com/ars-java")
      )
    ),

    licenses := Seq("Apache-2.0" -> url("http://www.apache.org/licenses/LICENSE-2.0.html")),

    pomIncludeRepository := { _ => false },
  )
