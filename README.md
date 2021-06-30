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
[8. Betrieb und Wiederherstellung](#8-betrieb-und-wiederherstellung)<br/>
[8.1 Ansprechpartner , Service Level](#81-ansprechpartner-service-level)<br/>
[8.2 Installation / Konfiguration](#82-installation-konfiguration)<br/>
[9. Entwurfsentscheidungen](#9-entwurfsentscheidungen)<br/>
[10. Qualitätsziele](#10-qualit%C3%A4tsziele)<br/>
[11. Risiken und technische Schulden](#11-risiken-und-technische-schulden)<br/>
[12. Glossar](#12-glossar)<br/>
[13. Release Notes](#13-release-notes)<br/>

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
Sicht aus der Vogelperspektive. Zeigt das System als Blackbox und den Zusammenhang zu Nachbarsystemen. 
# 4. Lösungsstrategie
Als Programmiersprache wurde Java wegen der vorhandenen Kenntnisse ausgewählt. 
Als Server Techologie wurde Firebase ausgewählt.
Für die Umsetzung der nebenläufigen Hintergrundaufgaben wurde der Android Empfehlung durch Einsatz von RxJava gefolgt. 
Für die Implementierung der Integrationstests wurde des Framework Espresso ausgewählt. 
Als Dokumentationstandard wurde ARC42 auf Basis von Markdown ausgewählt. 
Als Build Werkzeug wurde Gradle ausgewählt.  
# 5. Bausteinsicht
Statische Zerlegung des Systems in Bausteine.  
# 6. Laufzeitsicht
Zeigt das Zusammenspiel der Architekturbausteine zur Laufzeit. 
# 7. Verteilungssicht
Auf welchen Systemen laufen die Systemkomponenten. 
# 8. Betrieb und Wiederherstellung #
## 8.1 Ansprechpartner , Service Level
Konrad Eichstädt
## 8.2 Installation / Konfiguration ##
## 8.3 Wiederherstellung ##
Wie kann das defekte System wiederhergestellt werden. 
# 9. Entwurfsentscheidungen
Java als Programmiersprache
RxJava für die Nebenläufigkeit
# 10. Qualitätsziele
Szenarien konkretisierte Qualitätsanforderungen. 
# 11. Risiken und technische Schulden
# 12. Glossar
Fachliches Glossar. 
# 13. Release Notes
| Komponente        | letzte Änderung           | Version  |
| ------------- |:-------------:| -----:|
| ToDoApp      | 30.06.2021 | 1.0.0 |