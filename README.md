# 📄 PDF Maker

**PDF Maker** est une application de bureau (*desktop only*) de gestion de fichiers PDF, **100 % hors ligne**. Vos documents ne quittent jamais votre ordinateur.

![Angular](https://img.shields.io/badge/Angular-DD0031?logo=angular&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?logo=springboot&logoColor=white)
![Tauri](https://img.shields.io/badge/Tauri-24C8DB?logo=tauri&logoColor=white)
![Offline](https://img.shields.io/badge/Mode-100%25%20hors%20ligne-success)
![AES-256](https://img.shields.io/badge/Chiffrement-AES--256-blue)

---

## ✨ Fonctionnalités

### 🔄 Conversion en PDF
- Conversion de fichiers **texte (`.txt`)** en PDF
- Conversion d'**images** en PDF

### 🧩 Gestion des PDF
- **Fusion** de plusieurs PDF en un seul document
- **Séparation** d'un PDF en plusieurs documents

### 🔐 Coffre-fort chiffré
- Chiffrement et déchiffrement de PDF en **AES-256**
- Les fichiers sont stockés **chiffrés** sur le disque, dans un dossier dédié : `mon-coffre-fort`
- Un PDF n'est lisible qu'après déchiffrement depuis l'application

### 🛡️ Authentification
- Système d'authentification conçu pour respecter les **recommandations de la CNIL** (politique de mot de passe, protection contre les tentatives répétées, stockage sécurisé des secrets)

### 📴 Confidentialité par conception
- **Aucune connexion Internet requise**
- **Aucune donnée envoyée à un serveur distant** : tout est traité et stocké localement

---

## 🏗️ Architecture et technologies

| Couche | Technologie | Rôle |
|--------|-------------|------|
| Interface | **Angular** | Interface utilisateur |
| Backend | **Spring Boot** (Java) | Logique métier : traitement PDF, chiffrement, authentification |
| Application desktop | **Tauri** | Empaquetage en application native légère |

Le backend Spring Boot s'exécute **en local** sur la machine de l'utilisateur et n'est pas exposé sur le réseau.

---

## 📋 Prérequis

- [Node.js](https://nodejs.org/) (LTS) et npm
- [Angular CLI](https://angular.dev/tools/cli)
- [JDK](https://adoptium.net/)  Maven
- [Rust](https://www.rust-lang.org/tools/install) et les [prérequis Tauri](https://tauri.app/start/prerequisites/) pour votre système

---

## 🚀 Installation et lancement

### 1. Cloner le dépôt

```bash
git clone https://github.com/<votre-utilisateur>/<nom-du-depot>.git
cd <nom-du-depot>
```

### 2. Installer les dépendances du frontend

```bash
cd frontend
npm install
```

### 3. Lancer en mode développement

```bash
# Backend Spring Boot
cd backend
./mvnw spring-boot:run

# Application desktop (Angular + Tauri)
cd frontend
npm run tauri dev
```

### 4. Générer l'installateur

```bash
cd frontend
npm run tauri build
```

L'installateur est généré dans `src-tauri/target/release/bundle/`.

> ⚠️ Adaptez les chemins et commandes à la structure réelle de votre projet.

---

## 🔒 Sécurité

- **Chiffrement** : AES-256 pour tous les PDF du coffre-fort
- **Stockage** : fichiers conservés chiffrés dans le dossier `mon-coffre-fort`
- **Local uniquement** : aucune donnée n'est transmise à l'extérieur
- **Authentification** : mise en œuvre alignée sur les recommandations de la CNIL

> ⚠️ **Important** : si vous perdez votre mot de passe, les fichiers du coffre-fort ne pourront pas être récupérés. Il n'existe aucune porte dérobée.

---

## 🗂️ Structure du projet

```
.
├── frontend/          # Application Angular
│   └── src-tauri/     # Configuration Tauri (Rust)
├── backend/           # API Spring Boot

```


---

## 🖼️ Captures d'écran

<img width="636" height="442" alt="pdfMaker2" src="https://github.com/user-attachments/assets/5c90c0bc-a865-47f0-8be2-da2260a0fcfb" />
<img width="605" height="353" alt="pdfMaker3" src="https://github.com/user-attachments/assets/203deaf4-76f5-4b74-96c7-9338c7589090" />
<img width="605" height="349" alt="pdfMaker4" src="https://github.com/user-attachments/assets/85c43316-4c5d-47da-b97c-f952f396753e" />
<img width="605" height="360" alt="pdfMaker5" src="https://github.com/user-attachments/assets/08579d4c-fb70-451d-9ea7-622d1b351454" />
<img width="636" height="442" alt="pdfMaker1" src="https://github.com/user-attachments/assets/27e7b240-615a-4d31-b082-708e6bfa2d92" />



---

## 🗺️ Feuille de route

- [ ] Compression de PDF
- [ ] Rotation et réorganisation des pages


---


---

## 👤 Auteur

**Siad Myriam** —
