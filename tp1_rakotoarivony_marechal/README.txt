Application Serveur FTP
Auteur : Allan Rakotoarivony
	 Maréchal Tanguy
-----------------------------------------------------------------
Introduction
-----------------------------------------------------------------
Notre application a été développé dans le cadre du cours de Construction d'Applications Réparties. Il s'agit d'un serveur FTP basique pour le transfert de fichiers.
Les commandes suivantes sont traités : USER, PASS, CDUP, PWD, CWD, RETR, STOR, MKD, RMD, PORT, TYPE, SYST, QUIT.


-----------------------------------------------------------------
Design du code
-----------------------------------------------------------------
Une des particularité de notre application est son extensibilité au niveau des traiteurs de commandes. En effet, grâce au polymorphisme, il suffit de créer une classe nommée Process<COMMAND> qui implémente ProcessCommand pour avoir un traiteur de la commande <COMMAND>.
Le nommage de la classe est à respecter car pour traiter une commande, on utilise la fonction classForName(Process<COMMAND>). Si le nommage n'est pas respecté, le traiteur de commande sera considéré comme non existant.

La partie identification sur le serveur se fait grâce à la lecture d'un fichier log.txt dans utils/ .Son format est simple : sur une ligne, on trouve 3 éléments qui sont : le nom d'utilisateur, le mot de passe et le dossier utilisateur en chemin absolu.
Le serveur a besoin de ce fichier pour fonctionner.
Les dossiers utilisateurs doivent aussi exister.

-----------------------------------------------------------------
Code sample
-----------------------------------------------------------------
Voici le code du créateur de traiteur de commande:

public ProcessCommand processBuild(String[] processString) throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		String command = processString[0];
		Class<?> cls = Class.forName("ftp.process.Process"+command);
		Object obj = cls.newInstance();
		ProcessCommand cmd = (ProcessCommand) obj;
		return cmd;

Dans ce code processString est un tableau dont le premier élément contient le nom de la commande demandée. On instancie à partir de ce nom de commande le tratier qui va avec grâce au Class.forName()

--------------------------------------------------------------------
Interfaces et polymorphisme
----------------------------------------------------------------------
-Interface ProcessCommand : C'est une interface pour les processeurs des commandes comme USER ou PASS.
Toutes les classes qui l'implémentent doivent implémenter la méthode int process(String param,FTPCLient client) qui est la methode appelé pour traiter les commandes.

-------------------------------------------------------------------
Gestion d'erreur
--------------------------------------------------------------------
-UnauthorizedChangedDirectoryException : Exception provoqué quand le client essaie d'accéder à un repertoire où il ne devrait pas avoir accès.
Cette exception est capturé quand on essaie de traiter les changement de repertoire et qu'on accède à un repertoire non autorisé.

Liste des catch: 
-catch(IOException) : Cette exception est capturé quand il y a des erreurs de lecture sur les socket.


----------------------------------------------------------------------
Execution du programme
----------------------------------------------------------------------
Il y a deux manière de lancer le programme:
- avec le jar : java -jar FTPServer.jar <numeroPort>
Le numéro de port est optionnel mais doit être un port autorisé.
Par défaut ce sera 2121. 
- en utilisant eclipse
Il faut s'assurer que le fichier utils/log.txt existe et que les dossiers utilisateurs dedans existent bien.
