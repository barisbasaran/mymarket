# syntax=docker/dockerfile:1.7-labs

FROM nginx:latest

COPY ./nginx.conf /etc/nginx/

RUN mkdir -p "/home2"

COPY --exclude=**/uploads ./static/ /home2/
WORKDIR "/home2"
RUN mkdir -p "/home2/uploads"
