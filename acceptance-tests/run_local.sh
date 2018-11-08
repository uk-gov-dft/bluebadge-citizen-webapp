#!/bin/bash
<<<<<<< Updated upstream
gradle acceptanceTests -Dheadless=false -DbaseUrl=http://localhost:8780
=======
gradle acceptanceTests -Dheadless=false -DbStackMode=true -DbaseUrl=http://dft.local:8780 -Dcucumber.options="--tags @SubmitApplicationWALKDRoute,@SubmitApplicationPIPRoute" -DbStackBrowserName="firefox" -DbStackBrowserVersion="63.0"
>>>>>>> Stashed changes
