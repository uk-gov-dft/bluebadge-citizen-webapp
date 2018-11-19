#!/bin/bash
gradle acceptanceTests -Dheadless=false -DbaseUrl=https://qa.does.not.exist
#-DbStackMode=true -DbStackBrowserName="ie" -DbStackBrowserVersion="11.0" -DbStackUser="" -DbStackKey=""
# -Dcucumber.options="--tags @PageValidations"
