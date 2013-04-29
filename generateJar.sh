#! /bin/bash

mv -v jtsijavapart.jar jtsijavapart.jar.back
jar cvmf META-INF/MANIFEST.MF jtsijavapart.jar -C ~/Documents/ter/sourceRepos/JTSI/bin .
