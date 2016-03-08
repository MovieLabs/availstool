Here are the instructions for setting up a Java project to build availslib.

1) set up github:

> mkdir <your target directory>
> cd <your target directory> (e.g. availstool)
> git init
> git remote add github https://github.com/pgj-ml/availstool.git

2) download from github

> git fetch github

3) eclipse-setup

* start eclipse
* menu window->show view->...->git 
* in git repositories panel, select "add an existing local repository" button
* under git repositories, right-click 'availstool' and select 'switch to->new branch'
* name new branch 'master'
* right click on working directory, select 'Import Projects..." (from git)
* make sure files are in "com.movielabs.availstool" package
* add libraries to build path (you need to download these separately):
     commons-cli-1.3.1.jar
     poi-3.13-20150929.jar
     poi-ooxml-3.13-20150929.jar
     poi-ooxml-schemas-3.13-20150929.jar
     xmlbeans-2.6.0.jar
     apache-log4j-2.4.1-bin/log4j-api-2.4.1.jar
     apache-log4j-2.4.1-bin/log4j-core-2.4.1.jar
4) configure JavaDoc

* this is usually at C:\Program Files\Java\jdk1.8.0_65\bin\javadoc.exe
