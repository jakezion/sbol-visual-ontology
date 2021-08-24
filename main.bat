cd markdown-to-owl
pip3 install owlready2
pip3 install rdflib
call python Application.py

cd ../lode-document-extractor
call mvn clean install
call mvn exec:java -Dexec.mainClass="lodedocumentextractor"

cd ../sbol-vo-update-doc
call mvn clean install
call mvn exec:java -e -Dexec.mainClass="uk.ac.keele.dissys.sbolvo.html.App"


