#!/bin/bash

HOSTNAME=$1

echo "Copying plugin..."
ssh $HOSTNAME "rm /opt/sonar/extensions/plugins/sonar-ddd-plugin*.jar"
scp target/sonar-ddd-plugin-*.jar $HOSTNAME:/opt/sonar/extensions/plugins
echo "Restarting sonar..."
ssh $HOSTNAME "sudo service sonar restart"
