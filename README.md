# ImShare/nekodev.xyz - Java http/websocket server
# This repository is copy of old ImShare repository (around 100 commits)

### ImShare (Backend) is using:
* GSON - Licensed under [Apache License 2.0](https://github.com/google/gson/blob/master/LICENSE)
* nashorn

### ImShare (Frontend) is using:
* react js library with routing


## current version is 0.6-SNAPSHOT
* official release every 0.1 versions
* sub/fix versions are 0.01

## ImShare - Instalation:
* download ImShare from releases (newest)
* move .jar to runtime directory (can be /home/user/desktop/runtime)
* create Static and Notes folder
* open terminal and write:
* do not run as sudo
```bash
java -jar ImShare-VERSION.jar
```

### Todo version 1.0 - release:


* [ ] - Frontend for each not api website  

* [X] - GET/POST - notes

* [ ] - User dashboard

* [X] - Remove vulnerabilities for reverse directory

* [X] - Creating accounts

* [ ] - Admin panel

* [X] - Plugins support (for server side)

* [ ] - Plugins support (for frontend side)

* [ ] - Community panel (chat, forums)

* [ ] - Make ImShare public (Can be changed. Or published in other version)

* [X] - Create config file for changing properties (custom html files, custom limit of storage, and more!) 

* [ ] - Online file compilers (C/C++, java, rust, go)

* [X] - Server runtime info, logging (this will log runtime, errors, mass packet sent)

* [ ] - If user sends more than 10/s packets will be blocked for small-time (time can be changed, default=00:05:00)

* [ ] - Public user accounts (like steam etc)

* [X] - ImShare admin panel controll app

* [X] - Uploading notes/files (protected with password, burn after read etc)

* [X] - GZIP larger static files and etc


### Config file - ImShare.json
* best config can be found in /src/java/resource/config.json

## ImShare is licensed under MIT
[MIT LICENSE](https://choosealicense.com/licenses/mit/)
