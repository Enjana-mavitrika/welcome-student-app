# PROJET WELCOME STUDENT APP 2020
## Description :
Application web basée sur la technologie __Web semantique__ pour aider les nouveaux étudiants internationaux à trouver facilement les endroits clés de leurs villes d'accueil. 
## Authors :
 - Solofo RABONARIJAONA
 - Ibrahima DIALLO

### SUIVIS AVANCEMENT
| __Fonctionnalités de base__   |  __Statut__    |
|---------------------------|:----------:|
| Setup triple store |  DONE       |
| Use existing ontologies(schema) |    DONE   |
| Define customize vocabulary | TODO |
| Documentation ontology   | TODO |
| Populate triple multiple source | DONE |
| Website front end of the data  | DONE |
| Show group of entities in HTML TABLE | DONE |
| Extract data with SPARQL (wikidata & apache jena-fuseki-geosparql) | DONE |

| __Fonctionnalités avancées__   |  __Statut__    |
|--------------------------------|:--------------:|
| Show group of entities in MAP (openstreet map) | DONE |
| Add content negotiation (humman and machine) | DONE |
| add structured data to the web pages, using RDFa or JSON-LD | TODO |
| FILTER bus stop around schools with MAX distance | DONE | 
| Add meteo for each city | TODO |
| FILTER city with good weather | TODO |
| FILTER city with bad weather | TODO |
| Add interface to EXTEND data easily for other City or Country | TODO | 


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
