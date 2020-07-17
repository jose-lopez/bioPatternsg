# produce_dictionary.py
# version 0.1
#
import spacy
import csv

nlp = spacy.load("en_core_web_sm")
# text_as_string = open('ejemplos_31032020.txt', 'r').read()
text_as_string = open('caly-et-al-2020-ivermectin.txt', 'r').read()
text_as_string = text_as_string.replace("\r"," ").replace("\n"," ")
doc = nlp(text_as_string)

# salida csv
# based on https://stackoverflow.com/questions/33309436/python-elementtree-xml-output-to-csv
# with open('spacy_dictionary-resumidor.csv', 'w', newline='') as r:
with open('caly-et-al-2020-ivermectin.csv', 'w', newline='') as r:
    writer = csv.writer(r,  delimiter=' ',
                            quotechar='"', quoting=csv.QUOTE_ALL)
    writer.writerow(['token_id','token','lemma','pos','entity'])  # WRITING HEADERS

    count2 = 1;
    for token in doc:
        for ent in doc.ents:
            if (token.text==ent.text):
                entity = ent.label_
                break
            else:
                entity = 'No entity'
        writer.writerow([count2, token.text, token.lemma_, token.tag_, entity])
        if token.tag_=="VBN" or token.tag_=="VBZ":
            # verb(verbo([transcriptional-activate])) --> [transcriptional,'-',activate].
            # print(token.lemma_ + " --> " + token.text)
            print("verb(verbo(["+token.lemma_ + "])) --> [" + token.text+ "].")
        count2 += 1
