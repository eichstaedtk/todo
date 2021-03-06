# Android App zur Verwaltung von Aufgaben (ToDos)
<sub>Maintainer: [Konrad Eichstädt](mailto:konrad.eichstaedt@gmx.de)</sub>  
<br/>
<br/>
**Inhalt:**  
[1. Einführung und Ziele ](#1-einf%C3%BChrung-und-ziele)<br/>
[1.1 Aufgabenstellung](#11-aufgabenstellung)<br/>
[1.2 Qualitätsziele ](#12-qualit%C3%A4tsziele)<br/>
[1.3 Projektbeteiligte ](#13-projektbeteiligte-stakeholder)        
[2. Randbedingungen ](#2-randbedingungen)<br/>
[3. Kontextabgrenzung](#3-kontextabgrenzung)<br/>
[4. Lösungsstrategie](#4-l%C3%B6sungsstrategie)<br/>
[5. Baustein](#5-bausteinsicht)<br/>
[6. Laufzeitsicht](#6-laufzeitsicht)<br/>
[7. Verteilungssicht](#7-verteilungssicht)<br/>
[8. Entwurfsentscheidungen](#8-entwurfsentscheidungen)<br/>
[9. Qualitätsziele](#9-qualit%C3%A4tsziele)<br/>
[10. Risiken und technische Schulden](#10-risiken-und-technische-schulden)<br/>
[11. Glossar](#11-glossar)<br/>
[12. Release Notes](#12-release-notes)<br/>

# 1. Einführung und Ziele #
Entwicklung einer nativen Android Anwendung zur Verwaltung von Aufgaben mit Hilfe der Programmiersprache Java oder Kotlin. 
## 1.1 Aufgabenstellung ##
Zielstellung ist die Umsetzung einer Anforderungsliste einer Aufgabenverwaltung mit Hilfe des Android Frameworks. Hierbei soll der Umgang und die Umsetzung einer
Smartphone Anwendung auf Basis von Android erlernt werden. 
## 1.2 Qualitätsziele ##
Usability und Datensicherheit. 
## 1.3 Projektbeteiligte (Stakeholder) ##
Konrad Eichstädt
<br/>
Jörn Kreutel
# 2. Randbedingungen
Berücksichtigen Sie für die Erfüllung der unten genannten Anforderungen die folgenden Rahmenbedingungen:
- Die Anforderungen sind durch Implementierung einer nativen Android-Anwendung in Java oder Kotlin umzusetzen.
- Die Einbindung von Fremdbibliotheken für Backendzugriffe, z.B. für den Zugriff auf eine Webanwendung via HTTP und JSON, für
Objekt Relational Mapping oder die Nutzung von Google Firebase als Backend, ist erlaubt. Als alleinige Grundlage aller Anforderungen
zur lokalen und remote Datenspeicherung und Synchronisierung ist die Verwendung von Firebase nicht zulässig.
- Die Verwendung der durch Android Studio generierten LoginActivity ist erlaubt.
- Erlaubt ist außerdem die Nutzung der durch Android bereit gestellten Komponenten für das Versenden von Nachrichten, die Auswahl
von Kontakten sowie die Umsetzung von Kartenansichten.
- Alle anderen Activities / Fragments, die Ihre Anwendung verwendet und die zur Erbringung der projektspezifischen Anforderungen
dienen, müssen durch die Anwendung selbst bereitgestellt werden und dürfen nicht aus Fremdbibliotheken importiert werden.
- Der Einsatz von WebViews ist nicht erlaubt.
- Nicht erlaubt ist außerdem der Einsatz von Frameworks oder Werkzeugen zur plattformübergreifenden Anwendungsentwicklung (z.B.
Cordova, Ionic oder React Native, etc.) zur Entwicklungs- oder Laufzeit.
Bei Nichteinhaltung der Rahmenbedingungen wird ggf. die gesamte Implementierung als ungültig im Hinblick auf die formulierten
Anforderungen gewertet.
Beachten Sie für Gruppenprojekte außerdem, dass Gruppen von 2 Personen zusätzlich zu den Anforderungen für eine Person die am
Ende des Dokuments beschriebenen Erweiterungen umsetzen müssen. 
# 3. Kontextabgrenzung
Nachfolgende Abbildung zeigt die Kontextsicht dieser Anwendung. 
![](kontext-diagramm.png)

Die Anwendung läuft auf einem Android Smartphone. Die Daten werden zum einen auf der lokalen SQLite Datenbank und zum anderen auf der Google Firebase 
Dokumentendatenbank gespeichert. Die Persistierung in Firestore geschieht lediglich bei bestehender Internetverbindung.  
# 4. Lösungsstrategie
Als Programmiersprache wurde Java wegen der vorhandenen Kenntnisse ausgewählt. 
Als Server Techologie wurde [Google Cloud Firebase](https://console.firebase.google.com/) ausgewählt.
Für die Umsetzung der nebenläufigen Hintergrundaufgaben wurde der Android Empfehlung durch Einsatz von [RxJava](https://github.com/ReactiveX/RxJava) gefolgt. 
Für die Implementierung der Integrationstests wurde des [Framework Espresso](https://developer.android.com/training/testing/espresso) ausgewählt. 
Als Dokumentationstandard wurde [ARC42](https://arc42.de/) auf Basis von Markdown ausgewählt. 
Als Build Werkzeug wurde [Gradle](https://gradle.org/) ausgewählt.

Das grundlegende Design der Anwendung folgt dem Clean Architecture Pattern. Alle Klassen zur Umsetzung der fachlichen Aufgaben befinden sich im inneren der Awnedungsstruktur 
und die Klassen zur Umsetzung der UI und Persistierungsaufgaben im äußeren Bereich. 
![](CleanArchitecture.jpg)  
# 5. Bausteinsicht
Nachfolgende Abbildung zeigt die Struktur der Anwendung.
<br/> 
![](baustein-diagramm.png)

Im domain Bereich befinden sich alle Klassen zur Umsetzung der fachlichen Anforderung. 
im infrastructure Bereich befinden sich alle Klassen zur Umsetzung der Aufgaben zur Anzeige und Persistierung.  
# 6. Laufzeitsicht
Zeigt das Zusammenspiel der Architekturbausteine zur Laufzeit. 
# 7. Verteilungssicht
Auf welchen Systemen laufen die Systemkomponenten. 
# 8. Entwurfsentscheidungen
Java als Programmiersprache
RxJava für die Nebenläufigkeit
# 9. Qualitätsziele
Szenarien konkretisierte Qualitätsanforderungen. 
# 10. Risiken und technische Schulden
Die derzeit geringen Kenntnisse im Team sind ein Projektrisiko. 
# 11. Glossar
ToDo: Zu erledigende Aufgabe
User: Authentifizierung des Systems
Kontakt: Kontakt eines Anwenders
# 12. Release Notes
| Komponente        | letzte Änderung           | Version  |
| ------------- |:-------------:| -----:|
| ToDoApp      | 30.06.2021 | 1.0.0 |