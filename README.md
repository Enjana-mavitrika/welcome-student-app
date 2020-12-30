# PROJET WELCOME STUDENT APP 2020
## Description :
Application web basée sur la technologie __Web semantique__ pour aider les nouveaux étudiants internationaux à trouver facilement les endroits clés de leurs villes d'accueil. 
## Authors :
 - Solofo RABONARIJAONA
 - Ibrahima DIALLO

### SUIVIS AVANCEMENT
| __Fonctionnalités de base__   |  __Statut__    |
|---------------------------|:----------:|
| Setup triple store |  :white_check_mark:       |
| Use existing ontologies(schema) |    :white_check_mark:   |
| Define customize vocabulary | :white_check_mark: |
| Documentation ontology   | :white_check_mark: |
| Populate triple multiple source | :white_check_mark: |
| Website front end of the data  | :white_check_mark: |
| Show group of entities in HTML TABLE | :white_check_mark: |
| Extract data with SPARQL (wikidata & apache jena-fuseki-geosparql) | :white_check_mark: |

| __Fonctionnalités avancées__   |  __Statut__    |
|--------------------------------|:--------------:|
| Show group of entities in MAP (openstreet map) | :white_check_mark: |
| Add content negotiation (humman and machine) | :white_check_mark: |
| add structured data to the web pages, using RDFa or JSON-LD | :white_check_mark: |
| FILTER bus stop around schools with MAX distance | :white_check_mark: | 
| Add meteo for each city | :white_check_mark: |



## Demo video 
[Voir la vidéo sur youtube](https://youtu.be/5xC2lqzb1zk)


## Ontologie
![alt text](https://raw.githubusercontent.com/Enjana-mavitrika/welcome-student-app/master/Conception-Ontology.png?raw=true)


## Architecture générale
![alt text](https://raw.githubusercontent.com/Enjana-mavitrika/welcome-student-app/master/Conception-Architecture.png?raw=true)


## Pré-requis (développeur)
- JAVA 15
- Visual Studio Code 

## Instructions lancement (développeur)
1. Télécharger ce repository
2. Lancer le serveur apache jena-fuseki-geosparql :
    - Entrez dans le répertoire `geosparql-fuseki-1.1.2/bin/`
    - Ouvrez un terminal dans le répertoire
    - Exécutez le script `./start_triplestore_server.sh`
3. Lancer l'application web :
    - Entrez dans le répértoire `app/`
    - Ouvrez le répértoire dans Visual Studio Code 
    - Lancer le projet avec SPRING-BOOT-DASHBOARD

## pré-requis (Utilisateur)
- JAVA 15

- TODO: à compléter
