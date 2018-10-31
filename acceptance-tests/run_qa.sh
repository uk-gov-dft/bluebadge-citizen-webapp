#!/bin/bash
gradle acceptanceTests -Dheadless=false -DbaseUrl=https://qa.does.not.exist -Dcucumber.options="--tags @PageValidations"