#! /bin/bash

mv -v dist/jtsijavapart.jar dist/jtsijavapart.jar.back
jar cvmf META-INF/MANIFEST.MF dist/jtsijavapart.jar -C ./bin .
