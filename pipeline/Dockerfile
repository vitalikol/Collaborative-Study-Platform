FROM jenkins/jenkins:lts
USER root
RUN apt-get update && \
    apt-get install -y docker.io && \
    apt-get clean
ENTRYPOINT ["/bin/bash", "-c", "chmod 666 /var/run/docker.sock || true && exec /usr/local/bin/jenkins.sh"]
RUN usermod -aG docker jenkins
USER jenkins
