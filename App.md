# PersonalAgent

Die App soll dabei helfen, eine ausgeglichene Work-Life-Balance zu pflegen. Sie unterstützt die User*Innen bei der Planung ihres Tages und soll aufzeigen, in welche Aktivitäten verhältnismäßig zu viel Zeit und Energie gesteckt werden - und in welche zu wenig.

## Zielgruppe und Problemstellung

In einer so schnellebigen Leistungsgesellschaft wie unserer heutigen gehen viele Menschengruppen in ihrem Leben unter. Völlig überarbeitet und unausgeglichen fallen sie nachts ins Bett. Arbeiter*Innen, Student*Innen, Schüler*Innen - jede*r kann Opfer dieser ungesunden Lebensweise werden und die Folgen gehen weit über nur schlechte Laune hinaus. Gerade deshalb ist es wichtig, sich mit der eigenen Zeiteinteilung zu befassen und im Auge zu behalten, ob ein gesundes Gleichgewicht zwischen Arbeit und Freizeit herrscht.

Da das Smartphone zum Alltag Vieler gehört, dient es ideal als helfende Hand, den Menschen bei genau diesem Problem zu unterstützen. Unsere App setzt am unstrukturierten und unausgeglichenen Tagesablauf der User*Innen an und soll sie dabei motivieren, die eigene Zeit besser zu planen.

## Features und Szenarien
<img src="https://user-images.githubusercontent.com/79211586/134600801-087842ef-bb94-4d30-b5a7-7d40b9b1556e.png" align="right">

Grundlage der Funktionsweise des PersonalAgents ist die Aufteilung von Aktivitäten in vier Kategorien:

* **Arbeit** - der Beruf, die Schule oder Vorlesungen an der Uni. Hierunter fallen alle Aufgaben, die mit der Hauptbeschäftigung zu tun haben
* **Hobby** - ein Musikinstrument spielen, Lesen, ... - die Hobbys des Users
* **Fitness** - sportliche Aktivitäten
* **Termin** - von den anderen Kategorien unabhängige Aktivitäten, etwa ein Zahnarztbesuch

Der PersonalAgent hilft nun die Aktivitäten der Kategorien Arbeit, Hobby und Fitness zu balancieren. Es wird ein Verhältnis von 2:1:1 angestrebt, wobei Arbeit natürlicherweise den Großteil ausmacht und von Hobby und Fitness ausgeglichen werden soll. 

Um die investierte Zeit & Energie in einzelne Aufgaben zu messen und sich selbst für deren Erfüllung zu belohnen, wird jede Aktivität (ausgenommen Termine) mit einem Schwierigkeitsgrad versehen:

* **Einfach** (10 Punkte, grüner Indikator)
* **Mittel** (20 Punkte, gelber Indikator)
* **Schwer** (30 Punkte, roter Indikator)

### Hinzufügen und Bearbeiten von Aufgaben
Beim Erstellen eines neuen Eintrags für die To-Do-Liste gibt der User die Kategorie, die Schwierigkeit und einen Namen an. Durch Swipe-Gesten können Einträge als abgeschlossen markiert oder gelöscht werden und mit einem längeren Klick lassen sie sich bearbeiten. Nach Abschluss erhält der User die zuvor festgelegten Punkte.


