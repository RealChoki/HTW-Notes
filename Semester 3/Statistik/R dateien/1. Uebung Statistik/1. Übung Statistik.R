#1. Übung Statistik

#Erzeugen Sie als nächstes einen Vektor mit dem Namen vektor1 aus zehn
#verschiedenen ganzen Zahlen.
vektor1 <- c(1:10)

#Lassen Sie sich in der Konsole die Anzahl der Elemente von vektor1 anzeigen.
length(vektor1)

#Sortieren Sie mit dem Befehl sort den Vektor vektor1 der Größe nach, einmal 
#beginnend mit dem kleinsten und einmal beginnend mit dem größten Element.
sort(vektor1)
rev(sort(vektor1))

#Berechnen Sie die Summe aller Elemente des Vektors vektor1.
sum(vektor1)

#Berechnen Sie das Produkt aller Elemente des Vektors vektor1.
prod(vektor1)

#Erzeugen Sie einen weiteren Vektor vektor2 analog zum ersten und berechnen
#Sie die Summe der beiden Vektoren (Vektoraddition, nicht die Summe der Elemente).
#Weisen Sie das Ergebnis der Variable vektorSumme zu.
vektor2 <- c(11:15)
vektorSumme <- vektor1 + vektor2

#Lassen Sie sich in der Konsole das vierte Element des Vektors vektorSumme
#ausgeben, indem Sie den Vektor endstsprechend indizieren
vektorSumme[4] 

#Erzeugen Sie unter Verwendung des Befehls seq einen Vektor vektor3, der
#alle ganzen Zahlen zwischen 0 und 100 (einschließlich 0 und 100!) enthält,
#die durch 5 teilbar sind.
vektor3 <- seq(from = 0, to = 100, by = 5)

#Verbinden Sie die beiden Vektoren vektor2 und vektor3 mithilfe des Befehls
#c zu einem neuen Vektor vektor4 und lassen Sie sich in der Konsole die Länge
#des neuen Vektors ausgeben.
vektor4 <- c(vektor2,vektor3)
length(vektor4)
