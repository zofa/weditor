#!/bin/sh

mvn -T 3C clean war:war tomcat:redeploy