![ezgif com-gif-maker (5)](https://user-images.githubusercontent.com/79211586/134604843-e2736565-78d4-4f29-8a1f-3c7635e26fa7.gif) ![ezgif com-gif-maker (6)](https://user-images.githubusercontent.com/79211586/134605593-589a3b5e-75a0-4efd-87ff-2229114b44a4.gif)

### Der Kalender

Der Kalender zeigt den aktuellen Monat an, durch Swipes kann dieser gewechselt werden. Mit einem Klick auf den gewünschten Tag können bereits im Voraus Aktivitäten an diesem geplant und eingetragen werden. An diesem Tag erscheinen dann die Einträge auf der Startseite.
In der Tagesansicht reichen Klicks auf die oberen Pfeile, um zwischen vorherigem und nächstem Tag zu wechseln.

<img src="https://user-images.githubusercontent.com/79211586/134606635-b2f6218e-2e7a-4379-844e-225a90a3284f.png"> <img src="https://user-images.githubusercontent.com/79211586/134606640-205b6dc5-f452-43ea-8310-ae93a232ee1e.png">

### Die Wochenübersicht

In drei Wochendiagrammen - pro Kategorie eins - wird der entsprechende aktuelle Punktestand und das vom Tagesziel hochgerechnete Wochenziel angezeigt. Mit Abschluss weiterer Aktivitäten füllen sich die Kreise um den Nutzer*Innen deutlich zu machen, wie der aktuelle Stand ist und in welchen Kategorien noch nachgebessert werden muss. Der Punktestand wird jeden Montag automatisch zurückgesetzt.

<img src="https://user-images.githubusercontent.com/79211586/134607625-0e0e75c3-e46b-47a6-8e77-1be8a19638b9.png">


### Einstellungen, Benachrichtigungen und Darkmode
Hier können die Benachrichtigungen ein- und ausgeschaltet bzw. deren Zeitpunkte festgelegt werden. Bei Aktivierung erhält der User zu zwei ausgesuchten Uhrzeiten Benachrichtigungen, die daran erinnern sollen, den aktuellen Tag zu planen und abgeschlossene Aufgaben abzuhaken. Außerdem werden hier der am Startbildschirm angezeigte Name sowie das tägliche Punkte Ziel festgelegt. Das Design der App passt sich dem im System eingestellten Light- oder Darkmode an.

![rsz_einstellungsscrrrenshot](https://user-images.githubusercontent.com/79211586/134966460-f3c94472-1222-4009-9cec-5783f4a04860.png) <img src="https://user-images.githubusercontent.com/79211586/134970759-9cb53f45-a4fa-4304-abcd-7a906440cb58.png"> ![ezgif com-gif-maker (7)](https://user-images.githubusercontent.com/79211586/134971875-ea3c8cce-a3b5-4824-a199-e7df03caa1cc.gif)


## Szenario 1
**Max, 21, ist Jurastudent** und bemerkt, wie er ständig gestresst und schlecht gelaunt ist. In sein Studium steckt er sehr viel Zeit, vergisst dabei aber häufig, auch auf sich selbst zu achten. Er fühlt sich unausgeglichen und möchte deshalb etwas mehr Struktur in seinen Alltag bringen. Da er selbst allerdings nur seine Arbeiten im Kopf hat und sonstige Aktivitäten schnell vernachlässigt, benutzt der den PersonalAgent, um den Überblick über seine Zeiteinteilung zu behalten. Dazu geht er folgendermaßen vor:

* Er lädt sich die App herunter und stellt sich zuerst seinen Nutzernamen und ein Tagesziel in den Einstellungen ein. Da er neben dem Studium keinen Job hat, setzt er sich ein Ziel von 60 Punkten am Tag. Er geht täglich um 22:00 Uhr ins Bett und setzt sich dementsprechend den Benachrichtigungszeitpunkt.
* Danach wechselt er auf den Startbildschirm und fügt seine Lerneinheiten als Einträge der Kategorie _Arbeit_ hinzu. Da es sich um anstrengende & ermüdende Aufgaben handelt, stellt er die Schwierigkeit auf _schwer_.
* Nun da er diese Pflichtaufgaben vor Augen hat, kann er sich überlegen, wie er den restlichen Tag mit Sport und seinen Hobbys ausgleicht. Da auf der Startseite eine gute Wettervorhersage für den heuten Tag sieht, beschließt er, eine Runde zu laufen und danach etwas zu zeichnen. Die Einträge versieht er mit _mittlerer_ und _einfacher_ Schwierigkeit. 
* Damit erreicht er am Ende des Tages die selbe Punktzahl in _Arbeit_ wie in den beiden Ausgleichsaktegorien. Zufrieden geht er Nachts ins Bett, erhält eine Erinnerung seine Ziele abzuhaken und überlegt sich bereits, was er am nächsten Tag machen möchte.

## Szenario 2
**Sabine, 34, ist in einer Führungsposition eines Automobilhändlers.** Sie führte bereits einen Tagesplaner in schriftlicher Form, allerdings fällt es ihr schwer diesen von Termin zu Termin mitzunehmen und gleichzeitig unterwegs den Überblick über ihren Tagesplan zu behalten. Deswegen entscheidet sie sich dazu, den PersonalAgent auf ihrem Smartphone zu installieren:

* Zuerst setzt sie sich in den Einstellungen ihr Tagesziel auf 100 Punkte. Da sie sich sich für ihre langen Arbeitstage mehr als 30 Punkte (_schwer_) gutschreiben würde, teilt sie ihre Einträge der _Arbeit_-Kategorie in zwei mit jeweils _schwierig_ als Aufwandsgrad.
* Danach klickt sie in der Navigation auf den Kalender und fügt bereits im Voraus ihre Termine an den entsprechenden Tagen hinzu.
* Da Sabine aufgrund ihres Berufs bereits viel im Voraus planen muss klickt sie im Kalender auf den heutigen Tag und fügt ihre Ausgleichsaktivitäten hinzu. Daraufhin navigiert sie einfach Tag für Tag durch die ganze Woche und wiederholt dies für jeden Tag.
* Nun kann sie zwischen ihren vielen Terminen im Beruf auch unterwegs auf dem Smartphone ihre erledigten Aufgaben abschließen und im Blick behalten, was heute noch ansteht.
* Am Ende der Woche betrachtet sie in der Wochenübersicht ihre gesammelten Punkte. Sie stellt fest, dass immernoch ein großes Defizit an _Fitness_-Aktivitäten hat und plant deshalb für die nächste Woche bereits mehrere Einträge in dieser Kategorie ein.


## Erreichter Stand

Grundsätzlich kommt die finale Umsetzung unseres Projekts dem geplanten Konzept sehr nahe. In der Entwicklung der einzelnen Features kamen meist zusätzliche Ideen zur Verbesserung. So etwa, dass das Wetter anstelle eines festgelegten Orts dynamisch nach der aktuellen Position des Geräts angezeigt wird und Einträge der To-Do-Liste über Swipe-Gesten verwaltet werden.

Die Einfärbung der Tage im Kalender entsprechend der erreichten Punkte wurde zunächst ausgelassen, da der Fokus eher auf das Punkteergebnis einer Woche anstatt eines Tages gelegt wird.

Insgesamt wurden alle geplanten Activities & Kernfunktionen umgesetzt.
