#! /bin/sh

mvn -T 3C clean war:war release:prepare release:perform tomcat:redeploy site-deploy
