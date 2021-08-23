cd markdown-to-owl || exit
pip3 install owlready2
pip3 install rdflib
call python Application.py

cd ../lode-document-extractor || exit
call mvn clean install
call mvn exec:java -Dexec.mainClass="lodedocumentextractor"

# shellcheck disable=SC2164
cd ../sbol-vo-update-doc
call mvn clean install
call mvn exec:java -e -Dexec.mainClass="uk.ac.keele.dissys.sbolvo.html.App"


