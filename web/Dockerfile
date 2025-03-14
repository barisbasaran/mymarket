# syntax=docker/dockerfile:1.7-labs

FROM nginx:latest

COPY ./nginx.conf /etc/nginx/

COPY --exclude=**/uploads ./static/ /home/
WORKDIR "/home"
RUN mkdir -p "/home/uploads"
