# PFE

**Cahier des charges pour l'application de gestion d'absences d'un organisme**

**1. Introduction**

L'objectif de ce projet est de développer une application web utilisant Java Spring Boot pour le backend et Vue.js pour le frontend. L'application sera conçue pour gérer les absences des employés d'un organisme, en se basant sur les informations fournies dans un fichier Excel. Elle permettra également de générer des statistiques sur les heures travaillées par chaque employé, ainsi que sur la présence sur site, à la fois au niveau individuel et par équipe.

**2. Fonctionnalités de base**

2.1. Importation de données depuis un fichier Excel :
- L'application doit pouvoir importer les données relatives aux employés à partir d'un fichier Excel.
- Les données importées comprendront les détails sur chaque employé (nom, prénom, numéro d'identification, etc.) ainsi que les informations sur leur présence sur site ou en mode hybride, et les jours d'absence.

2.2. Gestion des horaires de travail :
- L'application devra tenir compte des horaires de travail de base, définis à 40 heures par semaine.
- Les heures travaillées par chaque employé seront calculées en fonction des informations fournies dans le fichier Excel.

2.3. Calcul des statistiques :
- L'application générera des statistiques pour chaque employé, indiquant le pourcentage d'heures travaillées par rapport aux heures de travail prévues.
- Elle calculera également le nombre de jours où chaque employé était présent sur site.

2.4. Gestion par équipe (squad) :
- Les statistiques seront également disponibles au niveau de chaque équipe (squad), permettant de voir combien de membres ont travaillé sur site.

**3. Interface utilisateur**

3.1. Interface d'importation de fichier :
- Une interface intuitive permettra à l'utilisateur d'importer un fichier Excel contenant les données des employés.

3.2. Tableau de bord :
- Un tableau de bord convivial affichera les statistiques générales sur les heures travaillées et la présence sur site.
- Des graphiques clairs et des tableaux récapitulatifs aideront à visualiser les données.

3.3. Affichage des détails de chaque employé :
- L'application permettra de consulter les détails individuels de chaque employé, y compris son historique d'absences et de présence sur site.

**4. Contraintes techniques**

4.1. Backend :
- Utilisation de Java Spring Boot pour le développement du backend.
- Les données seront stockées dans une base de données relationnelle (MySQL, PostgreSQL, etc.).

4.2. Frontend :
- Utilisation de Vue.js pour le développement du frontend.
- Utilisation de bibliothèques ou frameworks CSS (Bootstrap, Tailwind CSS, etc.) pour assurer un design responsive et esthétique.

4.3. Sécurité :
- Mise en place d'un système d'authentification et d'autorisation pour garantir la confidentialité des données.
- Les mots de passe doivent être stockés de manière sécurisée (hachage avec sel).

**5. Livrables attendus**

5.1. Code source de l'application :
- Le code source du backend développé avec Java Spring Boot.
- Le code source du frontend développé avec Vue.js.
- Scripts SQL pour la création de la base de données et l'initialisation des tables.

5.2. Documentation technique :
- Documentation détaillée sur l'architecture de l'application.
- Guide d'installation et de déploiement.

5.3. Manuel utilisateur :
- Guide utilisateur expliquant les différentes fonctionnalités de l'application.
- Instructions pour l'importation des données depuis un fichier Excel.

**6. Échéancier**

Le projet sera développé selon le calendrier suivant :

- Conception et spécifications : [Dates]
- Développement du backend : [Dates]
- Développement du frontend : [Dates]
- Intégration et tests : [Dates]
- Correction des bugs et améliorations : [Dates]
- Livraison finale : [Date]

**7. Validation et suivi**

Des réunions régulières seront organisées pour suivre l'avancement du projet et valider chaque étape avec l'encadrant. Des démonstrations seront également prévues pour présenter les fonctionnalités développées.

**8. Références**

Toute référence utilisée dans le cadre du développement de l'application sera correctement documentée dans la documentation technique.
