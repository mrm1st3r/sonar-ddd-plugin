#!/bin/bash

HOSTNAME=$1

echo "Copying plugin..."
scp target/sonar-ddd-plugin-0.0.1-SNAPSHOT.jar $HOSTNAME:/opt/sonar/extensions/plugins
echo "Restarting sonar..."
ssh $HOSTNAME "sudo service sonar restart"
