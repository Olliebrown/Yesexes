#!/bin/bash
export MSYS_NO_PATHCONV=1

if [ -f ./switchdevkitpro.cid ]; then
  echo "Reusing Existing Switch Devkitpro Docker container ..."
  docker start -i -a $(cat ./switchdevkitpro.cid)
else
  echo "Building New Switch Devkitpro Docker container ..."
  docker build -t switchdevkitpro .
  docker run --cidfile ./switchdevkitpro.cid -v $(pwd):/Yesexes -w /Yesexes -i -t switchdevkitpro bash
fi
