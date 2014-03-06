***********************************************************************
DEMO and PLAY FRAMEWORK OVERVIEW
***********************************************************************
This is a classic CRUD application that uses the MVC pattern via play.mvc

It demonstrates:

- Accessing a JDBC database, using JPA.
- Achieving, table pagination and CRUD forms.
- Integrating with a CSS framework (Twitter Bootstrap ).


Models

	Note that the domain objects have their "properties" defined as public
	Here's some interesting reads on Play domain objects
	http://www.playframework.com/documentation/1.2.3/model
 
Controllers

        Business logic is managed in the domain model layer. 
	For a web or REST client the domain layer is exposed as resources represented by URIs
	via the controllers and the REST api definition in conf/routes
	Like the HTTP interface, controllers are procedural and Request/Response oriented.

	NOTE: 
	* Play will automatically manage transactions for you. 
	* It will start a transaction for each HTTP request and commit it when the HTTP 
  	response is sent

	More interesting reads about controllers can be found here:
	http://www.playframework.com/documentation/1.2.3/controllers
	(Play documentation are resources so make sure you refer to the latest doc rest service 
	version, 1.2.3 at the time this writing )
     

Views

	All views are defined using scala template engine
     

Services

	You can optionally expose the domain objects business logic as a SOAP service,
	or a standalone OSGI bundle or other goodie that you'd like to expose 
	to a non web and rest client 


***********************************************************************
	BUILDING THE DEMO
***********************************************************************  

1. Download and install Play 2 distributions
	
	You can use the install-play goal to download and install Play 2 distributions.

	1.1 From a nix prompt / windows cygwin or Mac terminal issue the following command:
 
	export PLAY2_HOME=YourInstallDir

	1.2 Now let’s install play using the following command 

	mvn org.nanoko.playframework:play2-maven-plugin:install-play -Dplay2version=2.2.1 -Dplay2basedir=${PLAY2-HOME}/opt
	This will install  play2 in ${PLAY_HOME2}/opt/play-2.2.1.

2. Clone or fork this repo

3. Building and deploying the Double Entry demo
	
	3.1 cd to the base directory where you cloned the source code: 

	from within double-entry-mvn directory
	
	3.2 To build the app simply use: mvn clean install

	3.2 To run the demo use:  play run

	click  this url:  http://localhost:9000
	The first time the app runs it will prompt you for running H2 test DB setup- click on: Apply this script now
    	(The sql scripts located in conf/evolution/default)


	NOTE:
	The internal server is Netty (http://www.jboss.org/netty).

    While the dist file includes a war distribution it is recommended to run using the Play embedded servlet engine.
    Unless you are forced to deploy a Play application in a servlet container, 
    don't do it. 
    Play performs better on Netty as it doesn't need to jump through hoops to support 
    the servlet standard, which shaves off quite a bit of overhead.  
