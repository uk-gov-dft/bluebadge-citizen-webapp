#!/bin/bash
gradle acceptanceTests -Dheadless=false -DbaseUrl=https://qa.does.not.exist
#-DbStackMode=false -DbStackBrowserName="ie" -DbStackBrowserVersion="11.0"
#-Dcucumber.options="--tags @SubmitApplicationWPMSRoute"