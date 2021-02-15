## Java CI using webhooks

Developed by: 

Gustav Dowling

Amar Hodzic

Petter Jansen

### How to build and test the server

To manually build and test the CI server, clone this repo and run "./gradlew clean build".

### How to run the server

To build and run the CI server, you will need the following: Java, Gradle and ngrok

Clone this repository.

Change the target repo and branch in the file "ContinuousIntegrationServer.java" , the default target is this repository with the branch "assessment".

Change the recipient of the email in the file "mailNotification.java" to whatever address you want the CI information to be sent to.

Run "./gradlew clean build run"

Start up ngrok with "./ngrok http 8080" and redirect the webhook of the target repo to the address that ngrok provides.

Whenever the webhook sends a post request to the server, you will recieve an email with information on if the project built, what tests failed with some more debug information.

### Structure of this repository

In smallest-java-ci/app/src/main/java/smallest/java/ci/ you will find the following files:

ContinuousIntegrationServer.java Which is our main class which runs the server.

FsHelper.java which contains helper methods for handling of the files the server uses.

GitHelper.java which contains helper methods for JGit, for verifying the webhook signal and cloning the target repo.

mailNotification.java Which contains the methods for sending an email to a target address.

### Statement of contributions

Gustav Dowling - Mainly implemented the automatic building and testing functionality of the CI.

Amar Hodzic - Implemented the Git clone, webhook validation and folder removal functions including tests. 
As well as some functionality in the handle function used by the server handler.

Petter Jansen - 
