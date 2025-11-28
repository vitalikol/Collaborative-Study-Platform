# **README.md**

# 🚀 CI/CD Pipeline with Jenkins, Docker, and Docker-in-Docker (DIND)

This project uses **Jenkins running inside Docker**, along with a separate **Docker-in-Docker (DIND)** service to build and deploy a Spring Boot backend container.

This setup allows Jenkins to build Docker images *inside* Docker, which is the recommended method on macOS and Windows.

---

# 📦 **Project Structure**

```
.
├── Dockerfile.jenkins       # Custom Jenkins image with Docker CLI installed
├── docker-compose.yml       # Runs Jenkins + Docker-in-Docker
├── server/                  # Spring Boot backend project
└── Jenkinsfile              # CI/CD pipeline definition
```

---

# 🐳 **Running Jenkins + Docker-in-Docker**

To start the CI/CD environment:

```bash
docker compose up -d --build
```

This launches:

| Service     | Description                                 |
| ----------- | ------------------------------------------- |
| **jenkins** | Jenkins LTS with Docker CLI installed       |
| **docker**  | docker:dind (Docker Daemon used by Jenkins) |

Jenkins UI:
👉 [http://localhost:8081](http://localhost:8081)

---

# 🔑 **Initial Jenkins Setup**

### 1. Get the admin password

```bash
docker exec -it jenkins bash
cat /var/jenkins_home/secrets/initialAdminPassword
```

### 2. Install suggested plugins

Choose: **Install suggested plugins**

### 3. Create admin user

Fill out the form normally.

---

# 🔐 **GitHub Credentials Setup**

To allow Jenkins to pull your repository, you **must add GitHub credentials**.

### Steps:

1. Go to:
   **Jenkins Dashboard → Manage Jenkins → Credentials → System → Global credentials**

2. Add **new credentials**:

    * Kind: **Username with password**
    * Username: **your GitHub username**
    * Password: **your GitHub Personal Access Token**
    * ID: `github-token` *(this must match Jenkinsfile)*

### Required GitHub Token Permissions:

* `repo`
* `read:org`
* `workflow`

---

# 🧪 **Jenkins Pipeline**

The CI/CD pipeline is defined in the `Jenkinsfile`.

### It performs:

1. **Checkout from GitHub**
2. **Maven build (inside server/ directory)**
3. **Docker image build using DIND**
4. **Restart of the backend container**
5. **Database volume persistence**

The backend starts in a separate container named `csp`.

---

# 🐳 **Where Does the Backend Run?**

The Spring Boot server runs **inside the Docker-in-Docker (dind) service**, not on your macOS host.

To expose it externally, the `docker` service in `docker-compose.yml` maps port **8080:8080**.

Backend URL:
👉 [http://localhost:8080](http://localhost:8080)

---

# 🧰 **Viewing Running Containers**

### On your host:

```bash
docker ps
```

Shows only Jenkins + DIND.

### Inside the DIND container:

```bash
docker exec -it docker bash
docker ps
```

Shows the backend `csp` container.

---

# 🔄 **Triggering a Build**

Each new commit invokes Jenkins:

* Maven builds the jar
* Docker builds the image `csp:latest`
* The old container is removed
* A new container is started with the same persistent volume

Data stored in `/app/data` persists thanks to:

```
- csp_data:/app/data
```

---

# 📁 **Persistent Database**

The backend uses a volume:

```
csp_data
```

So SQLite database **does NOT reset** when Jenkins rebuilds the image.

---

# 📝 **Useful Commands**

### Stop environment:

```bash
docker compose down
```

### Rebuild everything:

```bash
docker compose up -d --build
```

### View Jenkins logs:

```bash
docker logs -f jenkins
```

### View backend logs:

```bash
docker exec -it docker bash
docker logs -f csp
```

---

# 🎯 Summary

This repository includes a complete CI/CD setup with:

* Jenkins running inside Docker
* Docker-in-Docker daemon
* Automatic builds & deployments
* Persistent SQLite database
* Secure GitHub credentials
* Fully automated pipeline

You can now push changes to GitHub → Jenkins automatically builds & deploys your backend.