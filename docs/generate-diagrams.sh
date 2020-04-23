#!/usr/bin/env bash
set -euo pipefail

#echo $(basename $0)
directory=$(dirname $0)

files=$(find $directory -name "*.puml")

for file in $files
do
  echo Converting $file
  plantuml -tsvg -o . $file
done