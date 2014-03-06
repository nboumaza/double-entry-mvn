import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName         = "double-entry-mvn"
    val appVersion      = "1.0"

    val appDependencies = Seq(
    	// Add the project dependencies here, 
		javaCore,
		javaJdbc,
		javaJpa,
		"org.hibernate" % "hibernate-entitymanager" % "4.2.1.Final"
    )

	val main = play.Project(appName, appVersion, appDependencies).settings(
  		ebeanEnabled := false 
	)
   
}
