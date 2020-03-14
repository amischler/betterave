Sur le serveur de production :

1. Backup postgres database :
   ./betterave-backup.sh
   Cette commande va créer un fichier betterave.bak
2. Pour vérifier que le backup est OK :

-   le récupérer localement via scp
-   dropper puis recréer la base betterave locale depuis psql : DROP DATABASE betterave; CREATE DATABASE betterave;
-   restaurer le backup : psql betterave < betterave.bak

3. Préparer le package de l'application :
   mvnw -Pprod clean package
4. Déposer le war de l'application sur le serveur distant dans le dossier betterave-runtime avec pour nom betterave-<version>.war :
   scp target/betterave-0.0.1-SNAPSHOT.war user@host:~/betterave-runtime/betterave-0.0.1-timestamp.war
5. Identifier l'ID du process en cours via :
   ps aux (/usr/bin/java -Dsun.misc.URLClassPath.disableJarChecking=true -jar /home/antoine/betterave-runtime/betterave-0.0.1)
6. Killer le process en cours :
   kill <PID>
7. Démarrer le nouveau runtime avec la commande
   betterave.sh <version>
