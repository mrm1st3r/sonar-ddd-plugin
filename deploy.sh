#!/bin/bash

echo "Copying plugin..."
scp target/sonar-ddd-plugin-0.0.1-SNAPSHOT.jar sonar.vm:/opt/sonar/extensions/plugins
echo "Restarting sonar..."
ssh sonar.vm "sudo service sonar restart"
