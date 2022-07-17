# crypt-file-test
- Ce projet est réalisé dans le cadre d'un entretien
- C'est un projet SpringBoot classique
- C'est un projet avec une archi clean-archi
## Domain
- Tout le code métier se trouve dans le package : domain
- Le code business est réalisé à partir de la TDD
- La couverture de test du domain est de 100% (car réalisé en TDD)
## Ports
- Tout branchement au métier se fait part l'implémentation des interfaces présentes dans le package domain.ports
- Il y a trois interfaces pour se brancher au code métier : metadataRepository, FileSystemeGateway et SecurityGateway
## Adapters
- Tout interfaçage au domain se trouve dans le package adapters
- Implémentation du repository (volontairement choisi de persister dans un fichier pour que vous puisser imaginer simplement le changement de choix technique si besoin)
- Implémentation de la passerelle de sécu (ici on regarde bêtement dans le fichier de conf)
- Implémentation de du file système ici on enregistre sur le disque local (cette implèmentation peut également facilement être remplacée par la sauvegarde sur serveur distant sans impacter le code métier)
## /download et /upload
- Deux pages ont été créées pour pouvoir télécharger ou uploader 
