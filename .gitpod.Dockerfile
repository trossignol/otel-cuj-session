FROM gitpod/workspace-full

USER gitpod

RUN bash -c "brew install httpie"

RUN bash -c ". /home/gitpod/.sdkman/bin/sdkman-init.sh && \
    sdk install java 21.0.4-ms && \
    sdk default java 21.0.4-ms"