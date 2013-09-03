#!/bin/sh

mvn sonar:sonar

mvn -T 3C clean war:war tomcat:redeploy
