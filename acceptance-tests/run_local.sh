#!/bin/bash
gradle acceptanceTests -Dheadless=false -DbaseUrl=http://localhost:8780 -DmanagementUrl=http://localhost:8781 -Dcucumber.options="--tags @PageValidations"