data <- read.table("C:/Users/PC/Desktop/miete03.asc", header=TRUE)

names(data)

str(data)

dim(data)

head(data)

View(data)

nettomiete <- data[,1]
nettomiete

nm Nettomiete in Euro
nmqm Nettomiete Euro pro qm
wfl Wohnfläche in qm
rooms Anzahl der Zimmer je Wohnung
bj Baujahr der Wohnung
bez Stadtbezirk
wohngut Gute Wohnlage? (Ja=1, Nein=0)
wohnbest Beste Wohnlage? (Ja=1, Nein=0)
ww0 Warmwasserversorgung vorhanden? (Ja=0, Nein=1)
zh0 Zentralheizung vorhanden? (Ja=0, Nein=1)
badkach0 Gekacheltes Badezimmer? (Ja=0, Nein=1)
badextra Besondere Zusatzausstattung im Bad? (Ja=1, Nein=0)
kueche Gehobene Küche? (Ja=1, Nein=0)
