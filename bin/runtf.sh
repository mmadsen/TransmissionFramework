#!/bin/sh

java -Xms1g -Xmx1g  -Dlog4j.configuration=file:///tmp/log4j.config -ea -jar TransmissionFrameworkModels.jar -c /tmp/wfia-2dim-neutral-ruleset.xml -p /tmp/tflogs
