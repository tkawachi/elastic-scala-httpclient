addSbtPlugin("com.typesafe.sbteclipse" % "sbteclipse-plugin" % "2.4.0")

addSbtPlugin("com.github.mpeltonen" % "sbt-idea" % "1.6.0")

addSbtPlugin("com.typesafe.sbt" % "sbt-pgp" % "0.8.3")

libraryDependencies <+= sbtVersion("org.scala-sbt" % "scripted-plugin" % _)
