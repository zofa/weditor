#!/bin/sh

mvn release:prepare release:perform tomcat:redeploy

git gc
