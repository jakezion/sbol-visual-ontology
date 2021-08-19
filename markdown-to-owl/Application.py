'''
Created on 29 Mar 2019

@author: gokselmisirli
'''
import types
from MarkDown import *
from Ontology import *
import os

# from numpy.distutils.exec_command import filepath_from_subprocess_output

# sbolVisualDir= "../../SBOL-visual/Glyphs"
sbolVisualDir = "../versions/v3.0/Glyphs"  # TODO: check for updating to external resource for glyphs
# sbolVisualDir = "https://github.com/SynBioDex/SBOL-visual/tree/master/Glyphs"


# sbolVisualDir = "../Glyphs"


def getTermName(identifiers, file):
    for key, value in identifiers.items():
        if value == file:
            print("  Term Name:" + key)
            return key
    raise ("No identifier has been found for file " + file)


def parseFile(filePath, identifiers):
    dir = os.path.dirname(filePath)
    md = MarkDown(sbolVisualDir, filePath)
    md.parseMdFile()
    termName = getTermName(identifiers, filePath)
    addOntologyTerms(md, termName)


def updateTitle(title, fileNameFragments):
    if title.endswith("Glyph"):
        title = title[:-len("Glyph")]


    fragmentCount = len(fileNameFragments)
    suffix = fileNameFragments[fragmentCount - 3]
    if suffix == "InteractionNodes":
        suffix = "Node"
    elif suffix == "Interactions":
        suffix = "Interaction"
    elif suffix == "MolecularSpecies":
        suffix = "Species"
    elif suffix == "SequenceFeatures":
        suffix = "SequenceFeature"
    else:
        raise ("Can't infer the identifier for " + title)
    return title + suffix + "Glyph"


def updateExisting(filePath, existingFilePath):
    fileNameFragments = filePath.split('/')
    fileNameFragmentsExisting = existingFilePath.split('/')
    if len(fileNameFragmentsExisting) > len(fileNameFragments):
        # The existing record is in a deeper directory, prioritise the current record and add the suffix to the existing record
        return True
    elif "Interaction/" in filePath and "InteractionNodes/" in existingFilePath:
        return True
    else:
        return False


def inferIdentifiers(directory, identifiers):
    files = os.listdir(directory)

    for file in files:
        filePath = directory + "/" + file
        if os.path.isdir(filePath):
            inferIdentifiers(filePath, identifiers)
        elif file == "README.md":
            md = MarkDown(sbolVisualDir, filePath)
            md.parseMdFile()
            visualMd = SBOLVisualMarkDown(md)
            title = visualMd.getGlyphLabel()
            existingFilePath = identifiers.get(title)
            if existingFilePath == None:
                # A new record, add to the dictionary
                identifiers[title] = filePath
            else:
                fileNameFragments = filePath.split('/')
                fileNameFragmentsExisting = existingFilePath.split('/')
                if updateExisting(filePath, existingFilePath):
                    existingTitle = updateTitle(title, fileNameFragmentsExisting)
                    identifiers[existingTitle] = existingFilePath
                    identifiers[title] = filePath
                    print("******Changed the identifier from " + title + " to " + existingTitle + ". File:" + existingFilePath)
                    print("***********The new term for " + title + " has also been added. File:" + filePath)

                else:
                    # Prioritise the existing record, add the suffix to the current record.
                    newTitle = updateTitle(title, fileNameFragments)
                    identifiers[newTitle] = filePath
                    print("******Changed the identifier from " + title + " to " + newTitle + ". File:" + filePath)
                    print("*********** The existing term for " + title + " was kept. File:" + existingFilePath)
    return identifiers


def parseFiles(directory):
    files = os.listdir(directory)

    for file in files:
        if file != "Deprecated":
            filePath = directory + "/" + file
            if os.path.isdir(filePath):
                # print ("******************dir:" + filePath)
                parseFiles(filePath)
            elif file == "README.md":
                parseFile(filePath, identifiers)


identifiers = inferIdentifiers(sbolVisualDir, {})
print("...")
#print("Creating the ontology! ...")
parseFiles(sbolVisualDir)
#print("saving ontology ...")
# saveOntology(tag)
saveOntology()
print("done!")
