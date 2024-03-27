# PFE

# Cahier des charges pour l'application de suivi de présence sur site

## 1. Introduction

L'objectif de ce projet est de concevoir et développer une application web pour automatiser le suivi de la présence sur site des employés d'un organisme. L'application doit être réalisée en utilisant les technologies Java Spring Boot pour le backend et Vue.js pour le frontend.

## 2. Objectifs

### 2.1 Objectif général

Développer une application permettant de gérer efficacement le suivi de présence sur site des employés d'un organisme à partir d'un fichier Excel contenant les informations de présence de chaque employé pour chaque jour du mois.

### 2.2 Objectifs spécifiques

1. Création d'une interface utilisateur conviviale pour importer un fichier Excel contenant les données de présence des employés.
2. Implémentation d'un backend robuste en Java Spring Boot pour traiter les données du fichier Excel et effectuer des calculs statistiques.
3. Développement d'un frontend dynamique en Vue.js permettant d'afficher les données de présence et les statistiques générées.
4. Analyse des données pour chaque employé afin de déterminer le nombre de jours travaillés sur site.
5. Génération de statistiques pour chaque équipe (squad) afin de voir combien de membres ont travaillé sur site.

## 3. Fonctionnalités

### 3.1 Backend (Java Spring Boot)

1. Endpoint pour importer un fichier Excel contenant les données de présence.
2. Fonctionnalités de lecture et de traitement des données Excel pour extraire les informations pertinentes.
3. Calcul du nombre de jours travaillés sur site pour chaque employé.
4. Calcul des statistiques pour chaque squad sur le nombre de membres travaillant sur site.

### 3.2 Frontend (Vue.js)

1. Interface utilisateur pour importer un fichier Excel.
2. Affichage des données de présence pour chaque employé.
3. Affichage des statistiques sur le nombre de jours travaillés sur site pour chaque employé.
4. Affichage des statistiques sur le nombre de membres travaillant sur site pour chaque squad.

## 4. Exigences fonctionnelles

1. L'application doit permettre à l'utilisateur d'importer un fichier Excel.
2. Les données du fichier Excel doivent être traitées pour extraire les informations de présence.
3. L'application doit calculer le nombre de jours travaillés sur site pour chaque employé.
4. L'application doit générer des statistiques sur la présence sur site pour chaque employé et chaque squad.
5. L'interface utilisateur doit être intuitive et conviviale pour faciliter l'utilisation de l'application.

## 5. Exigences techniques

1. Utilisation de Java Spring Boot pour le développement du backend.
2. Utilisation de Vue.js pour le développement du frontend.
3. Utilisation de bibliothèques ou de frameworks Java et JavaScript appropriés pour le traitement des fichiers Excel et l'interaction avec l'interface utilisateur.
4. Déploiement de l'application sur une plateforme de cloud computing (AWS, Azure, etc.) ou en local selon les besoins de l'organisme.

## 6. Livrables

1. Code source complet de l'application.
2. Documentation technique décrivant l'architecture, les technologies utilisées et les instructions d'installation et de déploiement.
3. Manuel utilisateur décrivant comment utiliser l'application, importer un fichier Excel, et interpréter les données et statistiques générées.

## 7. Planning prévisionnel

1. **Phase de conception** (1 semaine)
   - Analyse des besoins et des spécifications.
   - Conception de l'architecture de l'application.

2. **Phase de développement** (6 semaines)
   - Développement du backend en Java Spring Boot.
   - Développement du frontend en Vue.js.
   - Intégration des fonctionnalités et des interfaces utilisateur.

3. **Phase de test et de validation** (2 semaines)
   - Tests unitaires et tests d'intégration.
   - Validation des fonctionnalités par l'équipe projet.

4. **Phase de documentation et de livraison** (1 semaine)
   - Rédaction de la documentation technique et du manuel utilisateur.
   - Préparation des livrables finaux.

## 8. Équipe projet

- Chef de projet
- Développeurs backend (Java Spring Boot)
- Développeurs frontend (Vue.js)
- Testeurs

## 9. Budget

Le budget alloué pour ce projet sera déterminé en fonction des ressources humaines nécessaires et des coûts liés à l'infrastructure et aux outils de développement.

## 10. Contraintes

- Respect des délais de livraison.
- Utilisation des technologies spécifiées (Java Spring Boot pour le backend, Vue.js pour le frontend).
- Conformité aux normes de sécurité et de confidentialité des données.

Ce cahier des charges est soumis à validation par l'équipe projet et peut être sujet à des modifications en fonction des retours et des ajustements nécessaires au cours du développement de l'application.
