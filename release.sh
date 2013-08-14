#!/bin/sh

mvn -T 3C clean release:prepare release:perform tomcat:redeploy
